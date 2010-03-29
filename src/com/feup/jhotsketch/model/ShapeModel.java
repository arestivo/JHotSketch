package com.feup.jhotsketch.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public abstract class ShapeModel {
	private boolean selected = false;
	private HashSet<FigureObserver> observers = new HashSet<FigureObserver>();
	HashSet<Handle> handles = null;

	protected Rectangle bounds;

	public ShapeModel(int x, int y, int width, int height){
		bounds = new Rectangle(x, y, width, height);
	}

	public void createHandles() {
		handles = new HashSet<Handle>();
		handles.add(new Handle(this, 0, 0, "NW", "SQUARE", SWT.COLOR_DARK_GRAY));
		handles.add(new Handle(this, bounds.width, 0, "NE", "SQUARE", SWT.COLOR_DARK_GRAY));
		handles.add(new Handle(this, 0, bounds.height, "SW", "SQUARE", SWT.COLOR_DARK_GRAY));
		handles.add(new Handle(this, bounds.width, bounds.height, "SE", "SQUARE", SWT.COLOR_DARK_GRAY));
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public boolean contains(int x, int y) {
		return bounds.contains(new Point(x, y));
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		shapeChanged();
	}

	public boolean getSelected() {
		return selected;
	}
	
	public boolean inside(int x1, int y1, int x2, int y2) {
		Rectangle rectangle = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
		return bounds.intersection(rectangle).equals(bounds);
	}
	
	public void addObserver(FigureObserver observer) {
		observers.add(observer);
	}
	
	public void shapeChanged() {
		for (FigureObserver observer : observers) {
			observer.shapeChanged(this);
		}
	}

	public Set<Handle> getHandles() {
		if (handles == null) createHandles();
		return handles;
	}

	@Override
	public abstract ShapeModel clone();

	public void moveHandle(double rx, double ry, Handle handle){
		if (handle.getId().equals("NW")) {
			bounds.x += rx;
			bounds.y += ry;
			bounds.width -= rx;
			bounds.height -= ry;
		}
		if (handle.getId().equals("NE")) {
			bounds.y += ry;
			bounds.width += rx;
			bounds.height -= ry;
		}
		if (handle.getId().equals("SW")) {
			bounds.x += rx;
			bounds.width -= rx;
			bounds.height += ry;
		}
		if (handle.getId().equals("SE")) {
			bounds.width += rx;
			bounds.height += ry;
		}
		createHandles();
	}

	public void move(int dx, int dy) {
		bounds.x += dx;
		bounds.y += dy;
	}

	public void addHandle(Handle handle) {
		handles.add(handle);
	}

	public void dropHandle(int dx, int dy, Handle grabbedHandle) {
	}
}