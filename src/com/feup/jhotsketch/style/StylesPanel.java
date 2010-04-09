package com.feup.jhotsketch.style;

import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;
import com.feup.jhotsketch.application.ApplicationObserver;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.shape.RectangleShape;
import com.feup.jhotsketch.shape.Shape;
import com.feup.jhotsketch.viewer.Viewer;

@PackageName("Style")
public class StylesPanel extends Composite{

	List<Shape> shapes = new LinkedList<Shape>();
	Shape selectedStyle = null;
	Group shapeGroup;
	
	public StylesPanel(Composite composite, int style) {
		super(composite, SWT.NONE);
		setLayout(new GridLayout(1, true));
		
		shapeGroup = new Group(this, SWT.NONE);
		shapeGroup.setText("Shape Styles");
		
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;

		setLayoutData(gd);
		shapeGroup.setLayoutData(gd);
		
		shapeGroup.addPaintListener(new PaintListener() {			
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setAntialias(SWT.ON);
				paintShapes(e.gc);
			}
		});

		Application.getInstance().addApplicationObserver(new ApplicationObserver() {
			@Override
			public void diagramSelected(Viewer viewer) {
				update(viewer.getDiagram());
			}
		});
		
		shapeGroup.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Shape style = getShape(e.x, e.y);
				if (style == null) return;
				DiagramController controller = Application.getInstance().getActiveController();
				Diagram diagram = controller.getDiagram();

				if (new Ellipse2D.Double(style.getBounds().x + style.getBounds().width - 12, style.getBounds().y + 4, 8, 8).contains(e.x, e.y)) {
					for (Shape shape : controller.getSelectedShapes()) 
						shape.copyProperties(style);
					update(diagram);
				} else { 
					controller.clearSelection();
					for (Shape shape : diagram.getShapes()) 
						if (shape.compareStyles(style)) controller.selectShape(shape);
				}
			}
		});
		
		pack();
	}

	private Shape getShape(int x, int y) {
		for (Shape shape : shapes) if (shape.contains(x, y)) return shape;
		return null;
	}
	
	protected void paintShapes(GC gc) {
		for (Shape shape : shapes) {
			shape.paint(gc);
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
			gc.setLineStyle(SWT.LINE_SOLID);
			gc.setLineWidth(1);
			gc.fillOval(shape.getBounds().x + shape.getBounds().width - 12, shape.getBounds().y + 4, 8, 8);
			gc.drawOval(shape.getBounds().x + shape.getBounds().width - 12, shape.getBounds().y + 4, 8, 8);
		}
	}

	public void update(Diagram diagram) {
		shapes.clear();
		int c = 0, l = 0;
		int width = shapeGroup.getBounds().width;
		int swidth = (width - 50) / 4; 
		int sheight = swidth * 3 / 4; 
		
		for (Shape shape : diagram.getShapes()) {
			boolean found = false;
			for (Shape style : shapes) {
				if (style.compareStyles(shape)) found = true;
			}
			if (!found) {
				RectangleShape rect = new RectangleShape(c * swidth + (c + 1) * 10, l * sheight + (l + 1) * 10 + 10 , swidth, sheight);
				rect.copyProperties(shape);
				shapes.add(rect);
				c++; if (c == 4) {c = 0; l++;}
			}
		}
		if (selectedStyle == null && shapes.size() > 0) selectedStyle = shapes.get(0);
		boolean found = false;
		for (Shape style : shapes) {
			if (style.compareStyles(selectedStyle)) found = true;
		}
		if (!found && shapes.size() > 0) selectedStyle = shapes.get(0);
		shapeGroup.redraw();
	}	
}