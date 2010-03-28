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
	private List<ShapeModel> figures = new LinkedList<ShapeModel>();
	private Set<DiagramObserver> observers = new HashSet<DiagramObserver>();
	private List<ShapeModel> selected = new LinkedList<ShapeModel>();

	private Rectangle selectionRectangle; 

	public DiagramModel(){
		figures.add(new RectangleModel(10, 10, 40, 40));
		figures.add(new OvalModel(150, 150, 40, 40));
		figures.add(new OvalModel(50, 150, 40, 40));
		figures.add(new OvalModel(150, 50, 40, 40));
		figures.add(new OvalModel(100, 100, 40, 40));
		for (ShapeModel figure : figures) {
			figure.addObserver(this);
		}
	}
	
	public Collection<ShapeModel> getFigures() {
		return figures;
	}
	
	public void diagramChanged() {
		for (DiagramObserver observer : observers) {
			observer.diagramChanged(this);
		}
	}
	
	public void addFigure(ShapeModel f){
		figures.add(f);
		f.addObserver(this);
		diagramChanged();
	}
	
	public void removeFigure(ShapeModel f){
		figures.remove(f);
		selected.remove(f);
		diagramChanged();
	}

	public void addObserver(DiagramObserver observer) {
		observers .add(observer);
	}

	public ShapeModel getFigureAt(int x, int y) {
		for (ShapeModel figure : figures) {
			if (figure.contains(x,y)) return figure;
		}
		return null;
	}

	public List<ShapeModel> getSelected() {
		return selected ;
	}

	public void unselectAll() {
		for (ShapeModel figure : selected) {
			figure.setSelected(false);
		}
		selected.clear();
		diagramChanged();
	}

	public void setSelect(ShapeModel figure) {
		if (figure == null) return;
		selected.add(figure);
		figure.setSelected(true);			
	}

	public void toggleSelected(ShapeModel figure) {
		if (figure == null) return;
		if (selected.contains(figure)) {
			selected.remove(figure);
			figure.setSelected(false);
		} else {
			selected.add(figure);
			figure.setSelected(true);			
		}
	}

	public void moveFigure(ShapeModel figure, int dx, int dy) {
		figure.move(dx, dy);
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

	public void figureChanged(ShapeModel figure) {
		diagramChanged();
	}

	public void removeFigures(List<ShapeModel> toRemove) {
		figures.removeAll(toRemove);
		selected.removeAll(toRemove);
	}

	public void addFigures(List<ShapeModel> figures) {
		this.figures.addAll(figures);
		diagramChanged();
	}

	public void setSelect(List<ShapeModel> figures) {
		for (ShapeModel figure : figures) {
			setSelect(figure);
		}
	}

	public void resizeFigure(ShapeModel figure, double rx, double ry,Handle grabbedHandle) {
		figure.resize(rx, ry, grabbedHandle);
	}

	public void addFiguresAtStart(LinkedList<ShapeModel> figures) {
		this.figures.addAll(0, figures);
	}

	public LinkedList<ShapeModel> getFiguresAt(int x, int y) {
		LinkedList<ShapeModel> found = new LinkedList<ShapeModel>(); 
		for (ShapeModel figure : figures) {
			if (figure.contains(x,y)) found.add(figure);
		}
		return found;
	}

}