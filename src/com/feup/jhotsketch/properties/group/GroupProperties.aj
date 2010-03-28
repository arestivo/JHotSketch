package com.feup.jhotsketch.properties.group;

 import org.eclipse.swt.graphics.Color;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.groups.GroupModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Group.Properties")
public aspect GroupProperties {

	// Set Line Style
	
	pointcut setLineStyle(GroupModel group, int s) :
		call (void ShapeModel.setLineStyle(int)) && target(group) && args(s);
	
	after(GroupModel group, int s) : setLineStyle(group, s) {
		for (ShapeModel shape : group.getFigures()) {
			shape.setLineStyle(s);
		}
	}

	// Set Line Width
	
	pointcut setLineWidth(GroupModel group, int w) :
		call (void ShapeModel.setLineWidth(int)) && target(group) && args(w);
	
	after(GroupModel group, int w) : setLineWidth(group, w) {
		for (ShapeModel shape : group.getFigures()) {
			shape.setLineWidth(w);
		}
	}
	
	// Set Line Color

	pointcut setLineColor(GroupModel group, Color c) :
		call (void ShapeModel.setLineColor(Color)) && target(group) && args(c);
	
	after(GroupModel group, Color c) : setLineColor(group, c) {
		for (ShapeModel shape : group.getFigures()) {
			shape.setLineColor(c);
		}
	}

	// Set Fill Color

	pointcut setFillColor(GroupModel group, Color c) :
		call (void ShapeModel.setFillColor(Color)) && target(group) && args(c) && !within(GroupModel);
	
	after(GroupModel group, Color c) : setFillColor(group, c) {
		for (ShapeModel shape : group.getFigures()) {
			shape.setFillColor(c);
		}
	}

}