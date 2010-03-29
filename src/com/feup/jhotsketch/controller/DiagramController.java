package com.feup.jhotsketch.controller;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.model.Handle;

@PackageName("Controller")
public class DiagramController{
	public enum OPERATION {NONE, SELECT, MOVE, HANDLE} 
	private OPERATION operation = OPERATION.NONE;
	
	private List<ShapeModel> grabbed;
	private Handle grabbedHandle;
	
	private Point lastPoint;

	private DiagramModel diagram;

	private boolean moved = false;

	private boolean reselect;
	
	public DiagramController(DiagramModel diagram) {
		this.setDiagram(diagram);
	}

	public void mouseDown(Event event) {
		lastPoint = new Point(event.x, event.y);
		moved  = false;
		reselect = false;
		
		// Test if event on selected shape handle
		for (ShapeModel shape : getDiagram().getSelected()) {
			for (Handle handle : shape.getHandles()) {
				if (handle.contains(event.x, event.y)) {
					operation = OPERATION.HANDLE;
					grabbed = getDiagram().getSelected();
					grabbedHandle = handle;
					return;
				}
			}
		}

		// Test if event on selected shape
		for (ShapeModel shape : getDiagram().getSelected()) {
			if (shape.contains(event.x, event.y)) {
				operation = OPERATION.MOVE;
				grabbed = getDiagram().getSelected();
				reselect  = true;
				return;
			}
		}
		
		// Test if event on unselected shape
		ShapeModel shape = getDiagram().getFigureAt(event.x, event.y);
		if (shape != null) {
			if ((event.stateMask & SWT.CTRL) == 0) getDiagram().unselectAll();
			getDiagram().setSelect(shape);
			mouseDown(event);
			return;
		}

		// Event on nothing
		if ((event.stateMask & SWT.SHIFT) == 0) getDiagram().unselectAll();
		operation = OPERATION.SELECT;
	}

	public void mouseMove(Event event) {
		moved = true;
		Point newPoint = new Point(event.x, event.y);

		if (operation == OPERATION.HANDLE) {
			moveHandles(grabbed, lastPoint, newPoint);
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

	private void moveHandles(List<ShapeModel> shapes, Point lastPoint, Point newPoint) {
		int dx = newPoint.x - lastPoint.x;
		int dy = newPoint.y - lastPoint.y;
		for (ShapeModel shape : shapes) {
			getDiagram().moveHandle(shape, dx, dy, grabbedHandle);
		}
		getDiagram().diagramChanged();
	}

	private void moveFigures(List<ShapeModel> shapes, Point lastPoint, Point newPoint) {
		int dx = newPoint.x - lastPoint.x;
		int dy = newPoint.y - lastPoint.y;
		for (ShapeModel shape : shapes) {
			getDiagram().moveFigure(shape, dx, dy);
		}
		getDiagram().diagramChanged();
	}

	public void mouseUp(Event event) {
		if (operation == OPERATION.MOVE) {
			grabbed = null;
			if (!moved && reselect && diagram.getSelected().size() == 1) {
				LinkedList<ShapeModel> shapes = getDiagram().getFiguresAt(event.x, event.y);
				ShapeModel selected = diagram.getSelected().get(0);
				int index = shapes.indexOf(selected);
				if (index == shapes.size() - 1) index = 0; else index++;
				diagram.unselectAll();
				diagram.setSelect(shapes.get(index));
			}
		}

		if (operation == OPERATION.SELECT) {
			Point newPoint = new Point(event.x, event.y);
			getDiagram().removeSelectionRectangle();
			for (ShapeModel shape : getDiagram().getFigures()) {
				if (shape.inside(lastPoint.x, lastPoint.y, newPoint.x, newPoint.y))
					getDiagram().setSelect(shape);
			}
		}
		
		if (operation == OPERATION.HANDLE) {
			Point newPoint = new Point(event.x, event.y);
			dropHandles(grabbed, lastPoint, newPoint);			
		}
		operation = OPERATION.NONE;
	}

	private void dropHandles(List<ShapeModel> shapes, Point lastPoint, Point newPoint) {
		int dx = newPoint.x - lastPoint.x;
		int dy = newPoint.y - lastPoint.y;
		for (ShapeModel shape : shapes) {
			getDiagram().dropHandle(shape, dx, dy, grabbedHandle);
		}
		getDiagram().diagramChanged();
	}

	public void setDiagram(DiagramModel diagram) {
		this.diagram = diagram;
	}

	public DiagramModel getDiagram() {
		return diagram;
	}

	public OPERATION getOperation() {
		return operation;
	}

	public void mouseDoubleClick(Event event) {
	}
}