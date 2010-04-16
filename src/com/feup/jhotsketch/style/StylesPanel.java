package com.feup.jhotsketch.style;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;
import com.feup.jhotsketch.application.ApplicationObserver;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.shape.RectangleShape;
import com.feup.jhotsketch.shape.Shape;
import com.feup.jhotsketch.viewer.Viewer;

@PackageName("Style")
public class StylesPanel extends Composite{

	private List<Shape> shapes = new LinkedList<Shape>();
	private List<Connector> connectors = new LinkedList<Connector>();

	private Shape selectedShapeStyle = null;
	private Connector selectedConnectorStyle = null;

	private Group shapeGroup;
	private Group connectorGroup;
	
	private static StylesPanel instance = null;

	public static StylesPanel getInstance() {
		return instance;
	}
	
	public StylesPanel(Composite composite, int style) {
		super(composite, SWT.NONE);
		setLayout(new GridLayout(1, true));
		
		shapeGroup = new Group(this, SWT.NONE);
		shapeGroup.setText("Shape Styles");

		connectorGroup = new Group(this, SWT.NONE);
		connectorGroup.setText("Connector Styles");
		
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;

		setLayoutData(gd);

		shapeGroup.setLayoutData(gd);
		connectorGroup.setLayoutData(gd);
		
		shapeGroup.addPaintListener(new PaintListener() {			
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setAntialias(SWT.ON);
				paintShapes(e.gc);
			}
		});
		
		connectorGroup.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setAntialias(SWT.ON);
				paintConnectors(e.gc);
			}
		});

		Application.getInstance().addApplicationObserver(new ApplicationObserver() {
			@Override
			public void diagramSelected(Viewer viewer) {
				update(viewer.getDiagram());
			}
		});

		shapeGroup.addMouseListener(new MouseListener() {
			private boolean pendingClick = false;

			@Override
			public void mouseUp(MouseEvent e) {
				pendingClick  = true;
				final int x = e.x; final int y = e.y;
				Display.getCurrent().timerExec(Display.getCurrent().getDoubleClickTime() + 3, 
				new Runnable() {
					@Override
					public void run() {
						if (!pendingClick) return;
						pendingClick = false;
						Shape style = getShape(x, y);
						setSelectedShapeStyle(style);
						if (style == null) return;
						DiagramController controller = Application.getInstance().getActiveController();
						Diagram diagram = controller.getDiagram();
						controller.clearSelection();
						for (Shape shape : diagram.getShapes()) 
							if (shape.compareStyles(style)) controller.selectShape(shape);
						update(diagram);
					}
				});
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				pendingClick = false;
				Shape style = getShape(e.x, e.y);
				if (style == null) return;
				DiagramController controller = Application.getInstance().getActiveController();
				Diagram diagram = controller.getDiagram();

				for (Shape shape : controller.getSelectedShapes()) 
					shape.copyProperties(style);
				update(diagram);
			}
		});

		connectorGroup.addMouseListener(new MouseListener() {
			private boolean pendingClick = false;

			@Override
			public void mouseUp(MouseEvent e) {
				pendingClick  = true;
				final int x = e.x; final int y = e.y;
				Display.getCurrent().timerExec(Display.getCurrent().getDoubleClickTime() + 3, 
				new Runnable() {
					@Override
					public void run() {
						if (!pendingClick) return;
						pendingClick = false;
						Connector style = getConnector(x, y);
						setSelectedConnectorStyle(style);
						if (style == null) return;
						DiagramController controller = Application.getInstance().getActiveController();
						Diagram diagram = controller.getDiagram();
						controller.clearSelection();
						for (Connector connector : diagram.getConnectors()) 
							if (connector.compareStyles(style)) controller.selectConnector(connector);
						update(diagram);
					}

				});
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				pendingClick = false;
				Connector style = getConnector(e.x, e.y);
				if (style == null) return;
				DiagramController controller = Application.getInstance().getActiveController();
				Diagram diagram = controller.getDiagram();

				for (Connector connector : controller.getSelectedConnectors()) 
					connector.copyProperties(style);
				update(diagram);
			}
		});
		
		pack();
		instance = this;
	}

	private Shape getShape(int x, int y) {
		for (Shape shape : shapes) if (shape.contains(x, y)) return shape;
		return null;
	}

	private Connector getConnector(int x, int y) {
		for (Connector connector : connectors) {
			Rectangle r = new Rectangle(connector.getSource().getBounds().x,connector.getSource().getBounds().y, connector.getTarget().getBounds().x - connector.getSource().getBounds().x + 1, connector.getSource().getBounds().height);
			if (r.contains(x, y)) return connector;
		}
		return null;
	}
	
	protected void paintShapes(GC gc) {
		for (Shape shape : shapes)
			shape.paint(gc);
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		if (getSelectedShapeStyle() != null)
			gc.fillRectangle(getSelectedShapeStyle().getBounds().x, getSelectedShapeStyle().getBounds().y + getSelectedShapeStyle().getBounds().height + 3, getSelectedShapeStyle().getBounds().width, 3);
	}

	private void paintConnectors(GC gc) {
		for(Connector connector : connectors) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineWidth(1);
			gc.fillRectangle(connector.getSource().getBounds().x,connector.getSource().getBounds().y, connector.getTarget().getBounds().x - connector.getSource().getBounds().x + 1, connector.getSource().getBounds().height);
			gc.drawRectangle(connector.getSource().getBounds().x,connector.getSource().getBounds().y, connector.getTarget().getBounds().x - connector.getSource().getBounds().x + 1, connector.getSource().getBounds().height);
			connector.paint(gc);
		}
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		if (getSelectedConnectorStyle() != null)
			gc.fillRectangle(getSelectedConnectorStyle().getSource().getBounds().x, getSelectedConnectorStyle().getSource().getBounds().y + getSelectedConnectorStyle().getSource().getBounds().height + 3, getSelectedConnectorStyle().getTarget().getBounds().x - getSelectedConnectorStyle().getSource().getBounds().x + 1, 3);
	}
	
	public void update(Diagram diagram) {
		shapes.clear();
		connectors.clear();
		int c = 0, l = 0;
		int width = shapeGroup.getBounds().width;
		int swidth = (width - 50) / 4; 
		int sheight = swidth * 3 / 4; 
		
		for (Shape shape : diagram.getShapes()) {
			boolean found = false;
			for (Shape style : shapes) {
				if (style.compareStyles(shape)) found = true;
			}
			if (!found) {
				RectangleShape rect = new RectangleShape(c * swidth + (c + 1) * 10, l * sheight + (l + 1) * 10 + 10 , swidth, sheight);
				rect.copyProperties(shape);
				rect.setText("T");
				shapes.add(rect);
				c++; if (c == 4) {c = 0; l++;}
			}
		}
		if (getSelectedShapeStyle() == null && shapes.size() > 0) setSelectedShapeStyle(shapes.get(0));

		boolean found = false;
		for (Shape style : shapes) {
			if (style.compareStyles(getSelectedShapeStyle())) found = true;
		}
		if (!found && shapes.size() > 0) setSelectedShapeStyle(shapes.get(0));
		
		shapeGroup.redraw();

		c = 0; l = 0;
		for (Connector connector : diagram.getConnectors()) {
			boolean foundConnector = false;
			for (Connector style : connectors)
				if (style.compareStyles(connector)) foundConnector = true;
			if (!foundConnector) {
				RectangleShape rs1 = new RectangleShape(c * swidth + (c + 1) * 10, l * sheight + (l + 1) * 10 + 10 , 1, sheight);
				RectangleShape rs2 = new RectangleShape((c + 1) * swidth + (c + 1) * 10, l * sheight + (l + 1) * 10 + 10 , 1, sheight);
				Connector conn = new Connector(rs1, rs2);
				conn.copyProperties(connector);
				connectors.add(conn);
				c++; if (c == 4) {c = 0; l++;}
			}
		}
		if (getSelectedConnectorStyle() == null && connectors.size() > 0) setSelectedConnectorStyle(connectors.get(0));
		boolean foundConnector = false;
		for (Connector style : connectors) {
			if (style.compareStyles(getSelectedConnectorStyle())) foundConnector = true;
		}
		if (!foundConnector && connectors.size() > 0) setSelectedConnectorStyle(connectors.get(0));
		connectorGroup.redraw();

	}

	private void setSelectedConnectorStyle(Connector connector) {
		this.selectedConnectorStyle = connector;
	}

	Connector getSelectedConnectorStyle() {
		return selectedConnectorStyle;
	}

	private void setSelectedShapeStyle(Shape selectedStyle) {
		this.selectedShapeStyle = selectedStyle;
	}

	public Shape getSelectedShapeStyle() {
		return selectedShapeStyle;
	}	
}