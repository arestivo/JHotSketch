package com.feup.jhotsketch.controller;

import java.util.Set;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Controller")
public class MoveController implements ShapeController{

	private final Set<Shape> shapes;
	private Point lastPoint;
	private boolean moved = false;
	
	public MoveController(Set<Shape> selected) {
		this.shapes = selected;
	}

	@Override
	public void mouseDown(int x, int y) {
		lastPoint = new Point(x, y);
	}
	
	@Override
	public void mouseMove(int x, int y) {
		for (Shape shape : shapes) {
			shape.move(x - lastPoint.x, y - lastPoint.y);
		}
		lastPoint = new Point(x, y);
		moved = true;
	}
	
	public boolean moved() {
		return moved;
	}

	@Override
	public void mouseUp(int x, int y) {
	}

	@Override
	public void paint(GC gc) {		
	}

}
