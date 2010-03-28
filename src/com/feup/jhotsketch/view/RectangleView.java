package com.feup.jhotsketch.view;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.model.RectangleModel;

@PackageName("View")
public class RectangleView extends FigureView{
	
	@Override
	public void draw(DiagramView canvas, FigureModel figure, GC gc) {
		RectangleModel rectangle = (RectangleModel) figure;
		gc.drawRectangle(rectangle.getBounds().x, rectangle.getBounds().y, rectangle.getBounds().width, rectangle.getBounds().height);
		super.draw(canvas, figure, gc);
	}

}