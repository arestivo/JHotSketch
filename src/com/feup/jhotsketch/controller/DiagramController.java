package com.feup.jhotsketch.controller;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.model.Handle;

@PackageName("Controller")
public class DiagramController{
	private enum OPERATION {NONE, SELECT, MOVE, RESIZE} 
	private OPERATION operation = OPERATION.NONE;
	
	private Set<FigureModel> grabbed;
	private Handle grabbedHandle;
	
	private Point lastPoint;

	private DiagramModel diagram;
	
	public DiagramController(DiagramModel diagram) {
		this.setDiagram(diagram);
	}

	public void mouseDown(Event event) {
		lastPoint = new Point(event.x, event.y);
		
		// Test if event on selected figure handle
		for (FigureModel figure : getDiagram().getSelected()) {
			for (Handle handle : figure.getHandles()) {
				if (handle.contains(event.x, event.y)) {
					operation = OPERATION.RESIZE;
					grabbed = getDiagram().getSelected();
					grabbedHandle = handle;
					return;
				}
			}
		}

		// Test if event on selected figure
		for (FigureModel figure : getDiagram().getSelected()) {
			if (figure.contains(event.x, event.y)) {
				operation = OPERATION.MOVE;
				grabbed = getDiagram().getSelected();
				return;
			}
		}
		
		// Test if event on unselected figure
		FigureModel figure = getDiagram().getFigureAt(event.x, event.y);
		if (figure != null) {
			if ((event.stateMask & SWT.CTRL) == 0) getDiagram().unselectAll();
			getDiagram().setSelect(figure);
			mouseDown(event);
			return;
		}

		// Event on nothing
		if ((event.stateMask & SWT.SHIFT) == 0) getDiagram().unselectAll();
		operation = OPERATION.SELECT;
	}

	public void mouseMove(Event event) {
		Point newPoint = new Point(event.x, event.y);

		if (operation == OPERATION.RESIZE) {
			resizeFigures(grabbed, lastPoint, newPoint);
			lastPoint = newPoint;
		}
		if (operation == OPERATION.MOVE) {
			moveFigures(grabbed, lastPoint, newPoint);
			lastPoint = newPoint;
		}
		if (operation == OPERATION.SELECT) {
			getDiagram().setSelectionRectangle(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y);
		}
	}

	private void resizeFigures(Set<FigureModel> figures, Point lastPoint, Point newPoint) {
		int dx = newPoint.x - lastPoint.x;
		int dy = newPoint.y - lastPoint.y;
		for (FigureModel figure : figures) {
			getDiagram().resizeFigure(figure, dx, dy, grabbedHandle);
		}
		getDiagram().diagramChanged();
	}

	private void moveFigures(Set<FigureModel> figures, Point lastPoint, Point newPoint) {
		int dx = newPoint.x - lastPoint.x;
		int dy = newPoint.y - lastPoint.y;
		for (FigureModel figure : figures) {
			getDiagram().moveFigure(figure, dx, dy);
		}
		getDiagram().diagramChanged();
	}

	public void mouseUp(Event event) {
		if (operation == OPERATION.MOVE) {
			grabbed = null;
		}

		if (operation == OPERATION.SELECT) {
			Point newPoint = new Point(event.x, event.y);
			getDiagram().removeSelectionRectangle();
			for (FigureModel figure : getDiagram().getFigures()) {
				if (figure.inside(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y))
					getDiagram().setSelect(figure);
			}
		}
		operation = OPERATION.NONE;
	}

	public void setDiagram(DiagramModel diagram) {
		this.diagram = diagram;
	}

	public DiagramModel getDiagram() {
		return diagram;
	}
}