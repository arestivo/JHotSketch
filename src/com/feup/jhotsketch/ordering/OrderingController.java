package com.feup.jhotsketch.ordering;

import java.util.LinkedList;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("Ordering")
public class OrderingController {
	private static OrderingController instance;

	public void back() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		LinkedList<FigureModel> selected = new LinkedList<FigureModel>(); selected.addAll(diagram.getSelected());
		diagram.removeFigures(selected);
		diagram.addFiguresAtStart(selected);
		diagram.setSelect(selected);
		diagram.diagramChanged();
	}

	public void front() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		LinkedList<FigureModel> selected = new LinkedList<FigureModel>(); selected.addAll(diagram.getSelected());
		diagram.removeFigures(selected);
		diagram.addFigures(selected);
		diagram.setSelect(selected);
		diagram.diagramChanged();
	}

	public static OrderingController getInstance() {
		if (instance == null) instance = new OrderingController();
		return instance;
	}
}