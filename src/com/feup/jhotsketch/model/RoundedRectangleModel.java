package com.feup.jhotsketch.model;

import org.eclipse.swt.SWT;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class RoundedRectangleModel extends ShapeModel{
	private int radiusx = 15;
	private int radiusy = 15;
	
	public RoundedRectangleModel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public ShapeModel clone() {
		RoundedRectangleModel clone = new RoundedRectangleModel(bounds.x, bounds.y, bounds.width, bounds.height);
		clone.setRadiusX(getRadiusX());
		clone.setRadiusY(getRadiusY());
		return clone;
	}

	public void setRadiusX(int radius) {
		this.radiusx = radius;
	}

	public int getRadiusX() {
		return radiusx;
	}	

	public void setRadiusY(int radius) {
		this.radiusy = radius;
	}

	public int getRadiusY() {
		return radiusy;
	}	

	public void createHandles() {
		super.createHandles();
		handles.add(new Handle(this, radiusx, 0, "ROUNDRADIUSX", "RHOMBUS", SWT.COLOR_DARK_GREEN));
		handles.add(new Handle(this, 0, radiusy, "ROUNDRADIUSY", "RHOMBUS", SWT.COLOR_DARK_GREEN));
	}
	
	public void moveHandle(double rx, double ry, Handle handle){
		super.moveHandle(rx, ry, handle);
		if (handle.getId().equals("ROUNDRADIUSX")) radiusx += rx;
		if (handle.getId().equals("ROUNDRADIUSY")) radiusy += ry;
		if (radiusx < 0) radiusx = 0;
		if (radiusy < 0) radiusy = 0;
		if (radiusx > bounds.width) radiusx = bounds.width;
		if (radiusy > bounds.height) radiusy = bounds.height;
		createHandles();
	}
}