package com.feup.jhotsketch.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.connector.Connector.ENDTYPE;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.diagram.DiagramObserver;
import com.feup.jhotsketch.handle.Handle;
import com.feup.jhotsketch.handle.HandlerFactory;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Controller")
public class DiagramController implements MouseListener, MouseMoveListener, DiagramObserver {
	private final Diagram diagram;
	private List<ControllerObserver> observers = new LinkedList<ControllerObserver>();

	private Set<Shape> selectedShapes = new HashSet<Shape>();
	private Set<Connector> selectedConnectors = new HashSet<Connector>();
	
	private ShapeController controller;
	
	public DiagramController(Diagram diagram) {
		this.diagram = diagram;
		diagram.addDiagramObserver(this);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		for(Connector connector : getSelectedConnectors()) {
			List<Handle> handles = HandlerFactory.getHandlesFor(connector);
			for (Handle handle : handles) {
				if (handle.contains(e.x, e.y))
					new HandleController(handle, this).doubleClick(e.x, e.y);
			}
		}
		
		Set<Shape> selectedShapes = getSelectedShapes();
		for (Shape shape : selectedShapes) {
			final Text editor = new Text((Composite)e.getSource(), SWT.SINGLE);
			int width = Math.max(shape.getBounds().width, 100);
			editor.setSize(width, 20);
			editor.setLocation(shape.getBounds().x + shape.getBounds().width / 2 - width / 2, shape.getBounds().y + shape.getBounds().height / 2 - 10);
			editor.setData(shape);
			editor.setText(shape.getText());
			editor.setFocus();
						
			editor.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.keyCode == 13) {
						Shape shape = (Shape) editor.getData();
						shape.setText(editor.getText());
						editor.dispose();
					}
				}
			});
		}
	}

	public boolean startMoveSelected(int x, int y) {
		List<Shape> foundShapes = diagram.getShapesAt(x, y);
		for (Shape shape : selectedShapes) {
			if (foundShapes.contains(shape)) {
				controller = new MoveController(selectedShapes, diagram);
				controller.mouseDown(x, y);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		for(Connector connector : getSelectedConnectors()) {
			List<Handle> handles = HandlerFactory.getHandlesFor(connector);
			for (Handle handle : handles) {
				if (handle.contains(e.x, e.y)) {
					controller = new HandleController(handle, this);
					controller.mouseDown(e.x, e.y);
				}
			}
		}

		for (Shape shape : selectedShapes) {
			List<Handle> handles = HandlerFactory.getHandlesFor(shape);
			for (Handle handle : handles) {
				if (handle.contains(e.x, e.y)) {
					controller = new HandleController(handle, this);
					controller.mouseDown(e.x, e.y);
					return;
				}
			}
		}
		
		List<Shape> foundShapes = diagram.getShapesAt(e.x, e.y);
		if (foundShapes.size() == 0) {
			List<Connector> foundConnectors = diagram.getConnectorsAt(e.x, e.y);
			if (foundConnectors.size() == 0) {
				controller = new SelectionController();
				controller.mouseDown(e.x, e.y);
			} else {
				if ((e.stateMask & SWT.CONTROL) == 0) clearSelection();
				selectedConnectors.add(foundConnectors.get(0));
			}
		} else {
			
			// MOVE SELECTED
			if (startMoveSelected(e.x, e.y)) return;
			
			// SELECT
			Shape next = getNextSelection(foundShapes);
			if ((e.stateMask & SWT.CONTROL) == 0) clearSelection();
			selectedShapes.add(next);
			controller = new MoveController(selectedShapes, diagram);
			controller.mouseDown(e.x, e.y);
		}
		controllerChanged();
	}

	private Shape getNextSelection(List<Shape> shapes) {
		Shape found = null;
		for (Shape shape : shapes) {
			if (selectedShapes.contains(shape)) found = null;
			else found = shape;
		}
		if (found == null) found = shapes.get(0);
		return found;
	}

	public void selectShape(Shape shape) {
		selectedShapes.add(shape);
		controllerChanged();
	}
	
	@Override
	public void mouseMove(MouseEvent e) {
		mouseMove(e.x, e.y);
	}

	public void mouseMove(int x, int y) {
		if (controller != null) {
			controller.mouseMove(x, y);
			controllerChanged();
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		mouseUp(e.x, e.y, e.stateMask);
	}

	public void clearSelection() {
		selectedConnectors.clear(); 
		selectedShapes.clear();
		controllerChanged();
	}
	
	public void mouseUp(int x, int y, int stateMask) {
		if (controller instanceof SelectionController) {
			if ((stateMask & SWT.SHIFT) == 0) clearSelection();
			selectedShapes.addAll(diagram.getShapesIn(((SelectionController)controller).getSelectionRectangle()));
			selectedConnectors.addAll(diagram.getConnectorsIn(((SelectionController)controller).getSelectionRectangle()));
		}
		if (controller instanceof MoveController) {
			if (!((MoveController)controller).moved()) {
				List<Shape> found = diagram.getShapesAt(x, y);
				Shape next = getNextSelection(found);
				if ((stateMask & SWT.CONTROL) == 0) clearSelection();
				selectedShapes.add(next);
			}
		}
		if (controller instanceof HandleController) {
			controller.mouseUp(x, y);
		}
		controllerChanged();
		controller = null;
	}
	
	public void controllerChanged() {
		for (ControllerObserver observer : observers ) {
			observer.controllerChanged(this);
		}
	}

	public void addControllerObserver(ControllerObserver observer) {
		observers.add(observer);
	}

	public void removeControllerObserver(ControllerObserver observer) {
		observers.remove(observer);
	}

	public void paint(GC gc) {
		for (Shape shape : selectedShapes) {
			List<Handle> handles = HandlerFactory.getHandlesFor(shape);
			for (Handle handle : handles) {
				handle.paint(gc);
			}
		}
		for (Connector connector : selectedConnectors) {
			List<Handle> handles = HandlerFactory.getHandlesFor(connector);
			for (Handle handle : handles) {
				handle.paint(gc);
			}
		}
		if (controller != null)	controller.paint(gc);
	}

	public Set<Shape> getSelectedShapes() {
		return selectedShapes;
	}

	public void setSelectedLineStyle(int style) {
		for (Shape shape : selectedShapes) shape.setLineStyle(style);
		for (Connector connector : selectedConnectors) connector.setLineStyle(style);
	}

	public void setSelectedLineWidth(int width) {
		for (Shape shape : selectedShapes) shape.setLineWidth(width);
		for (Connector connector : selectedConnectors) connector.setLineWidth(width);
	}

	public void setSelectedLineColor(Color color) {
		for (Shape shape : selectedShapes) shape.setLineColor(color);
		for (Connector connector : selectedConnectors) connector.setLineColor(color);
	}

	public void setSelectedFillColor(Color color) {
		for (Shape shape : selectedShapes) shape.setFillColor(color);
	}

	public Set<Connector> getSelectedConnectors() {
		return selectedConnectors;
	}

	public Set<Color> getSelectedLineColors() {
		Set<Color> lineColors = new HashSet<Color>();
		for (Shape shape : selectedShapes) lineColors.add(shape.getLineColor());
		for (Connector connector : selectedConnectors) lineColors.add(connector.getLineColor());
		return lineColors;
	}

	public Set<Color> getSelectedFillColors() {
		Set<Color> fillColors = new HashSet<Color>();
		for (Shape shape : selectedShapes) fillColors.add(shape.getFillColor());
		return fillColors;
	}

	public Set<Integer> getSelectedLineWidths() {
		Set<Integer> lineWidths = new HashSet<Integer>();
		for (Shape shape : selectedShapes) lineWidths.add(new Integer(shape.getLineWidth()));
		for (Connector connector : selectedConnectors) lineWidths.add(new Integer(connector.getLineWidth()));
		return lineWidths;
	}

	public Set<Integer> getSelectedLineStyles() {
		Set<Integer> lineStyles = new HashSet<Integer>();
		for (Shape shape : selectedShapes) lineStyles.add(new Integer(shape.getLineStyle()));
		for (Connector connector : selectedConnectors) lineStyles.add(new Integer(connector.getLineStyle()));
		return lineStyles;
	}

	@Override
	public void diagramChanged(Diagram diagram) {
		controllerChanged();
	}

	public void selectAll() {
		selectedConnectors.addAll(diagram.getConnectors());
		selectedShapes.addAll(diagram.getShapes());
		controllerChanged();
	}

	public void removeSelected() {
		diagram.removeShapes(selectedShapes);
		diagram.removeConnectors(selectedConnectors);
		selectedConnectors.clear();
		selectedShapes.clear();
		removeOrphanConnectors();
	}
	
	private void removeOrphanConnectors() {
		Set<Connector> toRemove = new HashSet<Connector>();
		for (Connector connector : diagram.getConnectors()) {
			if (diagram.getShapes().contains(connector.getSource()) && diagram.getShapes().contains(connector.getTarget())) continue;
			toRemove.add(connector);
		}
		diagram.removeConnectors(toRemove);
	}

	public void setSelectedTargetEndSize(int size) {
		for (Connector connector : selectedConnectors) connector.setTargetEndSize(size);
	}

	public void setSelectedSourceEndSize(int size) {
		for (Connector connector : selectedConnectors) connector.setSourceEndSize(size);
	}

	public Set<Integer> getSelectedTargetEndSizes() {
		Set<Integer> sizes = new HashSet<Integer>();
		for (Connector connector : selectedConnectors) sizes.add(new Integer(connector.getTargetEndSize()));
		return sizes;
	}

	public Set<Integer> getSelectedAlphas() {
		Set<Integer> alphas = new HashSet<Integer>();
		for (Shape shape : selectedShapes) alphas.add(new Integer(shape.getAlpha()));
		return alphas;
	}
	
	public Set<Integer> getSelectedSourceEndSizes() {
		Set<Integer> sizes = new HashSet<Integer>();
		for (Connector connector : selectedConnectors) sizes.add(new Integer(connector.getSourceEndSize()));
		return sizes;
	}

	public void setSelectedTargetEndType(ENDTYPE endType) {
		for (Connector connector : selectedConnectors) connector.setTargetEndType(endType);		
	}

	public void setSelectedSourceEndType(ENDTYPE endType) {
		for (Connector connector : selectedConnectors) connector.setSourceEndType(endType);		
	}

	public Set<ENDTYPE> getSelectedSourceEndTypes() {
		Set<ENDTYPE> types = new HashSet<ENDTYPE>();
		for (Connector connector : selectedConnectors) types.add(connector.getSourceEndType());
		return types;
	}

	public Set<ENDTYPE> getSelectedTargetEndTypes() {
		Set<ENDTYPE> types = new HashSet<ENDTYPE>();
		for (Connector connector : selectedConnectors) types.add(connector.getTargetEndType());
		return types;
	}

	public void addShape(Shape shape) {
		diagram.addShape(shape);
		selectedShapes.clear();
		selectedConnectors.clear();
		selectedShapes.add(shape);
	}

	public Diagram getDiagram() {
		return diagram;
	}

	public void addConnector(Connector connector) {
		diagram.addConnector(connector);
		selectedShapes.clear();
		selectedConnectors.clear();
		selectedConnectors.add(connector);		
	}

	public void moveSelected(int dx, int dy) {
		for (Shape shape : selectedShapes)
			shape.move(dx, dy);
	}

	public ShapeController getCurrentController() {
		return controller;
	}

	public void setSelectedAlpha(int alpha) {
		for (Shape shape : selectedShapes) shape.setAlpha(alpha);		
	}

	public void selectMany(Collection<Shape> shapes, Collection<Connector> controllers) {
		selectedConnectors.addAll(controllers);
		selectedShapes.addAll(shapes);
		controllerChanged();
	}

	public void selectConnector(Connector connector) {
		selectedConnectors.add(connector);
		controllerChanged();
	}

	public void setSelectedFont(Font font) {
		for (Shape shape : selectedShapes) shape.setFont(font);		
	}
}