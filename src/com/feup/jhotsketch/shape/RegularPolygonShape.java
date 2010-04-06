package com.feup.jhotsketch.shape;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Shape")
public class RegularPolygonShape extends Shape{
	private int sides = 6;
	private double startAngle = Math.PI;
	
	public RegularPolygonShape(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	public RegularPolygonShape(double x, double y, double width, double height, int sides, double startAngle) {
		super(x, y, width, height);
		this.sides = sides;
		this.startAngle = startAngle;
	}

	@Override
	public void paint(GC gc) {
		super.paint(gc);

		Point center = new Point(getBounds().x + getBounds().width / 2, getBounds().y + getBounds().height / 2);

		double angle = startAngle;
		int[] points = new int[sides * 2];
		for (int i = 0; i < sides; i++) {
			Point p = new Point((int)(center.x + Math.cos(angle) * getBounds().width / 2), (int)(center.y + Math.sin(angle) * getBounds().width / 2));
			points[i*2] = p.x;
			points[i*2 + 1] = p.y;
			angle += 2 * Math.PI / sides;
		}
		
		if (getFillColor() != null) gc.fillPolygon(points);
		gc.drawPolygon(points);	
	}

	@Override
	public Shape clone() {
		RegularPolygonShape clone = new RegularPolygonShape(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
		clone.copyProperties(this);
		clone.setSides(sides);
		clone.setStartAngle(startAngle);
		return clone;
	}

	private void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}

	private void setSides(int sides) {
		this.sides = sides;
	}

	@Override
	public boolean contains(int x, int y) {
		Point center = new Point(getBounds().x + getBounds().width / 2, getBounds().y + getBounds().height / 2);

		double angle = startAngle;
		List<Point2D> points = new LinkedList<Point2D>();
		for (int i = 0; i < sides; i++) {
			Point2D p = new Point2D.Double(center.x + Math.cos(angle) * getBounds().width / 2, center.y + Math.sin(angle) * getBounds().width / 2);
			points.add(p);
			angle += 2 * Math.PI / sides;
		}

		GeneralPath path = new GeneralPath();

		path.moveTo(points.get(0).getX(), points.get(0).getY());
		points.remove(0);
		for (Point2D point : points) path.lineTo(point.getX(), point.getY());	
		path.closePath();
		
		return path.contains(x, y);
	}
	
	@Override
	public boolean contains(double x, double y) {
		Point center = new Point(getBounds().x + getBounds().width / 2, getBounds().y + getBounds().height / 2);

		double angle = startAngle;
		List<Point2D> points = new LinkedList<Point2D>();
		for (int i = 0; i < sides; i++) {
			Point2D p = new Point2D.Double(center.x + Math.cos(angle) * getBounds().width / 2, center.y + Math.sin(angle) * getBounds().width / 2);
			points.add(p);
			angle += 2 * Math.PI / sides;
		}

		GeneralPath path = new GeneralPath();

		path.moveTo(points.get(0).getX(), points.get(0).getY());
		points.remove(0);
		for (Point2D point : points) path.lineTo(point.getX(), point.getY());	
		path.closePath();
		
		return path.contains(x, y);
	}
}