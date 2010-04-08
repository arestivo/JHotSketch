package com.feup.jhotsketch.style;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;

import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.connector.Connector.ENDTYPE;
import com.feup.jhotsketch.shape.Shape;

public class StyleControl extends Composite{

	private Shape shape1;
	private Shape shape2;
	private Connector connector;

	public StyleControl(Composite parent, int style) {
		super(parent, style);
		RowData rd = new RowData(60, 40);
		setLayoutData(rd);
		
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				paint(e.gc);
			}

		});
	}

	private void paint(GC gc) {
		shape1.paint(gc);
		shape2.paint(gc);
		connector.paint(gc);
	}

	public void setShape(Shape shape) {
		shape1 = shape;
		shape2 = shape.clone();
		shape2.move(38, 0);		
		connector = new Connector(shape1, shape2);
		connector.setTargetEndType(ENDTYPE.FILLEDCIRCLE);
	}
}
