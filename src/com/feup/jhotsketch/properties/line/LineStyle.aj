package com.feup.jhotsketch.properties.line;

import java.util.List;

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
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Line.Properties")
public aspect LineStyle{

	// Add line style properties to Figure
	
	private int ShapeModel.lineStyle = SWT.LINE_SOLID;

	public int ShapeModel.getLineStyle(){
		return lineStyle;
	}

	public void ShapeModel.setLineStyle(int lineStyle){
		this.lineStyle = lineStyle;
		figureChanged();
	}

	// Apply line style when drawing
	
	pointcut drawFigure(DiagramView canvas, ShapeModel figure, GC gc) :
		target(ShapeView+) &&
		call(void draw(DiagramView, ShapeModel, GC)) && 
		args(canvas, figure, gc);

	before(DiagramView canvas, ShapeModel figure, GC gc) : drawFigure(canvas, figure, gc) {
		gc.setLineStyle(figure.getLineStyle());
	}
	
	// Create Line Width Editor
	
	private Button solidButton = null;
	private Button dashedButton = null;
	private Button dottedButton = null;
	
	public void createLineStyleControls(Composite lineComposite) {
		Composite lineStyleComposite = new Composite(lineComposite, SWT.NONE);
		lineStyleComposite.setLayout(new GridLayout(4, false));

		GridData gd_label = new GridData(); 
		gd_label.widthHint = 30;
		
		Label label = new Label(lineStyleComposite, SWT.NONE);
		label.setText("Style");
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
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ShapeModel figure : selected) {
					figure.setLineStyle(SWT.LINE_SOLID);
				}
			}
		});

		dashedButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ShapeModel figure : selected) {
					figure.setLineStyle(SWT.LINE_DASH);
				}
			}
		});

		dottedButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentView().getDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ShapeModel figure : selected) {
					figure.setLineStyle(SWT.LINE_DOT);
				}
			}
		});		
	}

	// Update Line Editor
	
	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		int lineStyle = -1;
		solidButton.setSelection(false);
		dashedButton.setSelection(false);
		dottedButton.setSelection(false);
		for (ShapeModel figure : diagram.getSelected()) {
			if (lineStyle == -1) lineStyle = figure.getLineStyle();
			else if (lineStyle != figure.getLineStyle()) return;
		}
		if (lineStyle == SWT.LINE_SOLID) solidButton.setSelection(true);
		if (lineStyle == SWT.LINE_DASH) dashedButton.setSelection(true);
		if (lineStyle == SWT.LINE_DOT) dottedButton.setSelection(true);
	}	

	// Clone line style
	
	pointcut clone(ShapeModel figure) :
		call (ShapeModel clone()) && target(figure);

	ShapeModel around(ShapeModel figure) : clone(figure) {
		ShapeModel clone = proceed(figure);
		clone.setLineStyle(figure.getLineStyle());
		return clone;
	}

}
