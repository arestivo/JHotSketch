package com.feup.jhotsketch.properties.text;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.file.OpenSaveDiagram;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Text")
public aspect TextProperties {
	private String ShapeModel.text = "";
	
	public void ShapeModel.setText(String text) {
		this.text = text;
	}
	
	public String ShapeModel.getText() {
		return text;
	}
	
	pointcut drawFigure(ShapeModel shape, GC gc) :
		target(ShapeView+) &&
		call(void draw(ShapeModel, GC)) && 
		args(shape, gc);

	after(ShapeModel shape, GC gc) : drawFigure(shape, gc) {
		if (!shape.getText().equals("")) {
			Point size = gc.textExtent(shape.getText());
			gc.drawText(shape.getText(), shape.getBounds().x + shape.getBounds().width / 2 - size.x / 2, shape.getBounds().y + shape.getBounds().height / 2 - size.y / 2 , true);
		}
	}
	
	pointcut mouseDoubleClick(DiagramController controller, Event event) :
		call(void DiagramController.mouseDoubleClick(Event)) && target(controller) && args(event);
	
	after(DiagramController controller, Event event) : mouseDoubleClick(controller, event) {
		List<ShapeModel> selected = controller.getDiagram().getSelected();
		if (selected.size() != 1) return;
		for (ShapeModel shape : selected) {
			final Text editor = new Text(JHotSketch.getInstance().getCurrentView(), SWT.SINGLE);
			int width = Math.max(shape.getBounds().width, 100);
			editor.setSize(width, 20);
			editor.setLocation(shape.getBounds().x + shape.getBounds().width / 2 - width / 2, shape.getBounds().y + shape.getBounds().height / 2 - 10);
			editor.setData(shape);
			editor.setText(shape.getText());
			editor.setFocus();
			
			final MouseListener mlistener = new MouseListener() {
				@Override
				public void mouseUp(MouseEvent e) {
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					ShapeModel shape = (ShapeModel) editor.getData();
					shape.setText(editor.getText());
					JHotSketch.getInstance().getCurrentView().removeMouseListener(this);
					editor.dispose();
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
			};
			
			JHotSketch.getInstance().getCurrentView().addMouseListener(mlistener);
			
			editor.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.keyCode == 13) {
						ShapeModel shape = (ShapeModel) editor.getData();
						shape.setText(editor.getText());
						JHotSketch.getInstance().getCurrentView().removeMouseListener(mlistener);
						editor.dispose();
					}
				}
			});
		}
	}
	
	// Clone text
	
	pointcut clone(ShapeModel shape) :
		call (ShapeModel clone()) && target(shape);

	after(ShapeModel shape) returning (ShapeModel clone): clone(shape) {
		clone.setText(shape.getText());
	}

	// Save shape text
	
	pointcut getXMLNode(ShapeModel shape) :
		call(Element OpenSaveDiagram.getXMLNode(.., ShapeModel)) && args(.., shape);
	
	Element around(ShapeModel shape) : getXMLNode(shape) {
		Element e = proceed(shape);
		e.setAttribute("text", "" + shape.getText());
		return e;
	}	

	// Load shape text
	
	pointcut createShapeFromNode(Node node) :
		call (ShapeModel OpenSaveDiagram.createShapeFromNode(.., Node)) && args(.., node);
	
	after(Node node) returning (ShapeModel shape) : createShapeFromNode(node) {
		if (node.getAttributes().getNamedItem("text")!=null) {
			String text = node.getAttributes().getNamedItem("text").getNodeValue();
			shape.setText(text);
		}
	}

	
}