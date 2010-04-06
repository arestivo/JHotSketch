package com.feup.jhotsketch.handle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Handle")
public class CenterHandle extends Handle{

	private Shape shape;
	
	public CenterHandle(Shape shape) {
		this.shape = shape;
	}

	@Override
	public void paint(GC gc) {
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_CYAN));
		gc.fillOval(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)(shape.getBounds().x + shape.getBounds().width / 2 - SIZE / 2), (int)(shape.getBounds().y + shape.getBounds().height / 2 - SIZE / 2), SIZE, SIZE);
	}

	public void setConnectorLine(int x, int y) {
		shape.setConnectorLine(new Point(x, y));
	}

	public void resetConnectorLine() {
		shape.setConnectorLine(null);
	}

	public Shape getShape() {
		return shape;
	}
}
