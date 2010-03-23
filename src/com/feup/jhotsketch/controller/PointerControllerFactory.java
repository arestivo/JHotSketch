package com.feup.jhotsketch.controller;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;

@PackageName("Controller")
public class PointerControllerFactory extends ControllerFactory{

	@Override
	public DiagramController createController(DiagramModel diagram) {
		return new PointerController(diagram);
	}

}
