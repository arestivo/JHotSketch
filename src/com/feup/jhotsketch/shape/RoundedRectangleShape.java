package com.feup.jhotsketch.shape;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Shape")
public class RoundedRectangleShape extends Shape{
	private int radiusx = 15;
	private int radiusy = 15;
	
	public RoundedRectangleShape(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public void paint(GC gc) {
		super.paint(gc);
		if (getFillColor() != null) gc.fillRoundRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height, radiusx, radiusy);
		gc.drawRoundRectangle(getBounds().x, getBounds().y, getBounds().width, getBounds().height, radiusx, radiusy);
		drawText(gc);
	}

	@Override
	public Shape clone() {
		RoundedRectangleShape clone = new RoundedRectangleShape(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		clone.setRadiusX(radiusx);
		clone.setRadiusY(radiusy);
		clone.copyProperties(this, true);
		return clone;
	}

	private void setRadiusY(int radiusy) {
		this.radiusy = radiusy;
	}

	private void setRadiusX(int radiusx) {
		this.radiusx = radiusx;
	}
}