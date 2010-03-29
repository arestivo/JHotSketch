package com.feup.jhotsketch.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC; 
import org.eclipse.swt.widgets.Display;

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

	@Override
	public void delete(ShapeModel shape, GC gc) {
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		RoundedRectangleModel rectangle = (RoundedRectangleModel) shape;
		gc.fillRoundRectangle(rectangle.getBounds().x, rectangle.getBounds().y, rectangle.getBounds().width, rectangle.getBounds().height, rectangle.getRadiusX(), rectangle.getRadiusY());
	}	
}