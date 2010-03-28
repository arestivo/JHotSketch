package com.feup.jhotsketch.groups;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Groups")
public class GroupModel extends ShapeModel{
	private List<ShapeModel> shapes = new LinkedList<ShapeModel>();
	
	public GroupModel() {
		super(0, 0, 0, 0);
	}

	public void addFigures(List<ShapeModel> shapes) {
		this.shapes.addAll(shapes);
		int x = 100000;	int y = 100000;
		int width = 0; int height = 0;
		for (ShapeModel shape : shapes) {
			x = Math.min(x, shape.getBounds().x);
			y = Math.min(y, shape.getBounds().y);
		}
		for (ShapeModel shape : shapes) {
			width = Math.max(width, shape.getBounds().x + shape.getBounds().width - x);
			height = Math.max(height, shape.getBounds().y + shape.getBounds().height - y);
		}
		bounds = new Rectangle(x, y, width, height);
	}
	
	public List<ShapeModel> getFigures() {
		return shapes;
	}

	@Override
	public ShapeModel clone() {
		GroupModel clone = new GroupModel();
		List<ShapeModel> clones = new LinkedList<ShapeModel>();
		for (ShapeModel shape : shapes) {
			clones.add(shape.clone());
		}
		clone.addFigures(clones);
		return clone;
	}
	
	@Override
	public void move(int dx, int dy) {
		super.move(dx, dy);
		for (ShapeModel shape : shapes) {
			shape.move(dx, dy);
		}		
	}
}