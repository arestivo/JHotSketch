package com.feup.jhotsketch.view;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("View")
public class OvalView extends ShapeView{

	@Override
	public void draw(DiagramView canvas, ShapeModel shape, GC gc) {
		OvalModel circle = (OvalModel) shape;
		gc.drawOval(circle.getBounds().x, circle.getBounds().y, circle.getBounds().width, circle.getBounds().height);
		super.draw(canvas, shape, gc);
	}

}
