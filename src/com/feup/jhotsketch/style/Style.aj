package com.feup.jhotsketch.style;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Composite;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;
import com.feup.jhotsketch.application.PropertyPanel;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.controller.HandleController;
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

	pointcut connectorAddedRemoved(Diagram diagram) :
		(call (void Diagram.addConnector(..)) || call(void Diagram.removeConnectors(..))) && target(diagram);

	after(Diagram diagram) : connectorAddedRemoved(diagram) {
		panel.update(diagram);
	}

	pointcut shapeChanged(Shape shape) :
		call(void Shape.set*(..)) && target(shape) && within(DiagramController);
	
	after(Shape shape) : shapeChanged(shape) {
		panel.update(Application.getInstance().getActiveEditor().getDiagram());
	}

	pointcut connectorChanged(Connector connector) :
		call(void Connector.set*(..)) && target(connector) && within(DiagramController);
	
	after(Connector connector) : connectorChanged(connector) {
		panel.update(Application.getInstance().getActiveEditor().getDiagram());
	}
	
	pointcut addShape(Shape shape) :
		call(void DiagramController.addShape(Shape)) && args(shape) && (this(MouseMoveListener) || this(MouseListener));
	
	after(Shape shape) : addShape(shape){
		if (StylesPanel.getInstance().getSelectedShapeStyle() != null)
			shape.copyProperties(StylesPanel.getInstance().getSelectedShapeStyle());
	}

	pointcut addConnector(Connector connector) :
		call(void DiagramController.addConnector(Connector)) && args(connector) && (this(HandleController));
	
	after(Connector connector) : addConnector(connector){
		if (StylesPanel.getInstance().getSelectedConnectorStyle() != null)
			connector.copyProperties(StylesPanel.getInstance().getSelectedConnectorStyle());
	}
}