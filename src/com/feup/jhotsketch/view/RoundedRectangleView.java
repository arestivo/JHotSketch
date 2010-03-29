package com.feup.jhotsketch.view;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.RoundedRectangleModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("View")
public class RoundedRectangleView extends ShapeView{
	
	@Override
	public void draw(ShapeModel shape, GC gc) {
		RoundedRectangleModel rectangle = (RoundedRectangleModel) shape;
		gc.drawRoundRectangle(rectangle.getBounds().x, rectangle.getBounds().y, rectangle.getBounds().width, rectangle.getBounds().height, rectangle.getRadiusX(), rectangle.getRadiusY());
		super.draw(shape, gc);
	}

}