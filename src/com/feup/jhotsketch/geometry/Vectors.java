package com.feup.jhotsketch.geometry;

import java.awt.geom.Point2D;

import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Geometry")
public class Vectors {
	public static Point2D getVersor(Point a, Point b) {
		if (a.equals(b)) return new Point2D.Double(1,0);
		Point vector = new Point (b.x - a.x, b.y - a.y);
		double dist = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
		Point2D versor = new Point2D.Double(vector.x / dist, vector.y / dist);
		return versor;
	}
}
