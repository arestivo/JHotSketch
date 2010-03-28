package com.feup.jhotsketch.properties.text;

import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Text")
public aspect TextProperties {
	private String ShapeModel.text = "";
	
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
	
	pointcut mouseDoubleClick(DiagramController controller, Event event) :
		call(void DiagramController.mouseDoubleClick(Event)) && target(controller) && args(event);
	
	after(DiagramController controller, Event event) : mouseDoubleClick(controller, event) {
		List<ShapeModel> selected = controller.getDiagram().getSelected();
		for (ShapeModel shape : selected) {
			shape.setText("Just Some Text");
		}
	}
}