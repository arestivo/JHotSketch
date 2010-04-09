package com.feup.jhotsketch.style;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;
import com.feup.jhotsketch.application.PropertyPanel;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Style")
public aspect Style {
	StylesPanel panel;
	
	pointcut createPropertyPanel(Composite composite) :
		call (PropertyPanel.new(Composite, ..)) && args(composite, ..);
	
	after(Composite composite) : createPropertyPanel(composite) {
		panel = new StylesPanel(composite, SWT.NONE);
	}
	
	pointcut shapeAddedRemoved(Diagram diagram) :
		(call (void Diagram.addShape(..)) || call(void Diagram.removeShapes(..))) && target(diagram);

	after(Diagram diagram) : shapeAddedRemoved(diagram) {
		panel.update(diagram);
	}
	
	pointcut shapeChanged(Shape shape) :
		call(void Shape.set*(..)) && target(shape) && within(DiagramController);
	
	after(Shape shape) : shapeChanged(shape) {
		panel.update(Application.getInstance().getActiveEditor().getDiagram());
	}
}
