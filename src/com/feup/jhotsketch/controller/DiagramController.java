package com.feup.jhotsketch.controller;

import org.eclipse.swt.widgets.Event;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;

@PackageName("Controller")
public abstract class DiagramController {
	protected DiagramModel diagram;
	
	public DiagramController(DiagramModel diagram) {
		this.diagram = diagram;
	}
	
	public abstract void mouseDown(Event event);

	public abstract void mouseUp(Event event);

	public abstract void mouseMove(Event event);
}
