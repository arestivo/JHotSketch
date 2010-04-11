package com.feup.jhotsketch.handle;

import java.awt.geom.Point2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;

@PackageName("Handle")
public class ConnectorMiddleHandle extends Handle{
	private Connector connector;
	private Point2D point;
	private int position;
	
	public ConnectorMiddleHandle(Connector connector, Point2D point, int position) {
		this.connector = connector;
		this.point = point;
		this.position = position;
	}

	@Override
	public void paint(GC gc) {
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(getBounds());
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(((int)point.getX() - SIZE / 2), (int)(point.getY() - SIZE / 2), SIZE, SIZE);
	}
	
	public Connector getConnector() {
		return connector;
	}

	public Point2D getPoint() {
		return point;
	}

	public int getPosition() {
		return position;
	}
}