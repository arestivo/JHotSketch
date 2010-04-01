package com.feup.jhotsketch.copypaste;

import java.util.HashMap;
import java.util.HashSet; 
import java.util.Set;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.connector.ConnectorModel;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("CopyPaste")
public class CopyPaste {
	private static CopyPaste instance;
	
	private Set<ShapeModel> clipboard = null;
	private Set<ConnectorModel> connectorClipboard = null;
	
	public static CopyPaste getInstance() {
		if (instance == null) instance = new CopyPaste();
		return instance;
	}
		
	public void cut() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<ShapeModel>();
		connectorClipboard = new HashSet<ConnectorModel>();
		for (ShapeModel shape : diagram.getSelected())
			clipboard.add(shape);
		for (ConnectorModel connector : diagram.getConnectors())
			if (clipboard.contains(connector.getSource()) || clipboard.contains(connector.getSink()))
					connectorClipboard.add(connector);
		diagram.removeConnectors(connectorClipboard);
		diagram.removeFigures(diagram.getSelected());
		diagram.diagramChanged();
	}
	
	public void copy() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<ShapeModel>();
		connectorClipboard = new HashSet<ConnectorModel>();
		HashMap<ShapeModel, ShapeModel> clones = new HashMap<ShapeModel, ShapeModel>();
		for (ShapeModel shape : diagram.getSelected()) {
			ShapeModel clone = shape.clone();
			clipboard.add(clone);
			clones.put(shape, clone);
		}
		for (ConnectorModel connector : diagram.getConnectors()) {
			if (diagram.getSelected().contains(connector.getSource()) || diagram.getSelected().contains(connector.getSink())) {
				ConnectorModel clone = connector.clone();
				connectorClipboard.add(clone);
				if (clones.containsKey(connector.getSource()))
					clone.setSource(clones.get(connector.getSource()));
				if (clones.containsKey(connector.getSink()))
					clone.setSink(clones.get(connector.getSink()));
			}
		}
	}

	public void paste() {
		if (clipboard == null) return;
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		diagram.unselectAll();
		for (ConnectorModel connector : connectorClipboard) {
			diagram.addConnector(connector);
		}
		for (ShapeModel shape : clipboard) {
			diagram.setSelect(shape);
			diagram.addFigure(shape);
			diagram.moveFigure(shape, 10, 10);
		}
		copy();
		diagram.diagramChanged();
	}	
}