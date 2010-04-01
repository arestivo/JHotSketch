package com.feup.jhotsketch.properties.shape;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.file.OpenSaveDiagram;
import com.feup.jhotsketch.groups.GroupModel;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Properties.Shape")
public aspect ShapeFillColor{

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
	
	pointcut drawFigure(ShapeModel shape, GC gc) :
		target(ShapeView+) &&
		call(void draw(ShapeModel, GC)) && 
		args(shape, gc);
	
	pointcut drawOval(GC gc, int x, int y, int w, int h, ShapeModel shape) : 
		call (void GC.drawOval(int, int, int, int)) && 
		args(x, y, w, h) && target(gc) &&
		cflow(drawFigure(shape, GC));
		
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
		cflow(drawFigure(shape, GC));

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
		cflow(drawFigure(shape, GC));

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
		gd_label.widthHint = 60;
		
		Label label = new Label(fillColorComposite, SWT.NONE);
		label.setText("Fill Color");
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

	after(ShapeModel shape) returning (ShapeModel clone): clone(shape) {
		if (!(clone instanceof GroupModel))
		clone.setFillColor(shape.getFillColor());
	}

	// Save fill color
	
	pointcut getXMLNode(ShapeModel shape) :
		call(Element OpenSaveDiagram.getXMLNode(.., ShapeModel)) && args(.., shape);
	
	Element around(ShapeModel shape) : getXMLNode(shape) {
		Element e = proceed(shape);
		if (shape.getFillColor() == null) return e;
		String color = Integer.toHexString(shape.getFillColor().getRGB().red);
		if (color.length() < 2) color = "0" + color;
		color += Integer.toHexString(shape.getFillColor().getRGB().green);
		if (color.length() < 4) color = "0" + color;
		color += Integer.toHexString(shape.getFillColor().getRGB().blue);
		if (color.length() < 6) color = "0" + color;
		e.setAttribute("fillcolor", color);
		return e;
	}
	
	// Load fill color
	
	pointcut createShapeFromNode(Node node) :
		call (ShapeModel OpenSaveDiagram.createShapeFromNode(.., Node)) && args(.., node);
	
	after(Node node) returning (ShapeModel shape) : createShapeFromNode(node) {
		if (node.getAttributes().getNamedItem("fillcolor")!=null) {
			String hex = node.getAttributes().getNamedItem("fillcolor").getNodeValue();
			int r = Integer.parseInt(hex.substring(0, 2), 16);
			int g = Integer.parseInt(hex.substring(2, 4), 16);
			int b = Integer.parseInt(hex.substring(4, 6), 16);
			Color color = new Color(Display.getCurrent(), new RGB(r, g, b));
			shape.setFillColor(color);
		}
	}

}