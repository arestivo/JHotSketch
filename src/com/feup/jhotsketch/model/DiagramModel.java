package com.feup.jhotsketch.model;

import java.util.HashSet; 
import java.util.Set;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class DiagramModel implements FigureObserver {
	private Set<FigureModel> figures = new HashSet<FigureModel>();
	private Set<DiagramObserver> observers = new HashSet<DiagramObserver>();
	private Set<FigureModel> selected = new HashSet<FigureModel>();

	private Rectangle selectionRectangle; 
	private Rectangle moveRectangle; 

	public DiagramModel(){
		figures.add(new SquareModel(10, 10, 50, 50));
		figures.add(new CircleModel(150, 150, 50, 50));
		figures.add(new CircleModel(50, 150, 50, 50));
		figures.add(new CircleModel(150, 50, 50, 50));
		figures.add(new CircleModel(100, 100, 50, 50));
		for (FigureModel figure : figures) {
			figure.addObserver(this);
		}
	}
	
	public Set<FigureModel> getFigures() {
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

	public Set<FigureModel> getSelected() {
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
		figure.setX(figure.getX() + dx);
		figure.setY(figure.getY() + dy);
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

	public void setMoveRectangle(Rectangle moveRectangle) {
		this.moveRectangle = moveRectangle;
		diagramChanged();
	}

	public Rectangle getMoveRectangle() {
		return moveRectangle;
	}

	public void removeMoveRectangle() {
		moveRectangle = null;
		diagramChanged();
	}

	@Override
	public void figureChanged(FigureModel figure) {
		diagramChanged();
	}

	public void removeFigures(Set<FigureModel> toRemove) {
		figures.removeAll(toRemove);
	}
}