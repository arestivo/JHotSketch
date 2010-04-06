package com.feup.jhotsketch.shape;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Shape")
public abstract class Shape {
	protected Rectangle2D bounds;

	private Color lineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private Color fillColor = null;
	private int lineWidth = 1;
	private int lineStyle = SWT.LINE_SOLID;
	
	public List<ShapeObserver> observers = new LinkedList<ShapeObserver>();
	
	public Shape(double x, double y, double width, double height) {
		bounds = new Rectangle2D.Double(x, y, width, height);
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
		shapeChanged();
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		shapeChanged();
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		shapeChanged();
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
		shapeChanged();
	}

	public int getLineStyle() {
		return lineStyle;
	}
	
	public void addShapeObserver(ShapeObserver observer) {
		observers.add(observer);
	}
	
	protected void shapeChanged() {
		for (ShapeObserver observer : observers) {
			observer.shapeChanged(this);
		}
	}

	public void paint(GC gc) {
		gc.setLineWidth(lineWidth);
		gc.setForeground(lineColor);
		gc.setLineStyle(lineStyle);
		if (fillColor != null) gc.setBackground(fillColor);
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
	}

	public boolean contains(int x, int y) {
		Rectangle2D rectangle = new Rectangle2D.Double(getBounds().x - getLineWidth() / 2, getBounds().y - getLineWidth() / 2, getBounds().width + getLineWidth(), getBounds().height + getLineWidth());
		return (rectangle.contains(x, y));
	}

	public boolean contains(double x, double y) {
		Rectangle2D rectangle = new Rectangle2D.Double(bounds.getX() - getLineWidth() / 2, bounds.getY() - getLineWidth() / 2, bounds.getWidth() + getLineWidth(), bounds.getHeight() + getLineWidth());
		return (rectangle.contains(x, y));
	}

	public void setLocation(int x, int y) {
		bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
		shapeChanged();
	}
	
	public void move(int dx, int dy) {
		bounds.setRect(bounds.getX() + dx, bounds.getY() + dy, bounds.getWidth(), bounds.getHeight());
		shapeChanged();
	}

	public void resize(int dx, int dy) {
		bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth() + dx, bounds.getHeight() + dy);
		shapeChanged();
	}

	public void correctBounds() {
		if (bounds.getWidth() < 0) bounds.setRect(bounds.getX() + bounds.getWidth(), bounds.getY(), -bounds.getWidth(), bounds.getHeight());
		if (bounds.getHeight() < 0) bounds.setRect(bounds.getX(), bounds.getY() + bounds.getHeight(), bounds.getWidth(), -bounds.getHeight());
	}

	public Rectangle2D getBounds2D() {
		return bounds;
	}
	
	public abstract Shape clone();
	
	protected void copyProperties(Shape other) {
		setFillColor(other.getFillColor());
		setLineColor(other.getLineColor());
		setLineStyle(other.getLineStyle());
		setLineWidth(other.getLineWidth());
	}

}