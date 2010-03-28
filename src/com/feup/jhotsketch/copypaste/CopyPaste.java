package com.feup.jhotsketch.copypaste;

import java.util.HashSet;
import java.util.Set;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("CopyPaste")
public class CopyPaste {
	private static CopyPaste instance;
	
	private Set<ShapeModel> clipboard = null;
	
	public static CopyPaste getInstance() {
		if (instance == null) instance = new CopyPaste();
		return instance;
	}
		
	public void cut() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<ShapeModel>();
		for (ShapeModel shape : diagram.getSelected()) {
			clipboard.add(shape);
		}
		diagram.removeFigures(diagram.getSelected());
		diagram.diagramChanged();
	}
	
	public void copy() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<ShapeModel>();
		for (ShapeModel shape : diagram.getSelected()) {
			clipboard.add(shape.clone());
		}
	}

	public void paste() {
		if (clipboard == null) return;
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		diagram.unselectAll();
		for (ShapeModel shape : clipboard) {
			ShapeModel clone = (ShapeModel) shape.clone();
			diagram.setSelect(clone);
			diagram.addFigure(clone);
			diagram.moveFigure(clone, 10, 10);
			copy();
		}
		diagram.diagramChanged();
	}
	
}