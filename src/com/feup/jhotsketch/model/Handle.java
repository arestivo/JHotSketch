package com.feup.jhotsketch.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class Handle {
	private ShapeModel shape;
	private int x;
	private int y;
	private static int size = 10;

	private String id;
	private String kind;
	private int color = SWT.COLOR_BLACK;
		
	public Handle(ShapeModel shape, int x, int y, String id, String kind, int color) {
		this.shape = shape;
		this.x = x;
		this.y = y;
		this.id = id;
		this.kind = kind;
		this.color = color;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}

	public static int getSize() {
		return size;
	}

	public Rectangle getBounds() {
		return new Rectangle(shape.bounds.x + x - size / 2, shape.bounds.y + y - size / 2, size, size);
	}
	
	public boolean contains(int x, int y) {
		return getBounds().contains(x, y);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getKind() {
		return kind;
	}

	public Color getColor() {
		return Display.getCurrent().getSystemColor(color);
	}
	
}