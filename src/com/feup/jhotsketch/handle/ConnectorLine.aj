package com.feup.jhotsketch.handle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Handle")
public aspect ConnectorLine {
	public Point Shape.connectorLine;
	
	public void Shape.setConnectorLine(Point p){
		connectorLine = p;
	}

	public Point Shape.getConnectorLine(){
		return connectorLine;
	}
	
	pointcut shapePainted(Shape shape, GC gc) :
		call (void Shape+.paint(GC)) && args(gc) && target(shape) && within(Diagram);

	void around(Shape shape, GC gc) : shapePainted(shape, gc) {
		if (shape.getConnectorLine() != null) {
			gc.setLineWidth(1);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setLineStyle(SWT.LINE_DOT);
			gc.drawLine(shape.getBounds().x + shape.getBounds().width / 2, shape.getBounds().y  + shape.getBounds().height / 2, shape.getConnectorLine().x, shape.getConnectorLine().y);
		}
		proceed(shape, gc);
	}
}
