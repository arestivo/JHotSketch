package com.feup.jhotsketch.controller;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Controller")
public class SelectionController implements ShapeController{
	private Rectangle selectionRectangle;
	private Point initialPoint;

	@Override
	public void mouseDown(int x, int y) {
		selectionRectangle  = new Rectangle(x, y, 0, 0);
		initialPoint = new Point(x, y);
	}

	@Override
	public void mouseMove(int x, int y) {
		selectionRectangle.width = Math.abs(x - initialPoint.x);
		selectionRectangle.height = Math.abs(y - initialPoint.y);
		selectionRectangle.x = Math.min(selectionRectangle.x, x);
		selectionRectangle.y = Math.min(selectionRectangle.y, y);
	}

	public Rectangle getSelectionRectangle() {
		return selectionRectangle;
	}

	@Override
	public void paint(GC gc) {
		if (selectionRectangle != null) {
			gc.setLineWidth(1);
			gc.setLineStyle(SWT.LINE_DOT);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
			gc.drawRectangle(selectionRectangle);
		}
	}

	@Override
	public void mouseUp(int x, int y){		
	}
}
