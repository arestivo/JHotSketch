package com.feup.jhotsketch.diagram;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.connector.ConnectorObserver;
import com.feup.jhotsketch.shape.Shape;
import com.feup.jhotsketch.shape.ShapeObserver;

@PackageName("Diagram")
public class Diagram implements ShapeObserver, ConnectorObserver {
	private List<Shape> shapes = new LinkedList<Shape>();
	private List<Connector> connectors = new LinkedList<Connector>();

	private List<DiagramObserver> observers = new LinkedList<DiagramObserver>();
	
	public Diagram() {
	}
	
	public void addShape(Shape shape) {
		shapes.add(shape);
		shape.addShapeObserver(this);
		diagramChanged();
	}
	
	public List<Shape> getShapes() {
		return shapes;
	}

	public void addConnector(Connector connector) {
		connectors.add(connector);
		connector.addObserver(this);
	}
	
	public List<Connector> getConnectors() {
		return connectors;
	}
	
	public void addDiagramObserver(DiagramObserver observer) {
		observers.add(observer);
	}

	public void removeDiagramObserver(DiagramObserver observer) {
		observers.remove(observer);
	}
	
	protected void diagramChanged() {
		for (DiagramObserver observer : observers) {
			observer.diagramChanged(this);
		}
	}

	@Override
	public void shapeChanged(Shape shape) {
		diagramChanged();
	}

	@Override
	public void connectorChanged(Connector connector) {
		diagramChanged();
	}

	public void paint(GC gc) {
		for (Connector connector : connectors) {
			connector.paint(gc);
		}
		for (Shape shape : shapes) {
			shape.paint(gc);
		}
	}

	public List<Shape> getShapesAt(int x, int y) {
		List<Shape> found = new LinkedList<Shape>();
		for (Shape shape : shapes) {
			if (shape.contains(x, y)) found.add(shape);
		}
		return found;
	}

	public List<Shape> getShapesIn(Rectangle rectangle) {		
		List<Shape> found = new LinkedList<Shape>();
		for (Shape shape : shapes)
			if (rectangle.intersection(shape.getBounds()).equals(shape.getBounds())) found.add(shape);
		return found;
	}

	public List<Connector> getConnectorsAt(int x, int y) {
		List<Connector>found = new LinkedList<Connector>();
		for (Connector connector : connectors)
			if (connector.contains(x, y)) found.add(connector);
		return found;
	}

	public List<Connector> getConnectorsIn(Rectangle rectangle) {
		List<Connector>found = new LinkedList<Connector>();
		for (Connector connector : connectors)
			if (rectangle.intersection(connector.getBounds()).equals(connector.getBounds())) found.add(connector);
		return found;
	}

	public void removeShapes(Set<Shape> shapes) {
		this.shapes.removeAll(shapes);
		diagramChanged();
	}

	public void removeConnectors(Set<Connector> connectors) {
		this.connectors.removeAll(connectors);
		diagramChanged();
	}
}