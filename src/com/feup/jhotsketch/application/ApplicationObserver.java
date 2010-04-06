package com.feup.jhotsketch.application;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.viewer.Viewer;

@PackageName("Application")
public interface ApplicationObserver {
	public void diagramSelected(Viewer viewer);
}
