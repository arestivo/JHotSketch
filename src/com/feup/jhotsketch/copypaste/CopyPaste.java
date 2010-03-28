package com.feup.jhotsketch.copypaste;

import java.util.HashSet;
import java.util.Set;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("CopyPaste")
public class CopyPaste {
	private static CopyPaste instance;
	
	private Set<FigureModel> clipboard = null;
	
	public static CopyPaste getInstance() {
		if (instance == null) instance = new CopyPaste();
		return instance;
	}
		
	public void cut() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<FigureModel>();
		for (FigureModel figure : diagram.getSelected()) {
			clipboard.add(figure);
		}
		diagram.removeFigures(diagram.getSelected());
		diagram.diagramChanged();
	}
	
	public void copy() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<FigureModel>();
		for (FigureModel figure : diagram.getSelected()) {
			clipboard.add(figure.clone());
		}
	}

	public void paste() {
		if (clipboard == null) return;
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		diagram.unselectAll();
		for (FigureModel figure : clipboard) {
			FigureModel clone = (FigureModel) figure.clone();
			diagram.setSelect(clone);
			diagram.addFigure(clone);
			diagram.moveFigure(clone, 10, 10);
			copy();
		}
		diagram.diagramChanged();
	}
	
}