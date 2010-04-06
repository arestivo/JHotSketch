package com.feup.jhotsketch.connector;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
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
	
	private Shape source;
	private Shape target;
	
	private Color lineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private int lineWidth = 1;
	private int lineStyle = SWT.LINE_SOLID;

	private int sourceEndSize = 10;
	private int targetEndSize = 10;
	
	private ENDTYPE sourceEndType = ENDTYPE.NONE;
	private ENDTYPE targetEndType = ENDTYPE.NONE;

	private Set<ConnectorObserver> observers = new HashSet<ConnectorObserver>();
	
	public Connector(Shape source, Shape target) {
		this.source = source;
		this.target = target;
		setLineColor(new Color(Display.getCurrent(), new RGB(30, 144, 255)));
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
		
		Point2D pt = getIntersectionPoint(source, target);
		Point2D ps = getIntersectionPoint(target, source);
		
		gc.drawLine((int)ps.getX(), (int)ps.getY(), (int)pt.getX(), (int)pt.getY());
		
		gc.setLineStyle(SWT.LINE_SOLID);
		
		paintEnd(gc, targetEndSize, ps, pt, targetEndType);
		paintEnd(gc, sourceEndSize, pt, ps, sourceEndType);
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

	private Point2D getIntersectionPoint(Shape shape1, Shape shape2) {
		Point2D p1 = new Point2D.Double(shape1.getBounds().x + shape1.getBounds().width / 2, shape1.getBounds().y + shape1.getBounds().height / 2);
		Point2D p2 = new Point2D.Double(shape2.getBounds().x + shape2.getBounds().width / 2, shape2.getBounds().y + shape2.getBounds().height / 2);
		return getIntersectionPoint(p1, p2, shape2);
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
		Point2D pt = getIntersectionPoint(source, target);
		Point2D ps = getIntersectionPoint(target, source);

		Line2D line = new Line2D.Double(pt, ps);
				
		if (!getBounds().contains(x, y)) return false;
		if (line.ptLineDist(x, y) > 5) return false;
		return true;
	}

	public Point2D getCenter() {
		Point2D pt = getIntersectionPoint(source, target);
		Point2D ps = getIntersectionPoint(target, source);

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

	public Rectangle getBounds() {
		Point2D pt = getIntersectionPoint(source, target);
		Point2D ps = getIntersectionPoint(target, source);
		
		Rectangle bounds = new Rectangle((int)Math.min(pt.getX(), ps.getX()), (int)Math.min(pt.getY(), (int)ps.getY()), (int)Math.abs(pt.getX() - ps.getX()), (int)Math.abs(pt.getY() - ps.getY()));
		if (bounds.width < 5) { bounds.x -= 3; bounds.width += 6;}
		if (bounds.height < 5) { bounds.y -= 3; bounds.height += 6;}
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
}