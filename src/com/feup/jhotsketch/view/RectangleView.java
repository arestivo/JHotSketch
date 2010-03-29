package com.feup.jhotsketch.view;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.RectangleModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("View")
public class RectangleView extends ShapeView{
	
	@Override
	public void draw(ShapeModel shape, GC gc) {
		RectangleModel rectangle = (RectangleModel) shape;
		gc.drawRectangle(rectangle.getBounds().x, rectangle.getBounds().y, rectangle.getBounds().width, rectangle.getBounds().height);
		super.draw(shape, gc);
	}
}