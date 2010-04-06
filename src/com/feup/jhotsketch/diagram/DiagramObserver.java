package com.feup.jhotsketch.diagram;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Diagram")
public interface DiagramObserver {
	public void diagramChanged(Diagram diagram);
}
