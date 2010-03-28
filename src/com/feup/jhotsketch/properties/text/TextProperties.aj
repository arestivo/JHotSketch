package com.feup.jhotsketch.properties.text;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Text")
public aspect TextProperties {
	public String ShapeModel.text = "";
	
	public void ShapeModel.setText(String text) {
		this.text = text;
	}
	
	public String ShapeModel.getText() {
		return text;
	}
	
	pointcut drawFigure(DiagramView canvas, ShapeModel shape, GC gc) :
		target(ShapeView+) &&
		call(void draw(DiagramView, ShapeModel, GC)) && 
		args(canvas, shape, gc);

	after(DiagramView canvas, ShapeModel shape, GC gc) : drawFigure(canvas, shape, gc) {
		if (!shape.getText().equals("")) {
			Point size = gc.textExtent(shape.getText());
			gc.drawText(shape.getText(), shape.getBounds().x + shape.getBounds().width / 2 - size.x / 2, shape.getBounds().y + shape.getBounds().height / 2 - size.y / 2 , true);
		}
	}
}