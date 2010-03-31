package com.feup.jhotsketch.properties.connector;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.connector.ConnectorModel;

@PackageName("Properties.Connector")
public aspect ConnectorArrowSize {
	
	// Create Arrow Size Editor
	
	private Scale scaleSource = null;
	private Scale scaleSink = null;
	
	public void createArrowControls(Composite lineComposite) {
		Composite lineWidthComposite = new Composite(lineComposite, SWT.NONE);
		lineWidthComposite.setLayout(new GridLayout(2, false));
		
		GridData gd_fill = new GridData(); 
		gd_fill.grabExcessHorizontalSpace = true;
		gd_fill.horizontalAlignment = SWT.FILL;
		gd_fill.widthHint = 100;

		GridData gd_label = new GridData(); 
		gd_label.widthHint = 60;
		
		Label labelSource = new Label(lineWidthComposite, SWT.NONE);
		labelSource.setText("Source Size");
		labelSource.setLayoutData(gd_label);

		scaleSource = new Scale(lineWidthComposite, SWT.HORIZONTAL);
		scaleSource.setMinimum(10);
		scaleSource.setMaximum(40);
		scaleSource.setLayoutData(gd_fill);
		
		scaleSource.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelectedConnectors();
				for (ConnectorModel connector : selected) {
					connector.setSourceEndSize(scaleSource.getSelection());
					connector.connectorChanged();
				}
			}
		});

		Label labelSink = new Label(lineWidthComposite, SWT.NONE);
		labelSink.setText("Target Size");
		labelSink.setLayoutData(gd_label);

		scaleSink = new Scale(lineWidthComposite, SWT.HORIZONTAL);
		scaleSink.setMinimum(10);
		scaleSink.setMaximum(40);
		scaleSink.setLayoutData(gd_fill);
		
		scaleSink.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelectedConnectors();
				for (ConnectorModel connector : selected) {
					connector.setSinkEndSize(scaleSink.getSelection());
					connector.connectorChanged();
				}
			}
		});

	}

}
