package com.feup.jhotsketch.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC; 
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("View")
public class OvalView extends ShapeView{

	@Override
	public void draw(ShapeModel shape, GC gc) {
		OvalModel circle = (OvalModel) shape;
		gc.drawOval(circle.getBounds().x, circle.getBounds().y, circle.getBounds().width, circle.getBounds().height);
		super.draw(shape, gc);
	}

	@Override
	public void delete(ShapeModel shape, GC gc) {
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		OvalModel circle = (OvalModel) shape;
		gc.fillOval(circle.getBounds().x, circle.getBounds().y, circle.getBounds().width, circle.getBounds().height);
	}

}
