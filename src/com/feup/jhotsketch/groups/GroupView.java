package com.feup.jhotsketch.groups;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Groups")
public class GroupView extends ShapeView {

	@Override
	public void draw(DiagramView canvas, ShapeModel shape, GC gc) {
		GroupModel group = (GroupModel) shape;
		for (ShapeModel f : group.getFigures()) {
			ShapeView.createView(f).draw(canvas, f, gc);
		}
		super.draw(canvas, shape, gc);
	}

}
