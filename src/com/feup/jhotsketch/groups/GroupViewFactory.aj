package com.feup.jhotsketch.groups;

import com.feup.contribution.aida.annotations.PackageName; 

import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Groups")
public aspect GroupViewFactory {
	pointcut createView(ShapeModel figure) :
		call(ShapeView ShapeView.createView(ShapeModel)) && args(figure);
	
	ShapeView around(ShapeModel figure) : createView(figure) {
		if (figure instanceof GroupModel) return new GroupView();
		return proceed(figure);
	}	

}
