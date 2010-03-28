package com.feup.jhotsketch.groups;

import java.util.LinkedList;
import java.util.List;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Groups")
public class GroupController {
	private static GroupController instance;

	public static GroupController getInstance() {
		if (instance == null) instance = new GroupController();
		return instance;
	}
	
	public void group() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		List<ShapeModel> selected = diagram.getSelected();
		if (selected.size() <= 1) return;
		for (ShapeModel figure : selected) {
			figure.setSelected(false);
		}
		GroupModel group = new GroupModel();
		group.addFigures(selected);
		diagram.removeFigures(selected);
		diagram.addFigure(group);
		diagram.unselectAll();
		diagram.setSelect(group);
	}

	public void ungroup() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		List<ShapeModel> selected = new LinkedList<ShapeModel>(); 
		selected.addAll(diagram.getSelected());
		for (ShapeModel figure : selected) {
			if (figure instanceof GroupModel) {
				diagram.removeFigure(figure);
				diagram.addFigures(((GroupModel)figure).getFigures());
				diagram.setSelect(((GroupModel)figure).getFigures());
			}
		}
	}

}