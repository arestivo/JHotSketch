package com.feup.jhotsketch.properties.line.width;

import java.util.List; 
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.FigureView;

@PackageName("Line.Width")
public aspect LineWidth {

	// Add line width properties to Figure
	
	private int FigureModel.lineWidth = 1;

	public int FigureModel.getLineWidth(){
		return lineWidth;
	}

	public void FigureModel.setLineWidth(int lineWidth){
		this.lineWidth = lineWidth;
		figureChanged();
	}

	// Apply line properties when drawing
	
	pointcut drawFigure(DiagramView canvas, FigureModel figure, GC gc) :
		target(FigureView+) &&
		call(void draw(DiagramView, FigureModel, GC)) && 
		args(canvas, figure, gc);

	before(DiagramView canvas, FigureModel figure, GC gc) : drawFigure(canvas, figure, gc) {
		gc.setLineWidth(figure.getLineWidth());
	}
	
	// Create Line Width Editor
	
	private Scale scale = null;
	
	public void createLineWidthControls(Composite lineComposite) {
		Composite lineWidthComposite = new Composite(lineComposite, SWT.NONE);
		lineWidthComposite.setLayout(new GridLayout(2, false));
		
		GridData gd_fill = new GridData(); 
		gd_fill.grabExcessHorizontalSpace = true;
		gd_fill.horizontalAlignment = SWT.FILL;
		gd_fill.widthHint = 200;

		GridData gd_label = new GridData(); 
		gd_label.widthHint = 30;
		
		Label label = new Label(lineWidthComposite, SWT.NONE);
		label.setText("Width");
		label.setLayoutData(gd_label);

		scale = new Scale(lineWidthComposite, SWT.HORIZONTAL);
		scale.setMinimum(1);
		scale.setMaximum(20);
		scale.setLayoutData(gd_fill);
		
		scale.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<FigureModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				int width = scale.getSelection();
				for (FigureModel figure : selected) {
					figure.setLineWidth(width);
				}
			}
		});

	}

	// Update Line Editor
	
	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		int lineWidth = -1;
		for (FigureModel figure : diagram.getSelected()) {
			if (lineWidth == -1) lineWidth = figure.getLineWidth();
			else if (lineWidth != figure.getLineWidth()) {
				scale.setSelection(1);
				return;
			}
		}
		scale.setSelection(lineWidth);
	}
	
	// Clone line width
	
	pointcut clone(FigureModel figure) :
		call (FigureModel clone()) && target(figure);

	FigureModel around(FigureModel figure) : clone(figure) {
		FigureModel clone = proceed(figure);
		clone.setLineWidth(figure.getLineWidth());
		return clone;
	}
}
