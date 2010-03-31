package com.feup.jhotsketch.connector;

import java.awt.Rectangle;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.geometry.Intersector;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.Handle;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.DiagramView;

@PackageName("Connector")
public aspect Connector {
	private List<ConnectorModel>	DiagramModel.connectors = new LinkedList<ConnectorModel>();
	private Set<ConnectorModel>	DiagramModel.selectedConnectors = new HashSet<ConnectorModel>();
	
	public void DiagramModel.addConnector(ConnectorModel connector) {
		connectors.add(connector);
	}

	public void DiagramModel.removeConnectors(Set<ConnectorModel> toRemove) {
		connectors.removeAll(toRemove);
	}

	public List<ConnectorModel> DiagramModel.getConnectors() {
		return connectors;
	}

	public void DiagramModel.toggleConnectorSelected(ConnectorModel connector) {
		if (selectedConnectors.contains(connector))
			selectedConnectors.remove(connector);
		else 
			selectedConnectors.add(connector);
		connector.toggleSelected();
	}

	public void DiagramModel.unselectAllConnectors() {
		for (ConnectorModel connector : selectedConnectors) {
			connector.toggleSelected();
		}
		selectedConnectors.clear();
	}

	public Set<ConnectorModel> DiagramModel.getSelectedConnectors() {
		return selectedConnectors;
	}

	private Point ShapeModel.connectingPoint = new Point(0, 0);
	
	public Point ShapeModel.getConnectingPoint() {
		return connectingPoint;
	}

	public void ShapeModel.setConnectingPoint(int x, int y) {
		this.connectingPoint = new Point(x, y);
	}
		
	pointcut diagramPaint(GC gc, DiagramView view) :
		call (void DiagramView.paint(GC,..)) && args(gc,..) && target(view);
	
	before(GC gc, DiagramView view) : diagramPaint(gc, view) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		for (ConnectorModel connector : view.getDiagram().getConnectors()) {
			ShapeModel source = connector.getSource();
			ShapeModel sink = connector.getSink();
			Point p1 = new Point(source.getBounds().x + source.getBounds().width / 2, source.getBounds().y + source.getBounds().height / 2);
			Point p2 = new Point(sink.getBounds().x + sink.getBounds().width / 2, sink.getBounds().y + sink.getBounds().height / 2);
			if (sink instanceof OvalModel) p2 = Intersector.intersectOval(p1, p2, sink.getBounds());
			else p2 = Intersector.intersectRectangle(p1, p2, sink.getBounds());
			if (source instanceof OvalModel) p1 = Intersector.intersectOval(p2, p1, source.getBounds());
			else p1 = Intersector.intersectRectangle(p2, p1, source.getBounds());
			gc.drawLine(p1.x, p1.y, p2.x, p2.y);
			
			ConnectorEndPainter.paintConnectorEnd(p1, p2, connector.getSinkEnd(), connector.getEndSize(), gc);
			ConnectorEndPainter.paintConnectorEnd(p2, p1, connector.getSourceEnd(), connector.getEndSize(), gc);
			
			connector.setBounds(new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y)));
			
			if (connector.isSelected()) {
				gc.setBackground(Display.getCurrent().getSystemColor((SWT.COLOR_DARK_GRAY)));
				gc.fillRectangle((p1.x + p2.x) / 2 - Handle.getSize() / 2, (p1.y + p2.y) / 2 - Handle.getSize() / 2, Handle.getSize(), Handle.getSize());
			}
		}
		for (ShapeModel shape : view.getDiagram().getFigures()) {
			if (!shape.getConnectingPoint().equals(new Point(0,0)))
				gc.drawLine(
						shape.getBounds().x + shape.getBounds().width / 2, 
						shape.getBounds().y + shape.getBounds().height / 2, 
						shape.getBounds().x + shape.getBounds().width / 2 + shape.connectingPoint.x, 
						shape.getBounds().y + shape.getBounds().height / 2 + shape.connectingPoint.y
				);
		}
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	

	pointcut createHandles(ShapeModel shape) :
		call (void ShapeModel.createHandles()) && target(shape);
		
	after(ShapeModel shape) : createHandles(shape) {
		shape.addHandle(new Handle(shape, shape.getBounds().width, shape.getBounds().height / 2, "CONNECTOR", "TRIANGLE", SWT.COLOR_DARK_RED));
	}

	pointcut moveHandle(ShapeModel shape, int rx, int ry, Handle handle) :
		call (void ShapeModel.moveHandle(int, int, Handle)) && target(shape) && args(rx, ry, handle);
	
	after(ShapeModel shape, int rx, int ry, Handle handle) : moveHandle(shape, rx, ry, handle) {
		if (handle.getId().equals("CONNECTOR")) {
			if (shape.getConnectingPoint().equals(new Point(0, 0))) shape.setConnectingPoint(shape.getBounds().width / 2, 0);
			shape.setConnectingPoint(shape.getConnectingPoint().x + rx, shape.getConnectingPoint().y + ry);
		}
	}

	pointcut dropHandle(ShapeModel shape, int rx, int ry, Handle handle) :
		call (void ShapeModel.dropHandle(int, int, Handle)) && target(shape) && args(rx, ry, handle);
	
	after(ShapeModel shape, int rx, int ry, Handle handle) : dropHandle(shape, rx, ry, handle) {
		if (handle.getId().equals("CONNECTOR")) {
			shape.setConnectingPoint(shape.getConnectingPoint().x + (int)rx, shape.getConnectingPoint().y + (int)ry);
			
			DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
			for (ShapeModel oshape : diagram.getFigures()) {
				if (oshape.contains(shape.getBounds().x + shape.getBounds().width / 2 + shape.connectingPoint.x, shape.getBounds().y + shape.getBounds().height / 2 + shape.connectingPoint.y)) {
					diagram.addConnector(new ConnectorModel(shape, oshape));
					break;
				}
			}
			
			shape.setConnectingPoint(0, 0);
		}
	}

	pointcut removeFigure(DiagramModel diagram):
		call (void DiagramModel.removeFigure*(..)) && target(diagram);
		
	after(DiagramModel diagram) : removeFigure(diagram) {
		Set<ConnectorModel> toRemove = new HashSet<ConnectorModel>();
		for (ConnectorModel	connector : diagram.getConnectors()) {
			if (!diagram.getFigures().contains(connector.getSink())) toRemove.add(connector);
			if (!diagram.getFigures().contains(connector.getSource())) toRemove.add(connector);
		}
		diagram.removeConnectors(toRemove);
	}

	pointcut mouseDown(Event event) : 
		call (void DiagramController.mouseDown(Event)) && args(event);

	after(Event event) : mouseDown(event){
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		if (diagram.getSelected().size()!=0) diagram.unselectAllConnectors();
		if ((event.stateMask & SWT.CTRL) == 0) diagram.unselectAllConnectors();
		for (ConnectorModel connector : diagram.getConnectors()) {
			if (connector.contains(event.x, event.y)) {
				diagram.toggleConnectorSelected(connector);
				diagram.unselectAll();
			}
		}
	}
}