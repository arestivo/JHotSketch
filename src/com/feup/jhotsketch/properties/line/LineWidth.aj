package com.feup.jhotsketch.properties.line;

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
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Line.Properties")
public aspect LineWidth {

	// Add line width properties to Figure
	
	private int ShapeModel.lineWidth = 1;

	public int ShapeModel.getLineWidth(){
		return lineWidth;
	}

	public void ShapeModel.setLineWidth(int lineWidth){
		this.lineWidth = lineWidth;
		shapeChanged();
	}

	// Apply line properties when drawing
	
	pointcut drawFigure(DiagramView canvas, ShapeModel shape, GC gc) :
		target(ShapeView+) &&
		call(void draw(DiagramView, ShapeModel, GC)) && 
		args(canvas, shape, gc);

	before(DiagramView canvas, ShapeModel shape, GC gc) : drawFigure(canvas, shape, gc) {
		gc.setLineWidth(shape.getLineWidth());
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
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				int width = scale.getSelection();
				for (ShapeModel shape : selected) {
					shape.setLineWidth(width);
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
		for (ShapeModel shape : diagram.getSelected()) {
			if (lineWidth == -1) lineWidth = shape.getLineWidth();
			else if (lineWidth != shape.getLineWidth()) {
				scale.setSelection(1);
				return;
			}
		}
		scale.setSelection(lineWidth);
	}
	
	// Clone line width
	
	pointcut clone(ShapeModel shape) :
		call (ShapeModel clone()) && target(shape);

	ShapeModel around(ShapeModel shape) : clone(shape) {
		ShapeModel clone = proceed(shape);
		clone.setLineWidth(shape.getLineWidth());
		return clone;
	}
}