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
	private List<FigureModel> figures = new LinkedList<FigureModel>();
	private Set<DiagramObserver> observers = new HashSet<DiagramObserver>();
	private List<FigureModel> selected = new LinkedList<FigureModel>();

	private Rectangle selectionRectangle; 

	public DiagramModel(){
		figures.add(new RectangleModel(10, 10, 40, 40));
		figures.add(new CircleModel(150, 150, 40, 40));
		figures.add(new CircleModel(50, 150, 40, 40));
		figures.add(new CircleModel(150, 50, 40, 40));
		figures.add(new CircleModel(100, 100, 40, 40));
		for (FigureModel figure : figures) {
			figure.addObserver(this);
		}
	}
	
	public Collection<FigureModel> getFigures() {
		return figures;
	}
	
	public void diagramChanged() {
		for (DiagramObserver observer : observers) {
			observer.diagramChanged(this);
		}
	}
	
	public void addFigure(FigureModel f){
		figures.add(f);
		f.addObserver(this);
		diagramChanged();
	}
	
	public void removeFigure(FigureModel f){
		figures.remove(f);
		selected.remove(f);
		diagramChanged();
	}

	public void addObserver(DiagramObserver observer) {
		observers .add(observer);
	}

	public FigureModel getFigureAt(int x, int y) {
		for (FigureModel figure : figures) {
			if (figure.contains(x,y)) return figure;
		}
		return null;
	}

	public List<FigureModel> getSelected() {
		return selected ;
	}

	public void unselectAll() {
		for (FigureModel figure : selected) {
			figure.setSelected(false);
		}
		selected.clear();
		diagramChanged();
	}

	public void setSelect(FigureModel figure) {
		if (figure == null) return;
		selected.add(figure);
		figure.setSelected(true);			
	}

	public void toggleSelected(FigureModel figure) {
		if (figure == null) return;
		if (selected.contains(figure)) {
			selected.remove(figure);
			figure.setSelected(false);
		} else {
			selected.add(figure);
			figure.setSelected(true);			
		}
	}

	public void moveFigure(FigureModel figure, int dx, int dy) {
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

	public void figureChanged(FigureModel figure) {
		diagramChanged();
	}

	public void removeFigures(List<FigureModel> toRemove) {
		figures.removeAll(toRemove);
		selected.removeAll(toRemove);
	}

	public void addFigures(List<FigureModel> figures) {
		this.figures.addAll(figures);
		diagramChanged();
	}

	public void setSelect(List<FigureModel> figures) {
		for (FigureModel figure : figures) {
			setSelect(figure);
		}
	}

	public void resizeFigure(FigureModel figure, double rx, double ry,Handle grabbedHandle) {
		figure.resize(rx, ry, grabbedHandle);
	}

	public void addFiguresAtStart(LinkedList<FigureModel> figures) {
		this.figures.addAll(0, figures);
	}

	public LinkedList<FigureModel> getFiguresAt(int x, int y) {
		LinkedList<FigureModel> found = new LinkedList<FigureModel>(); 
		for (FigureModel figure : figures) {
			if (figure.contains(x,y)) found.add(figure);
		}
		return found;
	}

}