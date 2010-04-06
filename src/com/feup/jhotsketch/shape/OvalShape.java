package com.feup.jhotsketch.shape;

import java.awt.geom.Ellipse2D;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Shape")
public class OvalShape extends Shape{

	public OvalShape(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public void paint(GC gc) {
		super.paint(gc);
		if (getFillColor() != null) gc.fillOval(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		gc.drawOval(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		drawText(gc);
	}

	@Override
	public boolean contains(int x, int y) {
		Ellipse2D ellipse = new Ellipse2D.Double(getBounds().x - getLineWidth() / 2, getBounds().y - getLineWidth() / 2, getBounds().width + getLineWidth(), getBounds().height + getLineWidth());
		return (ellipse.contains(x, y));
	}

	@Override
	public boolean contains(double x, double y) {
		Ellipse2D ellipse = new Ellipse2D.Double(bounds.getX() - getLineWidth() / 2, bounds.getY() - getLineWidth() / 2, bounds.getWidth() + getLineWidth(), bounds.getHeight() + getLineWidth());
		return (ellipse.contains(x, y));
	}
	
	@Override
	public Shape clone() {
		OvalShape clone = new OvalShape(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		clone.copyProperties(this);
		return clone;
	}

}