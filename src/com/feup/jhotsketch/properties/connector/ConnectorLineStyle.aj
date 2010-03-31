package com.feup.jhotsketch.properties.connector;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.connector.ConnectorModel;
import com.feup.jhotsketch.connector.ConnectorView;
import com.feup.jhotsketch.model.DiagramModel;

@PackageName("Properties.Connector")
public aspect ConnectorLineStyle{

	// Add line style properties to Figure
	
	private int ConnectorModel.lineStyle = SWT.LINE_SOLID;

	public int ConnectorModel.getLineStyle(){
		return lineStyle;
	}

	public void ConnectorModel.setLineStyle(int lineStyle){
		this.lineStyle = lineStyle;
		connectorChanged();
	}

	// Apply line style when drawing
	
	pointcut drawConnector(GC gc, ConnectorModel connector) :
		call(void ConnectorView.drawConnector(GC, ConnectorModel)) && 
		args(gc, connector);

	before(GC gc, ConnectorModel connector) : drawConnector(gc, connector) {
		gc.setLineStyle(connector.getLineStyle());
	}
	
	// Create Line Width Editor
	
	private Button solidButton = null;
	private Button dashedButton = null;
	private Button dottedButton = null;
	
	public void createLineStyleControls(Composite lineComposite) {
		Composite lineStyleComposite = new Composite(lineComposite, SWT.NONE);
		lineStyleComposite.setLayout(new GridLayout(4, false));

		GridData gd_label = new GridData(); 
		gd_label.widthHint = 60;
		
		Label label = new Label(lineStyleComposite, SWT.NONE);
		label.setText("Line Style");
		label.setLayoutData(gd_label);

		solidButton = new Button(lineStyleComposite, SWT.TOGGLE);
		solidButton.setImage(new Image(Display.getCurrent(), "icons/solid.gif"));

		dashedButton = new Button(lineStyleComposite, SWT.TOGGLE);
		dashedButton.setImage(new Image(Display.getCurrent(), "icons/dash.gif"));

		dottedButton = new Button(lineStyleComposite, SWT.TOGGLE);
		dottedButton.setImage(new Image(Display.getCurrent(), "icons/dot.gif"));

		solidButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelectedConnectors();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ConnectorModel connector : selected) {
					connector.setLineStyle(SWT.LINE_SOLID);
				}
			}
		});

		dashedButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelectedConnectors();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ConnectorModel connector : selected) {
					connector.setLineStyle(SWT.LINE_DASH);
				}
			}
		});

		dottedButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelectedConnectors();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ConnectorModel connector : selected) {
					connector.setLineStyle(SWT.LINE_DOT);
				}
			}
		});
	}

	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		int lineStyle = -1;
		solidButton.setSelection(false);
		dashedButton.setSelection(false);
		dottedButton.setSelection(false);
		for (ConnectorModel connector : diagram.getSelectedConnectors()) {
			if (lineStyle == -1) lineStyle = connector.getLineStyle();
			else if (lineStyle != connector.getLineStyle()) return;
		}
		if (lineStyle == SWT.LINE_SOLID) solidButton.setSelection(true);
		if (lineStyle == SWT.LINE_DASH) dashedButton.setSelection(true);
		if (lineStyle == SWT.LINE_DOT) dottedButton.setSelection(true);
	}	

	// Clone line style
	
	pointcut clone(ConnectorModel connector) :
		call (ConnectorModel clone()) && target(connector);

	after(ConnectorModel connector) returning (ConnectorModel clone): clone(connector) {
		clone.setLineStyle(connector.getLineStyle());
	}

	
}
