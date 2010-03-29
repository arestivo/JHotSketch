package com.feup.jhotsketch.connector;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Connector")
public aspect Connector {
	private LinkedList<ConnectorModel>	DiagramModel.connectors = new LinkedList<ConnectorModel>();
	
	public void DiagramModel.addConnector(ConnectorModel connector) {
		connectors.add(connector);
	}

	public List<ConnectorModel> DiagramModel.getConnectors() {
		return connectors;
	}
	
	pointcut newDiagram() :
		call (DiagramModel.new());
	
	after() returning(DiagramModel diagram) : newDiagram() {
		diagram.addConnector(new ConnectorModel((ShapeModel)diagram.getFigures().toArray()[0], (ShapeModel)diagram.getFigures().toArray()[1]));
	}
	
	pointcut diagramPaint(GC gc, DiagramView view) :
		call (void DiagramView.paint(GC)) && args(gc) && target(view);
	
	before(GC gc, DiagramView view) : diagramPaint(gc, view) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		for (ConnectorModel connector : view.getDiagram().getConnectors()) {
			ShapeModel source = connector.getSource();
			ShapeModel sink = connector.getSink();
			gc.drawLine(source.getBounds().x + source.getBounds().width / 2, 
					    source.getBounds().y + source.getBounds().height / 2,
					    sink.getBounds().x + source.getBounds().width / 2, 
					    sink.getBounds().y + source.getBounds().height / 2);
		}
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		for (ShapeModel shape : view.getDiagram().getFigures()) {
			ShapeView.createView(shape).delete(shape, gc);
		}
	}
}
