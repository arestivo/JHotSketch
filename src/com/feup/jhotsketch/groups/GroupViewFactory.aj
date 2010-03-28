package com.feup.jhotsketch.groups;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.view.ShapeView
;

@PackageName("Groups")
public aspect GroupViewFactory {
	pointcut createView(FigureModel figure) :
		call(ShapeView ShapeView.createView(FigureModel)) && args(figure);
	
	ShapeView around(FigureModel figure) : createView(figure) {
		if (figure instanceof GroupModel) return new GroupView();
		return proceed(figure);
	}	

}
