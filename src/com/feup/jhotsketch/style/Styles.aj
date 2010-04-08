package com.feup.jhotsketch.style;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.PropertyPanel;

@PackageName("Style")
public aspect Styles {
	pointcut createPropertyPanel(Composite composite) :
		call (PropertyPanel.new(Composite, ..)) && args(composite, ..);
	
	after(Composite composite) : createPropertyPanel(composite) {
		new StylesPanel(composite, SWT.NONE);
	}
}
