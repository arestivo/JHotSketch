package com.feup.jhotsketch.connector;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Connector")
public class Connector {
	public enum ENDTYPE  {NONE, FILLEDCIRCLE, HOLLOWCIRCLE, FILLEDSQUARE, SIMPLEARROW, HOLLOWARROW, FILLEDARROW, HOLLOWSQUARE}
	private enum SHAPE  {SOURCE, TARGET}
	
	private Shape source;
	private Shape target;
	
	private Color lineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private int lineWidth = 1;
	private int lineStyle = SWT.LINE_SOLID;

	private int sourceEndSize = 10;
	private int targetEndSize = 10;
	
	private ENDTYPE sourceEndType = ENDTYPE.NONE;
	private ENDTYPE targetEndType = ENDTYPE.NONE;

	private List<Point2D> points = new LinkedList<Point2D>();
	
	private Set<ConnectorObserver> observers = new HashSet<ConnectorObserver>();
	
	public Connector(Shape source, Shape target) {
		this.source = source;
		this.target = target;
		setLineColor(new Color(Display.getCurrent(), new RGB(30, 144, 255)));
		
		if (source == target) {
			points.add(new Point2D.Double(source.getBounds2D().getCenterX() + source.getBounds2D().getWidth() + 30, source.getBounds2D().getCenterY()));
			points.add(new Point2D.Double(source.getBounds2D().getCenterX() + source.getBounds2D().getWidth() + 30, source.getBounds2D().getCenterY() + source.getBounds2D().getHeight() + 30));
			points.add(new Point2D.Double(source.getBounds2D().getCenterX(), source.getBounds2D().getCenterY() + source.getBounds2D().getHeight() + 30));
		}
	}	

	public Shape getSource() {
		return source;
	}

	public Shape getTarget() {
		return target;
	}

	public void paint(GC gc) {
		gc.setLineWidth(lineWidth);
		gc.setLineStyle(lineStyle);
		gc.setForeground(lineColor);
		gc.setBackground(lineColor);
		
		Point2D pt = getIntersectionPoint(source, target, SHAPE.TARGET);
		Point2D ps = getIntersectionPoint(target, source, SHAPE.SOURCE);

		drawLines(gc);		
				
		gc.setLineStyle(SWT.LINE_SOLID);
		
		paintEnd(gc, targetEndSize, getLastPointBefore(SHAPE.TARGET), pt, targetEndType);
		paintEnd(gc, sourceEndSize, getLastPointBefore(SHAPE.SOURCE), ps, sourceEndType);
	}

	private void drawLines(GC gc) {
		List<Line2D> lines = getLines();
		for (Line2D line : lines) {
			gc.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
		}
	}

	private List<Point2D> augmentedPoints() {
		Point2D pt = getIntersectionPoint(source, target, SHAPE.TARGET);
		Point2D ps = getIntersectionPoint(target, source, SHAPE.SOURCE);

		List<Point2D> aPoints = new LinkedList<Point2D>();
		aPoints.add(ps);
		aPoints.addAll(points);
		aPoints.add(pt);
		return aPoints;
	}

	private void paintEnd(GC gc, int endSize, Point2D source, Point2D target, ENDTYPE endType) {
		Point2D versor = getVersor(source, target);
		Point2D normal = new Point2D.Double(versor.getY(), -versor.getX());
		Point2D base = new Point2D.Double(target.getX() - versor.getX() * endSize, target.getY() - versor.getY() * endSize);
		Point2D center = new Point2D.Double(target.getX() - versor.getX() * endSize / 2, target.getY() - versor.getY() * endSize / 2);
		if (endType == ENDTYPE.HOLLOWCIRCLE || endType == ENDTYPE.FILLEDCIRCLE) {
			if (endType == ENDTYPE.HOLLOWCIRCLE) gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.fillOval((int)center.getX() - endSize / 2, (int)center.getY() - endSize / 2, endSize, endSize);
			gc.drawOval((int)center.getX() - endSize / 2, (int)center.getY() - endSize / 2, endSize, endSize);
			gc.setBackground(lineColor);
		}
		if (endType == ENDTYPE.FILLEDSQUARE || endType == ENDTYPE.HOLLOWSQUARE) {
			Point2D p1 = new Point2D.Double(base.getX() + normal.getX() * endSize / 2, base.getY() + normal.getY() * endSize / 2);
			Point2D p2 = new Point2D.Double(target.getX() + normal.getX() * endSize / 2, target.getY() + normal.getY() * endSize / 2);
			Point2D p3 = new Point2D.Double(target.getX() - normal.getX() * endSize / 2, target.getY() - normal.getY() * endSize / 2);
			Point2D p4 = new Point2D.Double(base.getX() - normal.getX() * endSize / 2, base.getY() - normal.getY() * endSize / 2);
			if (endType == ENDTYPE.HOLLOWSQUARE) gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(new int[] {(int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), (int)p3.getX(), (int)p3.getY(), (int)p4.getX(), (int)p4.getY()});
			gc.drawPolygon(new int[] {(int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY(), (int)p3.getX(), (int)p3.getY(), (int)p4.getX(), (int)p4.getY()});
			gc.setBackground(lineColor);
		}
		if (endType == ENDTYPE.SIMPLEARROW) {
			Point2D p1 = new Point2D.Double(base.getX() + normal.getX() * endSize / 2, base.getY() + normal.getY() * endSize / 2);
			Point2D p2 = new Point2D.Double(base.getX() - normal.getX() * endSize / 2, base.getY() - normal.getY() * endSize / 2);
			gc.drawLine((int)p1.getX(), (int)p1.getY(), (int)target.getX(), (int)target.getY());
			gc.drawLine((int)p2.getX(), (int)p2.getY(), (int)target.getX(), (int)target.getY());
		}
		if (endType == ENDTYPE.HOLLOWARROW || endType == ENDTYPE.FILLEDARROW) {
			Point2D p1 = new Point2D.Double(base.getX() + normal.getX() * endSize / 2, base.getY() + normal.getY() * endSize / 2);
			Point2D p2 = new Point2D.Double(base.getX() - normal.getX() * endSize / 2, base.getY() - normal.getY() * endSize / 2);
			if (endType == ENDTYPE.HOLLOWARROW) gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(new int[] {(int)p1.getX(), (int)p1.getY(), (int)target.getX(), (int)target.getY(), (int) p2.getX(), (int) p2.getY()});
			gc.drawPolygon(new int[] {(int)p1.getX(), (int)p1.getY(), (int)target.getX(), (int)target.getY(), (int) p2.getX(), (int) p2.getY()});
			gc.setBackground(lineColor);
		}
	}
	
	private Point2D getVersor(Point2D a, Point2D b) {
		if (a.equals(b)) return new Point2D.Double(1,0);
		Point2D vector = new Point2D.Double(b.getX() - a.getX(), b.getY() - a.getY());
		double dist = vector.distance(0, 0);
		Point2D versor = new Point2D.Double(vector.getX() / dist, vector.getY() / dist);
		return versor;
	}

	private Point2D getIntersectionPoint(Shape shape1, Shape shape2, SHAPE s) {
		Point2D p1 = getLastPointBefore(s);
		Point2D p2 = new Point2D.Double(shape2.getBounds2D().getX() + shape2.getBounds2D().getWidth() / 2, shape2.getBounds2D().getY() + shape2.getBounds2D().getHeight() / 2);
		return getIntersectionPoint(p1, p2, shape2);
	}

	private Point2D getLastPointBefore(SHAPE s) {
		if (s.equals(SHAPE.SOURCE)) {
			if (points.size() != 0) return points.get(0);
			return target.getCenter();
		} else {
			if (points.size() != 0) return points.get(points.size() - 1);
			return source.getCenter();
		}
	}

	private Point2D getIntersectionPoint(Point2D p1, Point2D p2, Shape shape) {
		Point2D middle = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
		if (p1.distance(p2) < 0.1) return middle;
		if (shape.contains(middle.getX(), middle.getY())) return getIntersectionPoint(p1, middle, shape);
		else return getIntersectionPoint(middle, p2, shape);
	}

	public void setLineColor(Color color) {
		this.lineColor = color;
		connectorChanged();
	}
	
	public Color getLineColor() {
		return lineColor;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		connectorChanged();
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
		connectorChanged();
	}

	public int getLineStyle() {
		return lineStyle;
	}

	public boolean contains(int x, int y) {
		List<Line2D> lines = getLines();
		for (Line2D line : lines)
			if (getBounds(line).contains(x, y) && 
			    line.ptLineDist(x, y) < 5) return true;
		return false;
	}

	private List<Line2D> getLines() {
		List<Line2D> lines = new LinkedList<Line2D>();
		ListIterator<Point2D> iterator = augmentedPoints().listIterator();
		while (iterator.hasNext()) {
			Point2D p1 = (Point2D) iterator.next();
			if (iterator.hasNext()) {
				Point2D p2 = (Point2D) iterator.next();
				Line2D line = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
				lines.add(line);
				iterator.previous();
			}
		}
		return lines;
	}

	public Point2D getCenter() {
		Point2D pt = getIntersectionPoint(source, target, SHAPE.TARGET);
		Point2D ps = getIntersectionPoint(target, source, SHAPE.SOURCE);

		Line2D line = new Line2D.Double(pt, ps);
				
		return new Point2D.Double((line.getX1() + line.getX2()) / 2, (line.getY1() + line.getY2()) / 2);
	}

	public void addObserver(ConnectorObserver observer) {
		observers.add(observer);
	}

	private void connectorChanged() {
		for (ConnectorObserver observer : observers) {
			observer.connectorChanged(this);
		}
	}

	public Rectangle getBounds(Line2D line) {
		Rectangle bounds = new Rectangle((int)Math.min(line.getX1(), line.getX2()), (int)Math.min(line.getY1(), (int)line.getY2()), (int)Math.abs(line.getX1() - line.getX2()), (int)Math.abs(line.getY1() - line.getY2()));
		bounds.x -= 3; bounds.width += 6;
		bounds.y -= 3; bounds.height += 6;
		return bounds;
	}

	public void setTargetEndSize(int size) {
		targetEndSize = size;
		connectorChanged();
	}

	public void setSourceEndSize(int size) {
		sourceEndSize = size;
		connectorChanged();
	}

	public int getTargetEndSize() {
		return targetEndSize;
	}

	public int getSourceEndSize() {
		return sourceEndSize;
	}

	public void setTargetEndType(ENDTYPE endType) {
		targetEndType = endType;
		connectorChanged();
	}

	public void setSourceEndType(ENDTYPE endType) {
		sourceEndType = endType;
		connectorChanged();
	}

	public ENDTYPE getTargetEndType() {
		return targetEndType;
	}

	public ENDTYPE getSourceEndType() {
		return sourceEndType;
	}
	
	public Connector clone() {
		Connector clone = new Connector(source, target);
		clone.copyProperties(this);
		return clone;
	}

	public void copyProperties(Connector other) {
		setLineColor(other.getLineColor());
		setLineStyle(other.getLineStyle());
		setLineWidth(other.getLineWidth());
		setSourceEndType(other.getSourceEndType());
		setTargetEndType(other.getTargetEndType());
		setSourceEndSize(other.getSourceEndSize());
		setTargetEndSize(other.getTargetEndSize());
		for (Point2D point : other.getPoints()) addPoint((Point2D) point.clone());
	}

	private void addPoint(Point2D point) {
		points.add(point);
	}

	public void setSource(Shape source) {
		this.source = source;
	}

	public void setTarget(Shape target) {
		this.target = target;
	}

	public List<Point2D> getMiddlePoints() {
		List<Point2D> augmentedPoints = augmentedPoints();
		List<Point2D> mPoints = new LinkedList<Point2D>();
		ListIterator<Point2D> iterator = augmentedPoints .listIterator();
		while (iterator.hasNext()) {
			Point2D p1 = (Point2D) iterator.next();
			if (iterator.hasNext()) {
				Point2D p2 = iterator.next();
				Point2D mp = new Point2D.Double((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
				mPoints.add(mp);
				iterator.previous();
			}
		}	
		return mPoints;
	}

	public List<Point2D> getPoints() {
		return points;
	}

	public void addPoint(Point2D point, int position) {
		points.add(position, point);
	}

	public void removePoint(int position) {
		points.remove(position);
	}

	public Rectangle getBounds() {
		Rectangle bounds = null;
		List<Line2D> lines = getLines();
		for (Line2D line : lines) {
			Rectangle lineBounds = getBounds(line);
			if (bounds == null) bounds = lineBounds;
			else bounds = bounds.union(lineBounds);
		}
		return bounds;
	}

	public void movePoint(int position, int x, int y) {
		points.get(position).setLocation(new Point2D.Double(x, y));
		
	}

	public void setPointX(int position, int x) {
		points.get(position).setLocation(new Point2D.Double(x, points.get(position).getY()));
	}

	public void setPointY(int position, int y) {
		points.get(position).setLocation(new Point2D.Double(points.get(position).getX(), y));
	}

	public boolean compareStyles(Connector other) {
		if (!getLineColor().equals(other.getLineColor())) return false;
		if (getLineStyle() != other.getLineStyle()) return false;
		if (getLineWidth() != other.getLineWidth()) return false;
		if (getSourceEndSize() != other.getSourceEndSize()) return false;
		if (getTargetEndSize() != other.getTargetEndSize()) return false;
		if (getSourceEndType() != other.getSourceEndType()) return false;
		if (getTargetEndType() != other.getTargetEndType()) return false;
		return true;
	}

	public void move(int dx, int dy) {
		for (Point2D point : points) {
			point.setLocation(new Point2D.Double(point.getX() + dx, point.getY() + dy));
		}
	}

	public void removeAllPoints() {
		points.clear();
	}
}