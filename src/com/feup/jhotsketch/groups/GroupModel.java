package com.feup.jhotsketch.groups;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Groups")
public class GroupModel extends ShapeModel{
	private List<ShapeModel> figures = new LinkedList<ShapeModel>();
	
	public GroupModel() {
		super(0, 0, 0, 0);
	}

	public void addFigures(List<ShapeModel> figures) {
		this.figures.addAll(figures);
		int x = 100000;	int y = 100000;
		int width = 0; int height = 0;
		for (ShapeModel figure : figures) {
			x = Math.min(x, figure.getBounds().x);
			y = Math.min(y, figure.getBounds().y);
		}
		for (ShapeModel figure : figures) {
			width = Math.max(width, figure.getBounds().x + figure.getBounds().width - x);
			height = Math.max(height, figure.getBounds().y + figure.getBounds().height - y);
		}
		bounds = new Rectangle(x, y, width, height);
	}
	
	public List<ShapeModel> getFigures() {
		return figures;
	}

	@Override
	public ShapeModel clone() {
		GroupModel clone = new GroupModel();
		List<ShapeModel> clones = new LinkedList<ShapeModel>();
		for (ShapeModel figure : figures) {
			clones.add(figure.clone());
		}
		clone.addFigures(clones);
		return clone;
	}
	
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		for (ShapeModel figure : figures) {
			figure.move(dx, dy);
		}		
	}
}