package com.feup.jhotsketch.group;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Group")
public class GroupShape extends Shape{

	private static int PADDING = 5;
	
	private List<Shape> shapes = new LinkedList<Shape>();
	private LinkedList<Connector> connectors = new LinkedList<Connector>();
	
	public GroupShape() {
		super(0, 0, 0, 0);
		setLineColor(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}

	@Override
	public void paint(GC gc) {
		super.paint(gc);
		if (getFillColor() != null) gc.fillRectangle(getBounds());
		gc.drawRectangle(getBounds());
		for (Connector connector : connectors) {
			connector.paint(gc);
		}
		for (Shape shape : shapes) {
			shape.paint(gc);
		}
		drawText(gc);
	}
	
	@Override
	public void setLocation(int x, int y) {
		for (Shape shape : shapes) {
			shape.setLocation(x, y);
		}
		shapeChanged();
	}
	
	@Override
	public void move(int dx, int dy) {
		for (Shape shape : shapes) {
			shape.move(dx, dy);
		}
		shapeChanged();
	}

	
	@Override
	public Shape clone() {
		return null;
	}

	public void addShapes(Set<Shape> shapes){
		this.shapes.addAll(shapes);
	}

	public void addConnectors(Set<Connector> connectors){
		this.connectors.addAll(connectors);
	}

	@Override
	public Rectangle2D getBounds2D() {
		Rectangle2D bounds = null;
		for (Shape shape : shapes) {
			if (bounds == null) bounds = shape.getBounds2D();
			else bounds = bounds.createUnion(shape.getBounds2D());
		}
		bounds.setRect(bounds.getX() - PADDING, bounds.getY() - PADDING, bounds.getWidth() + PADDING * 2, bounds.getHeight() + PADDING * 2);
		return bounds;
	}
	
}