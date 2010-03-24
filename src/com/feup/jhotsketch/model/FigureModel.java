package com.feup.jhotsketch.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public abstract class FigureModel {
	private boolean selected;
	private HashSet<FigureObserver> observers = new HashSet<FigureObserver>();
	HashSet<Handle> handles = null;
	
	protected int x;
	protected int y;

	public FigureModel(int x, int y){
		this.x = x;
		this.y = y;
		this.selected = false;
	}

	private void createHandles() {
		handles = new HashSet<Handle>();
		handles.add(new Handle(this, 0, 0));
		handles.add(new Handle(this, getBounds().width, 0));
		handles.add(new Handle(this, 0, getBounds().height));
		handles.add(new Handle(this, getBounds().width, getBounds().height));
	}

	public abstract Rectangle getBounds();

	public boolean contains(int x, int y) {
		return getBounds().contains(new Point(x, y));
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		figureChanged();
	}

	public boolean getSelected() {
		return selected;
	}
	
	public boolean inside(int x1, int y1, int x2, int y2) {
		Rectangle rectangle = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
		return getBounds().intersection(rectangle).equals(getBounds());
	}
	
	public void addObserver(FigureObserver observer) {
		observers.add(observer);
	}
	
	public void figureChanged() {
		for (FigureObserver observer : observers) {
			observer.figureChanged(this);
		}
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public Set<Handle> getHandles() {
		if (handles == null) createHandles();
		return handles;
	}

	@Override
	public abstract FigureModel clone();
}