package com.feup.jhotsketch.controller;

import java.util.Set;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Controller")
public class MoveController implements ShapeController{

	private final Set<Shape> shapes;
	private Point lastPoint;
	private boolean moved = false;
	private Diagram diagram;
	
	public MoveController(Set<Shape> selected, Diagram diagram) {
		this.shapes = selected;
		this.diagram = diagram;
	}

	@Override
	public void mouseDown(int x, int y) {
		lastPoint = new Point(x, y);
	}
	
	@Override
	public void mouseMove(int x, int y) {
		for (Shape shape : shapes) {
			shape.move(x - lastPoint.x, y - lastPoint.y);
		}
		for (Connector connector : diagram.getConnectors()) {
			if (shapes.contains(connector.getSource()) && shapes.contains(connector.getTarget()))
			connector.move(x - lastPoint.x, y - lastPoint.y);
		}
		lastPoint = new Point(x, y);
		moved = true;
	}
	
	public boolean moved() {
		return moved;
	}

	@Override
	public void mouseUp(int x, int y) {
	}

	@Override
	public void paint(GC gc) {		
	}

	public Set<Shape> getShapes() {
		return shapes;
	}
}