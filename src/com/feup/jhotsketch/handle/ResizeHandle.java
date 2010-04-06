package com.feup.jhotsketch.handle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Handle")
public class ResizeHandle extends Handle{

	private Shape shape;
	private String id;
	
	public ResizeHandle(Shape shape, String id) {
		this.shape = shape;
		this.id = id;
	}

	@Override
	public void paint(GC gc) {
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(getBounds());
	}

	@Override
	public Rectangle getBounds() {
		if (id.equals("NW")) return new Rectangle(shape.getBounds().x, shape.getBounds().y, SIZE, SIZE);
		if (id.equals("SW")) return new Rectangle(shape.getBounds().x, shape.getBounds().y + shape.getBounds().height - SIZE, SIZE, SIZE);
		if (id.equals("NE")) return new Rectangle(shape.getBounds().x + shape.getBounds().width - SIZE, shape.getBounds().y, SIZE, SIZE);
		if (id.equals("SE")) return new Rectangle(shape.getBounds().x + shape.getBounds().width - SIZE, shape.getBounds().y + shape.getBounds().height - SIZE, SIZE, SIZE);
		return null;
	}

	public Shape getShape() {
		return shape;
	}

	public String getId() {
		return id;
	}
}
