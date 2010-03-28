package com.feup.jhotsketch.groups;

import com.feup.contribution.aida.annotations.PackageName; 

import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Groups")
public aspect GroupViewFactory {
	pointcut createView(ShapeModel shape) :
		call(ShapeView ShapeView.createView(ShapeModel)) && args(shape);
	
	ShapeView around(ShapeModel shape) : createView(shape) {
		if (shape instanceof GroupModel) return new GroupView();
		return proceed(shape);
	}	

}
