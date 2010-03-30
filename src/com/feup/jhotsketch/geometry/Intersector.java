package com.feup.jhotsketch.geometry;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Geometry")
public class Intersector {
	private static Point2D intersectOval(Point2D p1, Point2D p2, Ellipse2D ellipse) {
		Point2D p3 = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		if (p1.distance(p2) < 1) return p3;
		
		if (ellipse.contains(p3)) return intersectOval(p1, p3, ellipse);
		else return intersectOval(p3, p2, ellipse);
	}

	public static Point intersectOval(Point p1, Point p2, Rectangle oval) {
		Ellipse2D ellipse = new Ellipse2D.Double(oval.x, oval.y,	oval.width, oval.height);

		Point2D point1 = new Point2D.Double(p1.x, p1.y);
		Point2D point2 = new Point2D.Double(p2.x, p2.y);
		
		Point2D result = intersectOval(point1, point2, ellipse);
		return new Point((int)result.getX(), (int)result.getY());
	}

	private static Point2D intersectRectangle(Point2D p1, Point2D p2, Rectangle2D rectangle) {
		Point2D p3 = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		if (p1.distance(p2) < 1) return p3;
		
		if (rectangle.contains(p3)) return intersectRectangle(p1, p3, rectangle);
		else return intersectRectangle(p3, p2, rectangle);
	}

	public static Point intersectRectangle(Point p1, Point p2, Rectangle rect) {
		Rectangle2D rectangle = new Rectangle2D.Double(rect.x, rect.y, rect.width, rect.height);

		Point2D point1 = new Point2D.Double(p1.x, p1.y);
		Point2D point2 = new Point2D.Double(p2.x, p2.y);
		
		Point2D result = intersectRectangle(point1, point2, rectangle);
		return new Point((int)result.getX(), (int)result.getY());
	}
}
