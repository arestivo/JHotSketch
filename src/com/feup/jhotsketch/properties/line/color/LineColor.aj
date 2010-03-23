package com.feup.jhotsketch.properties.line.color;

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
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.FigureView;

@PackageName("Line.Color")
public aspect LineColor{

	// Add line color properties to Figure
	
	private Color FigureModel.lineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

	public Color FigureModel.getLineColor(){
		return lineColor;
	}

	public void FigureModel.setLineColor(Color lineColor){
		this.lineColor = lineColor;
		figureChanged();
	}

	// Apply line color when drawing
	
	pointcut drawFigure(DiagramView canvas, FigureModel figure, GC gc) :
		target(FigureView+) &&
		call(void draw(DiagramView, FigureModel, GC)) && 
		args(canvas, figure, gc);

	before(DiagramView canvas, FigureModel figure, GC gc) : drawFigure(canvas, figure, gc) {
		gc.setForeground(figure.getLineColor());
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

		colorButton = new Button(lineColorComposite, SWT.TOGGLE);
		colorButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		colorButton.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		
		colorButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ColorDialog dialog = new ColorDialog(lineComposite.getShell());
				dialog.open();
			    Color color = new Color(Display.getCurrent(), dialog.getRGB());
				Set<FigureModel> selected = JHotSketch.getInstance().getCurrentView().getDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (FigureModel figure : selected) {
					figure.setLineColor(color);
				}
				colorButton.setSelection(false);
			}
		});
	}

	// Update Line Editor
	
	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		Color lineColor = null;
		colorButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		colorButton.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		for (FigureModel figure : diagram.getSelected()) {
			if (lineColor == null) lineColor = figure.getLineColor();
			else if (!lineColor.equals(figure.getLineColor())) return;
		}
		if (lineColor != null) {
			colorButton.setBackground(lineColor);
			colorButton.setImage(new Image(Display.getCurrent(), "icons/solid.gif"));
		}
	}	
}
