package com.feup.jhotsketch.properties.fill.color;

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

@PackageName("Fill.Color")
public aspect FillColor{

	// Add fill color properties to Figure
	
	private Color FigureModel.fillColor = null;

	public Color FigureModel.getFillColor(){
		return fillColor;
	}

	public void FigureModel.setFillColor(Color fillColor){
		this.fillColor = fillColor;
		figureChanged();
	}

	// Apply fill color when drawing
	
	pointcut drawFigure(DiagramView canvas, FigureModel figure, GC gc) :
		target(FigureView+) &&
		call(void draw(DiagramView, FigureModel, GC)) && 
		args(canvas, figure, gc);
	
	pointcut drawOval(GC gc, int x, int y, int w, int h, FigureModel figure) : 
		call (void GC.drawOval(int, int, int, int)) && 
		args(x, y, w, h) && target(gc) &&
		cflow(drawFigure(DiagramView, figure, GC));
		
	void around(GC gc, int x, int y, int w, int h, FigureModel figure) : drawOval(gc, x, y, w, h, figure) {
		if (figure.getFillColor() != null) {
			gc.setBackground(figure.getFillColor());
			gc.fillOval(x, y, w, h);
		}
		proceed(gc, x, y, w, h, figure);
	}

	pointcut drawRectangle(GC gc, int x, int y, int w, int h, FigureModel figure) : 
		call (void GC.drawRectangle(int, int, int, int)) && 
		args(x, y, w, h) && target(gc) &&
		cflow(drawFigure(DiagramView, figure, GC));

	void around(GC gc, int x, int y, int w, int h, FigureModel figure) : drawRectangle(gc, x, y, w, h, figure) {
		if (figure.getFillColor() != null) {
			gc.setBackground(figure.getFillColor());
			gc.fillRectangle(x, y, w, h);
		}
		proceed(gc, x, y, w, h, figure);
	}
		
	// Create Fill Color Editor
	
	private Button colorButton = null;
	private Button noneButton = null;
	
	public void createFillColorControls(final Composite fillComposite) {
		Composite fillColorComposite = new Composite(fillComposite, SWT.NONE);
		fillColorComposite.setLayout(new GridLayout(3, false));

		GridData gd_label = new GridData(); 
		gd_label.widthHint = 30;
		
		Label label = new Label(fillColorComposite, SWT.NONE);
		label.setText("Color");
		label.setLayoutData(gd_label);

		colorButton = new Button(fillColorComposite, SWT.PUSH);
		colorButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		colorButton.setImage(new Image(Display.getCurrent(), "icons/null.gif"));

		noneButton = new Button(fillColorComposite, SWT.PUSH);
		noneButton.setImage(new Image(Display.getCurrent(), "icons/null.gif"));

		colorButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ColorDialog dialog = new ColorDialog(fillComposite.getShell());
				dialog.open();
			    Color color = new Color(Display.getCurrent(), dialog.getRGB());
				Set<FigureModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (FigureModel figure : selected) {
					figure.setFillColor(color);
				}
				colorButton.setSelection(false);
			}
		});
	}

	// Update Fill Editor
	
	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		Color fillColor = null;
		colorButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		colorButton.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		for (FigureModel figure : diagram.getSelected()) {
			if (fillColor == null) fillColor = figure.getFillColor();
			else if (!fillColor.equals(figure.getFillColor())) return;
		}
		if (fillColor != null) {
			colorButton.setBackground(fillColor);
			colorButton.setImage(new Image(Display.getCurrent(), "icons/square.gif"));
		}
	}	
	
	// Clone fill color
	
	pointcut clone(FigureModel figure) :
		call (FigureModel clone()) && target(figure);

	FigureModel around(FigureModel figure) : clone(figure) {
		FigureModel clone = proceed(figure);
		clone.setFillColor(figure.getFillColor());
		return clone;
	}
}