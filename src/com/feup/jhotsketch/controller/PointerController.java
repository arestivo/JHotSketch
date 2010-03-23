package com.feup.jhotsketch.controller;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("Controller")
public class PointerController extends DiagramController{
	private enum OPERATION {NONE, SELECT, MOVE} 
	private OPERATION operation = OPERATION.NONE;
	
	private Set<FigureModel> grabbed;
	
	private Point lastPoint;
	
	public PointerController(DiagramModel diagram) {
		super(diagram);
	}

	@Override
	public void mouseDown(Event event) {
		lastPoint = new Point(event.x, event.y);
		
		// Test if event on selected figure
		for (FigureModel figure : diagram.getSelected()) {
			if (figure.contains(event.x, event.y)) {
				operation = OPERATION.MOVE;
				grabbed = diagram.getSelected();
				return;
			}
		}
		
		// Test if event on unselected figure
		FigureModel figure = diagram.getFigureAt(event.x, event.y);
		if (figure != null) {
			if ((event.stateMask & SWT.CTRL) == 0) diagram.unselectAll();
			diagram.setSelect(figure);
			mouseDown(event);
			return;
		}

		// Event on nothing
		if ((event.stateMask & SWT.SHIFT) == 0) diagram.unselectAll();
		operation = OPERATION.SELECT;
	}

	@Override
	public void mouseMove(Event event) {
		Point newPoint = new Point(event.x, event.y);

		if (operation == OPERATION.MOVE) {
			moveFigures(grabbed, lastPoint, newPoint);
			lastPoint = newPoint;
		}
		if (operation == OPERATION.SELECT) {
			diagram.setSelectionRectangle(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y);
		}
	}

	private void moveFigures(Set<FigureModel> figures, Point lastPoint, Point newPoint) {
		int dx = newPoint.x - lastPoint.x;
		int dy = newPoint.y - lastPoint.y;
		for (FigureModel figure : figures) {
			diagram.moveFigure(figure, dx, dy);
		}
	}

	@Override
	public void mouseUp(Event event) {
		if (operation == OPERATION.MOVE) {
			grabbed = null;
		}

		if (operation == OPERATION.SELECT) {
			Point newPoint = new Point(event.x, event.y);
			diagram.removeSelectionRectangle();
			for (FigureModel figure : diagram.getFigures()) {
				if (figure.inside(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y))
					diagram.setSelect(figure);
			}
		}
		operation = OPERATION.NONE;
	}
}