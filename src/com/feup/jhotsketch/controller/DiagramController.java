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
	
	public abstract void mouseClick(Event event);
	
	public abstract void mouseDrag(Event event1, Event event2);

	public abstract void mouseDrop(Event event1, Event event2);
}
