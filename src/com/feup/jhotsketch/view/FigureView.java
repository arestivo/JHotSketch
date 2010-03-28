package com.feup.jhotsketch.view;

import org.eclipse.swt.SWT; 
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.CircleModel;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.model.RectangleModel;

@PackageName("View")
public abstract class FigureView {
	public void draw(DiagramView canvas, FigureModel figure, GC gc){
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	}

	public static FigureView createView(FigureModel figure) {
		if (figure instanceof RectangleModel) return new RectangleView();
		if (figure instanceof CircleModel) return new CircleView();		
		return null;
	}
}