package com.feup.jhotsketch.shape;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
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
	private int alpha = 255;

	private Color textColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private String text = "";
	
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
		gc.setAlpha(alpha);
		gc.setLineWidth(lineWidth);
		gc.setForeground(lineColor);
		gc.setLineStyle(lineStyle);
		if (fillColor != null) gc.setBackground(fillColor);
	}
	
	protected void drawText(GC gc) {
		gc.setForeground(textColor);
		Point size = gc.textExtent(text);
		gc.drawText(text, getBounds().x + getBounds().width / 2 - size.x / 2, getBounds().y + getBounds().height / 2 - size.y / 2);
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)getBounds2D().getX(), (int)getBounds2D().getY(), (int)getBounds2D().getWidth(), (int)getBounds2D().getHeight());
	}

	public boolean contains(int x, int y) {
		Rectangle2D rectangle = new Rectangle2D.Double(getBounds().x - getLineWidth() / 2, getBounds().y - getLineWidth() / 2, getBounds().width + getLineWidth(), getBounds().height + getLineWidth());
		return (rectangle.contains(x, y));
	}

	public boolean contains(double x, double y) {
		Rectangle2D rectangle = new Rectangle2D.Double(getBounds2D().getX() - getLineWidth() / 2, getBounds2D().getY() - getLineWidth() / 2, getBounds2D().getWidth() + getLineWidth(), getBounds2D().getHeight() + getLineWidth());
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

	public void setSize(int w, int h) {
		bounds.setRect(bounds.getX(), bounds.getY(), w, h);
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
	
	public void copyProperties(Shape other) {
		setAlpha(other.getAlpha());
		setFillColor(other.getFillColor());
		setLineColor(other.getLineColor());
		setLineStyle(other.getLineStyle());
		setLineWidth(other.getLineWidth());
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
		shapeChanged();
	}

	public int getAlpha() {
		return alpha;
	}
	
	public boolean compareStyles(Shape shape) {
		if (getAlpha() != shape.getAlpha()) return false;
		if (getFillColor() == null) {
			if (shape.getFillColor() != null) return false;
		}
		else if (!getFillColor().equals(shape.getFillColor())) return false;
		if (!getLineColor().equals(shape.getLineColor())) return false;
		if (getLineStyle() != shape.getLineStyle()) return false;
		if (getLineWidth() != shape.getLineWidth()) return false;
		return true;
	}
}