package com.feup.jhotsketch.model;

import java.util.HashSet;
import java.util.Set;

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

	private void createHandles() {
		handles = new HashSet<Handle>();
		handles.add(new Handle(this, 0, 0, Handle.NW));
		handles.add(new Handle(this, bounds.width, 0, Handle.NE));
		handles.add(new Handle(this, 0, bounds.height, Handle.SW));
		handles.add(new Handle(this, bounds.width, bounds.height, Handle.SE));
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

	public void resize(double rx, double ry, Handle handle){
		switch (handle.getId()) {
		case Handle.NW:
			bounds.x += rx;
			bounds.y += ry;
			bounds.width -= rx;
			bounds.height -= ry;
			break;
		case Handle.NE:
			bounds.y += ry;
			bounds.width += rx;
			bounds.height -= ry;
			break;
		case Handle.SW:
			bounds.x += rx;
			bounds.width -= rx;
			bounds.height += ry;
			break;
		case Handle.SE:
			bounds.width += rx;
			bounds.height += ry;
			break;
		default:
			break;
		}
		createHandles();
	}

	public void move(int dx, int dy) {
		bounds.x += dx;
		bounds.y += dy;
	}
}