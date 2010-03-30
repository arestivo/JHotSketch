package com.feup.jhotsketch.connector;

import java.awt.geom.Point2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.ConnectorModel.END;
import com.feup.jhotsketch.geometry.Vectors;

@PackageName("Connector")
public class ConnectorEndPainter {
	public static void paintConnectorEnd(Point p1, Point p2, END end, int size, GC gc) {
		Point2D versor = Vectors.getVersor(p1, p2);
		Point2D normal = new Point2D.Double(-versor.getY(), versor.getX());
		Point base = new Point((int)(p2.x - versor.getX() * size), (int)(p2.y - versor.getY() * size));
		Point halfbase = new Point((int)(p2.x - versor.getX() * size / 2), (int)(p2.y - versor.getY() * size / 2));

		if (end.equals(ConnectorModel.END.FILLEDCIRCLE)) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.fillOval(halfbase.x - size / 2, halfbase.y - size / 2, size, size);
		}
		if (end.equals(ConnectorModel.END.HOLLOWCIRCLE)) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.fillOval(halfbase.x - size / 2, halfbase.y - size / 2, size, size);
			gc.drawOval(halfbase.x - size / 2, halfbase.y - size / 2, size, size);
		}
		if (end.equals(ConnectorModel.END.FILLEDSQUARE)) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.fillPolygon(
					new int[] {
							(int)(base.x + normal.getX() * size / 2),(int)(base.y + normal.getY() * size / 2), 
							(int)(p2.x + normal.getX() * size / 2),(int)(p2.y + normal.getY() * size / 2), 
							(int)(p2.x - normal.getX() * size / 2),(int)(p2.y - normal.getY() * size / 2), 
							(int)(base.x - normal.getX() * size / 2),(int)(base.y - normal.getY() * size / 2)});
		}
		if (end.equals(ConnectorModel.END.HOLLOWSQUARE)) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(
					new int[] {
							(int)(base.x + normal.getX() * size / 2),(int)(base.y + normal.getY() * size / 2), 
							(int)(p2.x + normal.getX() * size / 2),(int)(p2.y + normal.getY() * size / 2), 
							(int)(p2.x - normal.getX() * size / 2),(int)(p2.y - normal.getY() * size / 2), 
							(int)(base.x - normal.getX() * size / 2),(int)(base.y - normal.getY() * size / 2)});
			gc.drawPolygon(
					new int[] {
							(int)(base.x + normal.getX() * size / 2),(int)(base.y + normal.getY() * size / 2), 
							(int)(p2.x + normal.getX() * size / 2),(int)(p2.y + normal.getY() * size / 2), 
							(int)(p2.x - normal.getX() * size / 2),(int)(p2.y - normal.getY() * size / 2), 
							(int)(base.x - normal.getX() * size / 2),(int)(base.y - normal.getY() * size / 2)});
		}
		if (end.equals(ConnectorModel.END.FILLEDARROW)) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.fillPolygon(new int[] {(int)(base.x + normal.getX() * size / 2),(int)(base.y + normal.getY() * size / 2), 
					p2.x, p2.y, 
					(int)(base.x - normal.getX() * size / 2),(int)(base.y - normal.getY() * size / 2)});
		}
		if (end.equals(ConnectorModel.END.HOLLOWARROW)) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.fillPolygon(new int[] {(int)(base.x + normal.getX() * size / 2),(int)(base.y + normal.getY() * size / 2), 
					p2.x, p2.y, 
					(int)(base.x - normal.getX() * size / 2),(int)(base.y - normal.getY() * size / 2)});
			gc.drawPolygon(new int[] {(int)(base.x + normal.getX() * size / 2),(int)(base.y + normal.getY() * size / 2), 
					p2.x, p2.y, 
					(int)(base.x - normal.getX() * size / 2),(int)(base.y - normal.getY() * size / 2)});
		}
	}

}
