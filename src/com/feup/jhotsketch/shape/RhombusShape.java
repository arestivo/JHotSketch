package com.feup.jhotsketch.shape;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Shape")
public class RhombusShape extends Shape{
	public RhombusShape(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public void paint(GC gc) {
		super.paint(gc);
		
		Point p1 = new Point(getBounds().x + getBounds().width / 2, getBounds().y);
		Point p2 = new Point(getBounds().x, getBounds().y + getBounds().height / 2);
		Point p3 = new Point(getBounds().x + getBounds().width / 2, getBounds().y + getBounds().height);
		Point p4 = new Point(getBounds().x + getBounds().width, getBounds().y + getBounds().height / 2);

		if (getFillColor() != null) gc.fillPolygon(new int[] {p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y});
		gc.drawPolygon(new int[] {p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y});	
		drawText(gc);
	}

	@Override
	public Shape clone() {
		RhombusShape clone = new RhombusShape(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		clone.copyProperties(this, true);
		return clone;
	}

	@Override
	public boolean contains(int x, int y) {
		GeneralPath path = new GeneralPath();
		Point2D p1 = new Point2D.Double(getBounds().x + getBounds().width / 2, getBounds().y);
		Point2D p2 = new Point2D.Double(getBounds().x, getBounds().y + getBounds().height / 2);
		Point2D p3 = new Point2D.Double(getBounds().x + getBounds().width / 2, getBounds().y + getBounds().height);
		Point2D p4 = new Point2D.Double(getBounds().x + getBounds().width, getBounds().y + getBounds().height / 2);

		path.moveTo(p1.getX(), p1.getY());
		path.lineTo(p2.getX(), p2.getY());
		path.lineTo(p3.getX(), p3.getY());
		path.lineTo(p4.getX(), p4.getY());
		path.closePath();
		
		return path.contains(x, y);
	}
	
	@Override
	public boolean contains(double x, double y) {
		GeneralPath path = new GeneralPath();
		Point2D p1 = new Point2D.Double(bounds.getX() + bounds.getWidth() / 2, bounds.getY());
		Point2D p2 = new Point2D.Double(bounds.getX(), bounds.getY() + bounds.getHeight() / 2);
		Point2D p3 = new Point2D.Double(bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight());
		Point2D p4 = new Point2D.Double(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight() / 2);

		path.moveTo(p1.getX(), p1.getY());
		path.lineTo(p2.getX(), p2.getY());
		path.lineTo(p3.getX(), p3.getY());
		path.lineTo(p4.getX(), p4.getY());
		path.closePath();
		
		return path.contains(x, y);
	}
}