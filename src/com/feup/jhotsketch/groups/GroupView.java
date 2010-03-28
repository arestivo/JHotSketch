package com.feup.jhotsketch.groups;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.FigureView;

@PackageName("Groups")
public class GroupView extends FigureView {

	@Override
	public void draw(DiagramView canvas, FigureModel figure, GC gc) {
		GroupModel group = (GroupModel) figure;
		for (FigureModel f : group.getFigures()) {
			FigureView.createView(f).draw(canvas, f, gc);
		}
		super.draw(canvas, figure, gc);
	}

}
