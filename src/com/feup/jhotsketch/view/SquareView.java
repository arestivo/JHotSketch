package com.feup.jhotsketch.view;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.model.SquareModel;

@PackageName("View")
public class SquareView extends FigureView{
	
	@Override
	public void draw(DiagramView canvas, FigureModel figure, GC gc) {
		SquareModel rectangle = (SquareModel) figure;
		gc.drawRectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
		super.draw(canvas, figure, gc);
	}

}