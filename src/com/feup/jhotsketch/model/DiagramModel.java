package com.feup.jhotsketch.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class DiagramModel implements FigureObserver {
	private List<ShapeModel> shapes = new LinkedList<ShapeModel>();
	private Set<DiagramObserver> observers = new HashSet<DiagramObserver>();
	private List<ShapeModel> selected = new LinkedList<ShapeModel>();

	private Rectangle selectionRectangle; 

	public DiagramModel(){
		shapes.add(new RectangleModel(10, 10, 40, 40));
		shapes.add(new OvalModel(150, 150, 40, 40));
		shapes.add(new OvalModel(50, 150, 40, 40));
		shapes.add(new OvalModel(150, 50, 40, 40));
		shapes.add(new OvalModel(100, 100, 40, 40));
		for (ShapeModel shape : shapes) {
			shape.addObserver(this);
			shape.setText("Just Some Text");
		}
	}
	
	public Collection<ShapeModel> getFigures() {
		return shapes;
	}
	
	public void diagramChanged() {
		for (DiagramObserver observer : observers) {
			observer.diagramChanged(this);
		}
	}
	
	public void addFigure(ShapeModel f){
		shapes.add(f);
		f.addObserver(this);
		diagramChanged();
	}
	
	public void removeFigure(ShapeModel f){
		shapes.remove(f);
		selected.remove(f);
		diagramChanged();
	}

	public void addObserver(DiagramObserver observer) {
		observers .add(observer);
	}

	public ShapeModel getFigureAt(int x, int y) {
		for (ShapeModel shape : shapes) {
			if (shape.contains(x,y)) return shape;
		}
		return null;
	}

	public List<ShapeModel> getSelected() {
		return selected ;
	}

	public void unselectAll() {
		for (ShapeModel shape : selected) {
			shape.setSelected(false);
		}
		selected.clear();
		diagramChanged();
	}

	public void setSelect(ShapeModel shape) {
		if (shape == null) return;
		selected.add(shape);
		shape.setSelected(true);			
	}

	public void toggleSelected(ShapeModel shape) {
		if (shape == null) return;
		if (selected.contains(shape)) {
			selected.remove(shape);
			shape.setSelected(false);
		} else {
			selected.add(shape);
			shape.setSelected(true);			
		}
	}

	public void moveFigure(ShapeModel shape, int dx, int dy) {
		shape.move(dx, dy);
	}

	public void setSelectionRectangle(int x1, int y1, int x2, int y2) {
		selectionRectangle = new Rectangle(x1, y1, x2 - x1, y2 - y1);
		diagramChanged();
	}

	public Rectangle getSelectionRectangle() {
		return selectionRectangle;
	}

	public void removeSelectionRectangle() {
		selectionRectangle = null;
		diagramChanged();
	}

	public void shapeChanged(ShapeModel shape) {
		diagramChanged();
	}

	public void removeFigures(List<ShapeModel> toRemove) {
		shapes.removeAll(toRemove);
		selected.removeAll(toRemove);
	}

	public void addFigures(List<ShapeModel> shapes) {
		this.shapes.addAll(shapes);
		diagramChanged();
	}

	public void setSelect(List<ShapeModel> shapes) {
		for (ShapeModel shape : shapes) {
			setSelect(shape);
		}
	}

	public void resizeFigure(ShapeModel shape, double rx, double ry,Handle grabbedHandle) {
		shape.resize(rx, ry, grabbedHandle);
	}

	public void addFiguresAtStart(LinkedList<ShapeModel> shapes) {
		this.shapes.addAll(0, shapes);
	}

	public LinkedList<ShapeModel> getFiguresAt(int x, int y) {
		LinkedList<ShapeModel> found = new LinkedList<ShapeModel>(); 
		for (ShapeModel shape : shapes) {
			if (shape.contains(x,y)) found.add(shape);
		}
		return found;
	}

}