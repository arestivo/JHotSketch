package com.feup.jhotsketch.controller;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Controller")
public interface ControllerObserver {
	public void controllerChanged(DiagramController controller);
}
