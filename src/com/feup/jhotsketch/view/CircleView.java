package com.feup.jhotsketch.view;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.CircleModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("View")
public class CircleView extends FigureView{

	@Override
	public void draw(DiagramView canvas, FigureModel figure, GC gc) {
		CircleModel circle = (CircleModel) figure;
		gc.drawOval(circle.getBounds().x, circle.getBounds().y, circle.getBounds().width, circle.getBounds().height);
		super.draw(canvas, figure, gc);
	}

}
