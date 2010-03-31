package com.feup.jhotsketch.properties.connector;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.connector.ConnectorModel;
import com.feup.jhotsketch.connector.ConnectorModel.END;
import com.feup.jhotsketch.model.DiagramModel;

@PackageName("Properties.Connector")
public aspect ConnectorEndStyle{
	
	// Create Line Width Editor
	
	private Button leftNoneButton = null;
	private Button leftSimpleArrowButton = null;
	private Button leftHollowArrowButton = null;
	private Button leftFilledArrowButton = null;
	private Button leftHollowSquareButton = null;
	private Button leftFilledSquareButton = null;
	private Button leftHollowCircleButton = null;
	private Button leftFilledCircleButton = null;

	private Button rightNoneButton = null;
	private Button rightSimpleArrowButton = null;
	private Button rightHollowArrowButton = null;
	private Button rightFilledArrowButton = null;
	private Button rightHollowSquareButton = null;
	private Button rightFilledSquareButton = null;
	private Button rightHollowCircleButton = null;
	private Button rightFilledCircleButton = null;
	
	public void createEndControls(Composite lineComposite) {
		Composite lineStyleComposite = new Composite(lineComposite, SWT.NONE);
		lineStyleComposite.setLayout(new GridLayout(8, false));
		
		leftNoneButton = createSourceButton(END.NONE, "none", lineStyleComposite);
		leftSimpleArrowButton = createSourceButton(END.SIMPLEARROW, "simplearrow", lineStyleComposite);
		leftHollowArrowButton = createSourceButton(END.HOLLOWARROW, "hollowarrow", lineStyleComposite);
		leftFilledArrowButton = createSourceButton(END.FILLEDARROW, "filledarrow", lineStyleComposite);
		leftHollowSquareButton = createSourceButton(END.HOLLOWSQUARE, "hollowsquare", lineStyleComposite);
		leftFilledSquareButton = createSourceButton(END.FILLEDSQUARE, "filledsquare", lineStyleComposite);
		leftHollowCircleButton = createSourceButton(END.HOLLOWCIRCLE, "hollowcircle", lineStyleComposite);
		leftFilledCircleButton = createSourceButton(END.FILLEDCIRCLE, "filledcircle", lineStyleComposite);
		
		rightNoneButton = createSinkButton(END.NONE, "none", lineStyleComposite);
		rightSimpleArrowButton = createSinkButton(END.SIMPLEARROW, "simplearrow", lineStyleComposite);
		rightHollowArrowButton = createSinkButton(END.HOLLOWARROW, "hollowarrow", lineStyleComposite);
		rightFilledArrowButton = createSinkButton(END.FILLEDARROW, "filledarrow", lineStyleComposite);
		rightHollowSquareButton = createSinkButton(END.HOLLOWSQUARE, "hollowsquare", lineStyleComposite);
		rightFilledSquareButton = createSinkButton(END.FILLEDSQUARE, "filledsquare", lineStyleComposite);
		rightHollowCircleButton = createSinkButton(END.HOLLOWCIRCLE, "hollowcircle", lineStyleComposite);
		rightFilledCircleButton = createSinkButton(END.FILLEDCIRCLE, "filledcircle", lineStyleComposite);
	}

	private Button createSourceButton(final END end, String image, Composite composite) {
		Button button = new Button(composite, SWT.TOGGLE);
		button.setImage(new Image(Display.getCurrent(), "icons/" + image + "left.gif"));

		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelectedConnectors();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ConnectorModel connector : selected) {
					connector.setSourceEnd(end);
					connector.connectorChanged();
				}
			}
		});
		
		return button;
	}

	private Button createSinkButton(final END end, String image, Composite composite) {
		Button button = new Button(composite, SWT.TOGGLE);
		button.setImage(new Image(Display.getCurrent(), "icons/" + image + "right.gif"));

		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Set<ConnectorModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelectedConnectors();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ConnectorModel connector : selected) {
					connector.setSinkEnd(end);
					connector.connectorChanged();
				}
			}
		});
		
		return button;
	}
	
	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		END sourceEndStyle = END.UNKNOWN;
		END sinkEndStyle = END.UNKNOWN;
		leftNoneButton.setSelection(false);
		leftFilledArrowButton.setSelection(false);
		leftFilledCircleButton.setSelection(false);
		leftFilledSquareButton.setSelection(false);
		leftHollowArrowButton.setSelection(false);
		leftHollowCircleButton.setSelection(false);
		leftHollowSquareButton.setSelection(false);
		leftSimpleArrowButton.setSelection(false);
		rightNoneButton.setSelection(false);
		rightFilledArrowButton.setSelection(false);
		rightFilledCircleButton.setSelection(false);
		rightFilledSquareButton.setSelection(false);
		rightHollowArrowButton.setSelection(false);
		rightHollowCircleButton.setSelection(false);
		rightHollowSquareButton.setSelection(false);
		rightSimpleArrowButton.setSelection(false);
		for (ConnectorModel connector : diagram.getSelectedConnectors()) {
			if (sinkEndStyle == END.UNKNOWN) sinkEndStyle = connector.getSinkEnd();
			else if (sinkEndStyle != connector.getSinkEnd()) sinkEndStyle = END.UNMATCHED;
			if (sourceEndStyle == END.UNKNOWN) sourceEndStyle = connector.getSourceEnd();
			else if (sourceEndStyle != connector.getSourceEnd()) sourceEndStyle = END.UNMATCHED;
		}
		if (sinkEndStyle == END.NONE) rightNoneButton.setSelection(true);
		if (sinkEndStyle == END.FILLEDARROW) rightFilledArrowButton.setSelection(true);
		if (sinkEndStyle == END.FILLEDCIRCLE) rightFilledCircleButton.setSelection(true);
		if (sinkEndStyle == END.FILLEDSQUARE) rightFilledSquareButton.setSelection(true);
		if (sinkEndStyle == END.HOLLOWARROW) rightHollowArrowButton.setSelection(true);
		if (sinkEndStyle == END.HOLLOWCIRCLE) rightHollowCircleButton.setSelection(true);
		if (sinkEndStyle == END.HOLLOWSQUARE) rightHollowSquareButton.setSelection(true);
		if (sinkEndStyle == END.SIMPLEARROW) rightSimpleArrowButton.setSelection(true);

		if (sourceEndStyle == END.NONE) leftNoneButton.setSelection(true);
		if (sourceEndStyle == END.FILLEDARROW) leftFilledArrowButton.setSelection(true);
		if (sourceEndStyle == END.FILLEDCIRCLE) leftFilledCircleButton.setSelection(true);
		if (sourceEndStyle == END.FILLEDSQUARE) leftFilledSquareButton.setSelection(true);
		if (sourceEndStyle == END.HOLLOWARROW) leftHollowArrowButton.setSelection(true);
		if (sourceEndStyle == END.HOLLOWCIRCLE) leftHollowCircleButton.setSelection(true);
		if (sourceEndStyle == END.HOLLOWSQUARE) leftHollowSquareButton.setSelection(true);
		if (sourceEndStyle == END.SIMPLEARROW) leftSimpleArrowButton.setSelection(true);
	}	

}
