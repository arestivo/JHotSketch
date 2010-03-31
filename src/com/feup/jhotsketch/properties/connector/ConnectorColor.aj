package com.feup.jhotsketch.properties.connector;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.connector.ConnectorModel;
import com.feup.jhotsketch.connector.ConnectorView;

@PackageName("Connector.Properties")
public aspect ConnectorColor{

	// Add line color properties to Figure
	
	private Color ConnectorModel.lineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

	public Color ConnectorModel.getLineColor(){
		return lineColor;
	}

	public void ConnectorModel.setLineColor(Color lineColor){
		this.lineColor = lineColor;
//		connectorChanged();
	}

	// Apply line color when drawing
	
	pointcut drawConnector(GC gc, ConnectorModel connector) :
		call(void ConnectorView.drawConnector(GC, ConnectorModel)) && args(gc, connector);
	
	before(GC gc, ConnectorModel connector) : drawConnector(gc, connector) {
		gc.setForeground(connector.getLineColor());
	}
	
	// Create Line Width Editor
	
	private Button colorButton = null;
	
	public void createLineColorControls(final Composite lineComposite) {
		Composite lineColorComposite = new Composite(lineComposite, SWT.NONE);
		lineColorComposite.setLayout(new GridLayout(2, false));

		GridData gd_label = new GridData(); 
		gd_label.widthHint = 30;
		
		Label label = new Label(lineColorComposite, SWT.NONE);
		label.setText("Color");
		label.setLayoutData(gd_label);

		colorButton = new Button(lineColorComposite, SWT.PUSH);
		colorButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		colorButton.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		
		colorButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ColorDialog dialog = new ColorDialog(lineComposite.getShell());
				dialog.open();
				if (dialog.getRGB() == null) return;
			    Color color = new Color(Display.getCurrent(), dialog.getRGB());
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentView().getDiagram().getSelectedConnectors();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ConnectorModel connector : selected) {
					connector.setLineColor(color);
				}
				colorButton.setSelection(false);
			}
		});
	}

	// Update Line Editor
	
/*	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		Color lineColor = null;
		colorButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		colorButton.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		for (ShapeModel shape : diagram.getSelected()) {
			if (lineColor == null) lineColor = shape.getLineColor();
			else if (!lineColor.equals(shape.getLineColor())) return;
		}
		if (lineColor != null) {
			colorButton.setBackground(lineColor);
			colorButton.setImage(new Image(Display.getCurrent(), "icons/solid.gif"));
		}
	}	
	*/
}