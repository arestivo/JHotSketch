package com.feup.jhotsketch.properties.shape;

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
public aspect ShapeLineStyle{

	// Add line style properties to Figure
	
	private int ShapeModel.lineStyle = SWT.LINE_SOLID;

	public int ShapeModel.getLineStyle(){
		return lineStyle;
	}

	public void ShapeModel.setLineStyle(int lineStyle){
		this.lineStyle = lineStyle;
		shapeChanged();
	}

	// Apply line style when drawing
	
	pointcut drawFigure(ShapeModel shape, GC gc) :
		target(ShapeView+) &&
		call(void draw(ShapeModel, GC)) && 
		args(shape, gc);

	before(ShapeModel shape, GC gc) : drawFigure(shape, gc) {
		gc.setLineStyle(shape.getLineStyle());
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
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ShapeModel shape : selected) {
					shape.setLineStyle(SWT.LINE_SOLID);
				}
			}
		});

		dashedButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ShapeModel shape : selected) {
					shape.setLineStyle(SWT.LINE_DASH);
				}
			}
		});

		dottedButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				List<ShapeModel> selected = JHotSketch.getInstance().getCurrentView().getDiagram().getSelected();
				if (selected.size() == 0) ((Button)event.widget).setSelection(false);
				for (ShapeModel shape : selected) {
					shape.setLineStyle(SWT.LINE_DOT);
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
		for (ShapeModel shape : diagram.getSelected()) {
			if (lineStyle == -1) lineStyle = shape.getLineStyle();
			else if (lineStyle != shape.getLineStyle()) return;
		}
		if (lineStyle == SWT.LINE_SOLID) solidButton.setSelection(true);
		if (lineStyle == SWT.LINE_DASH) dashedButton.setSelection(true);
		if (lineStyle == SWT.LINE_DOT) dottedButton.setSelection(true);
	}	

	// Clone line style
	
	pointcut clone(ShapeModel shape) :
		call (ShapeModel clone()) && target(shape);

	after(ShapeModel shape) returning (ShapeModel clone): clone(shape) {
		if (!(clone instanceof GroupModel))
		clone.setLineStyle(shape.getLineStyle());
	}

	// Save line style
	
	pointcut getXMLNode(ShapeModel shape) :
		call(Element OpenSaveDiagram.getXMLNode(.., ShapeModel)) && args(.., shape);
	
	Element around(ShapeModel shape) : getXMLNode(shape) {
		Element e = proceed(shape);
		e.setAttribute("linestyle", "" + shape.getLineStyle());
		return e;
	}	
	
	// Load line style
	
	pointcut createShapeFromNode(Node node) :
		call (ShapeModel OpenSaveDiagram.createShapeFromNode(.., Node)) && args(.., node);
	
	after(Node node) returning (ShapeModel shape) : createShapeFromNode(node) {
		if (node.getAttributes().getNamedItem("linestyle")!=null) {
			int style = new Integer(node.getAttributes().getNamedItem("linestyle").getNodeValue()).intValue();
			shape.setLineStyle(style);
		}
	}

}