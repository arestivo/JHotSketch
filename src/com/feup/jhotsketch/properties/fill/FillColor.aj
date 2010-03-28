package com.feup.jhotsketch.properties.fill;

import java.util.List;

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
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.DiagramView;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Fill.Properties")
public aspect FillColor{

	// Add fill color properties to Figure
	
	private Color ShapeModel.fillColor = null;

	public Color ShapeModel.getFillColor(){
		return fillColor;
	}

	public void ShapeModel.setFillColor(Color fillColor){
		this.fillColor = fillColor;
		shapeChanged();
	}

	// Apply fill color when drawing
	
	pointcut drawFigure(DiagramView canvas, ShapeModel shape, GC gc) :
		target(ShapeView+) &&
		call(void draw(DiagramView, ShapeModel, GC)) && 
		args(canvas, shape, gc);
	
	pointcut drawOval(GC gc, int x, int y, int w, int h, ShapeModel shape) : 
		call (void GC.drawOval(int, int, int, int)) && 
		args(x, y, w, h) && target(gc) &&
		cflow(drawFigure(DiagramView, shape, GC));
		
	void around(GC gc, int x, int y, int w, int h, ShapeModel shape) : drawOval(gc, x, y, w, h, shape) {
		if (shape.getFillColor() != null) {
			gc.setBackground(shape.getFillColor());
			gc.fillOval(x, y, w, h);
		}
		proceed(gc, x, y, w, h, shape);
	}

	pointcut drawRectangle(GC gc, int x, int y, int w, int h, ShapeModel shape) : 
		call (void GC.drawRectangle(int, int, int, int)) && 
		args(x, y, w, h) && target(gc) &&
		cflow(drawFigure(DiagramView, shape, GC));

	void around(GC gc, int x, int y, int w, int h, ShapeModel shape) : drawRectangle(gc, x, y, w, h, shape) {
		if (shape.getFillColor() != null) {
			gc.setBackground(shape.getFillColor());
			gc.fillRectangle(x, y, w, h);
		}
		proceed(gc, x, y, w, h, shape);
	}

	pointcut drawRoundedRectangle(GC gc, int x, int y, int w, int h, int r1, int r2, ShapeModel shape) : 
		call (void GC.drawRoundRectangle(int, int, int, int, int , int)) && 
		args(x, y, w, h, r1, r2) && target(gc) &&
		cflow(drawFigure(DiagramView, shape, GC));

	void around(GC gc, int x, int y, int w, int h, int r1, int r2, ShapeModel shape) : drawRoundedRectangle(gc, x, y, w, h, r1, r2, shape) {
		if (shape.getFillColor() != null) {
			gc.setBackground(shape.getFillColor());
			gc.fillRoundRectangle(x, y, w, h, r1, r2);
		}
		proceed(gc, x, y, w, h, r1, r2, shape);
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
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ShapeModel shape : selected) {
					shape.setFillColor(color);
				}
				colorButton.setSelection(false);
			}
		});
		
		noneButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				for (ShapeModel shape : selected) {
					shape.setFillColor(null);
				}
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
		for (ShapeModel shape : diagram.getSelected()) {
			if (fillColor == null) fillColor = shape.getFillColor();
			else if (!fillColor.equals(shape.getFillColor())) return;
		}
		if (fillColor != null) {
			colorButton.setBackground(fillColor);
			colorButton.setImage(new Image(Display.getCurrent(), "icons/square.gif"));
		}
	}	
	
	// Clone fill color
	
	pointcut clone(ShapeModel shape) :
		call (ShapeModel clone()) && target(shape);

	ShapeModel around(ShapeModel shape) : clone(shape) {
		ShapeModel clone = proceed(shape);
		clone.setFillColor(shape.getFillColor());
		return clone;
	}
}