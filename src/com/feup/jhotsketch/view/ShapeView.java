package com.feup.jhotsketch.view;

import org.eclipse.swt.SWT; 
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.RoundedRectangleModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.model.RectangleModel;

@PackageName("View")
public abstract class ShapeView {
	public void draw(DiagramView canvas, ShapeModel shape, GC gc){
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	}

	public static ShapeView createView(ShapeModel shape) {
		if (shape instanceof RectangleModel) return new RectangleView();
		if (shape instanceof RoundedRectangleModel) return new RoundedRectangleView();
		if (shape instanceof OvalModel) return new OvalView();		
		return null;
	}
}