package com.feup.jhotsketch.model;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public interface DiagramObserver {
	public void diagramChanged(DiagramModel diagram);
}
