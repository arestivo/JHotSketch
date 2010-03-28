package com.feup.jhotsketch.properties.group;

 import org.eclipse.swt.graphics.Color;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.groups.GroupModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("Properties.Group")
public aspect GroupProperties {

	// Set Line Style
	
	pointcut setLineStyle(GroupModel group, int s) :
		call (void FigureModel.setLineStyle(int)) && target(group) && args(s);
	
	after(GroupModel group, int s) : setLineStyle(group, s) {
		for (FigureModel figure : group.getFigures()) {
			figure.setLineStyle(s);
		}
	}

	// Set Line Width
	
	pointcut setLineWidth(GroupModel group, int w) :
		call (void FigureModel.setLineWidth(int)) && target(group) && args(w);
	
	after(GroupModel group, int w) : setLineWidth(group, w) {
		for (FigureModel figure : group.getFigures()) {
			figure.setLineWidth(w);
		}
	}
	
	// Set Line Color

	pointcut setLineColor(GroupModel group, Color c) :
		call (void FigureModel.setLineColor(Color)) && target(group) && args(c);
	
	after(GroupModel group, Color c) : setLineColor(group, c) {
		for (FigureModel figure : group.getFigures()) {
			figure.setLineColor(c);
		}
	}

	// Set Fill Color

	pointcut setFillColor(GroupModel group, Color c) :
		call (void FigureModel.setFillColor(Color)) && target(group) && args(c) && !within(GroupModel);
	
	after(GroupModel group, Color c) : setFillColor(group, c) {
		for (FigureModel figure : group.getFigures()) {
			figure.setFillColor(c);
		}
	}

}