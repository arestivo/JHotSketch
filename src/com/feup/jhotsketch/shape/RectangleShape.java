package com.feup.jhotsketch.shape;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Shape")
public class RectangleShape extends Shape{
	public RectangleShape(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public void paint(GC gc) {
		super.paint(gc);
		if (getFillColor() != null) gc.fillRectangle(getBounds());
		gc.drawRectangle(getBounds());
		drawText(gc);
	}

	@Override
	public Shape clone() {
		RectangleShape clone = new RectangleShape(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		clone.copyProperties(this);
		return clone;
	}
}