package com.feup.jhotsketch.grouping;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.jhotsketch.model.FigureModel;

public class GroupModel extends FigureModel{
	private HashSet<FigureModel> figures = new HashSet<FigureModel>();
	private int width, height;
	
	public GroupModel(int x, int y) {
		super(x, y);
	}

	public GroupModel() {
		super(0, 0);
	}

	public void addFigures(Set<FigureModel> figures) {
		this.figures.addAll(figures);
		x = 100000;	y = 100000;
		width = 0; height = 0;
		for (FigureModel figure : figures) {
			x = Math.min(x, figure.getBounds().x);
			y = Math.min(y, figure.getBounds().y);
		}
		for (FigureModel figure : figures) {
			width = Math.max(width, figure.getBounds().x + figure.getBounds().width - x);
			height = Math.max(height, figure.getBounds().y + figure.getBounds().height - y);
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public Set<FigureModel> getFigures() {
		return figures;
	}
	
	@Override
	public void setX(int x) {
		for (FigureModel figure : figures) {
			figure.setX(figure.getX() + x - getX());
		}
		super.setX(x);
	}

	@Override
	public void setY(int y) {
		for (FigureModel figure : figures) {
			figure.setY(figure.getY() + y - getY());
		}
		super.setY(y);
	}
}