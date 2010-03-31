package com.feup.jhotsketch.connector;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.ConnectorModel.END;
import com.feup.jhotsketch.geometry.Intersector;
import com.feup.jhotsketch.geometry.Vectors;
import com.feup.jhotsketch.model.Handle;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Connector")
public class ConnectorView {
	public static void drawConnector(GC gc, ConnectorModel connector) {
		ShapeModel source = connector.getSource();
		ShapeModel sink = connector.getSink();

		Point p1 = new Point(source.getBounds().x + source.getBounds().width / 2, source.getBounds().y + source.getBounds().height / 2);
		Point p2 = new Point(sink.getBounds().x + sink.getBounds().width / 2, sink.getBounds().y + sink.getBounds().height / 2);

		if (sink instanceof OvalModel) p2 = Intersector.intersectOval(p1, p2, sink.getBounds());
		else p2 = Intersector.intersectRectangle(p1, p2, sink.getBounds());
		if (source instanceof OvalModel) p1 = Intersector.intersectOval(p2, p1, source.getBounds());
		else p1 = Intersector.intersectRectangle(p2, p1, source.getBounds());
		gc.drawLine(p1.x, p1.y, p2.x, p2.y);
		
		paintConnectorEnd(p1, p2, connector.getSinkEnd(), connector.getSinkEndSize(), gc);
		paintConnectorEnd(p2, p1, connector.getSourceEnd(), connector.getSourceEndSize(), gc);
		
		connector.setBounds(new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y)));
		
		if (connector.isSelected()) {
			gc.setBackground(Display.getCurrent().getSystemColor((SWT.COLOR_DARK_GRAY)));
			gc.fillRectangle((p1.x + p2.x) / 2 - Handle.getSize() / 2, (p1.y + p2.y) / 2 - Handle.getSize() / 2, Handle.getSize(), Handle.getSize());
		}
	}

	public static void paintConnectorEnd(Point p1, Point p2, END end, int size, GC gc) {
		gc.setLineStyle(SWT.LINE_SOLID);
		Color originalBackColor = gc.getBackground();
		Point2D versor = Vectors.getVersor(p1, p2);
		Point2D normal = new Point2D.Double(-versor.getY(), versor.getX());
		Point base = new Point((int)(p2.x - versor.getX() * size), (int)(p2.y - versor.getY() * size));
		Point halfbase = new Point((int)(p2.x - versor.getX() * size / 2), (int)(p2.y - versor.getY() * size / 2));

		if (end.equals(ConnectorModel.END.FILLEDCIRCLE)) {
			gc.fillOval(halfbase.x - size / 2, halfbase.y - size / 2, size, size);
		}
		
		if (end.equals(ConnectorModel.END.HOLLOWCIRCLE)) {
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			gc.fillOval(halfbase.x - size / 2, halfbase.y - size / 2, size, size);
			gc.drawOval(halfbase.x - size / 2, halfbase.y - size / 2, size, size);
		}
		
		if (end.equals(ConnectorModel.END.FILLEDSQUARE)) {
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

		if (end.equals(ConnectorModel.END.SIMPLEARROW)) {
			gc.drawLine(p2.x, p2.y, (int)(base.x + normal.getX() * size / 2),(int)(base.y + normal.getY() * size / 2));
			gc.drawLine(p2.x, p2.y, (int)(base.x - normal.getX() * size / 2),(int)(base.y - normal.getY() * size / 2));
		}
		
		gc.setBackground(originalBackColor);
}
	
}