package com.feup.jhotsketch.controller;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;

@PackageName("Controller")
public abstract class ControllerFactory {
	public abstract DiagramController createController(DiagramModel diagram);
}
