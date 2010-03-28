package com.feup.jhotsketch.grouping;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("Grouping")
public class GroupModel extends FigureModel{
	private List<FigureModel> figures = new LinkedList<FigureModel>();
	
	public GroupModel() {
		super(0, 0, 0, 0);
	}

	public void addFigures(List<FigureModel> figures) {
		this.figures.addAll(figures);
		int x = 100000;	int y = 100000;
		int width = 0; int height = 0;
		for (FigureModel figure : figures) {
			x = Math.min(x, figure.getBounds().x);
			y = Math.min(y, figure.getBounds().y);
		}
		for (FigureModel figure : figures) {
			width = Math.max(width, figure.getBounds().x + figure.getBounds().width - x);
			height = Math.max(height, figure.getBounds().y + figure.getBounds().height - y);
		}
		bounds = new Rectangle(x, y, width, height);
	}
	
	public List<FigureModel> getFigures() {
		return figures;
	}

	@Override
	public FigureModel clone() {
		GroupModel clone = new GroupModel();
		List<FigureModel> clones = new LinkedList<FigureModel>();
		for (FigureModel figure : figures) {
			clones.add(figure.clone());
		}
		clone.addFigures(clones);
		return clone;
	}
	
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		for (FigureModel figure : figures) {
			figure.move(dx, dy);
		}		
	}
}