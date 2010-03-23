package com.feup.jhotsketch.grouping;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.FigureView;

@PackageName("Grouping")
public class GroupView extends FigureView {

	@Override
	public void draw(DiagramView canvas, FigureModel figure, GC gc) {
		super.draw(canvas, figure, gc);
		GroupModel group = (GroupModel) figure;
		for (FigureModel f : group.getFigures()) {
			FigureView.createView(f).draw(canvas, f, gc);
		}
	}

}
