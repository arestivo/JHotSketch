package com.feup.jhotsketch.style;

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

	private List<Shape> shapes = new LinkedList<Shape>();
	private Shape selectedStyle = null;
	private Group shapeGroup;
	
	private static StylesPanel instance = null;

	public static StylesPanel getInstance() {
		return instance;
	}
	
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
			private boolean pendingClick = false;

			@Override
			public void mouseUp(MouseEvent e) {
				pendingClick  = true;
				final int x = e.x; final int y = e.y;
				Display.getCurrent().timerExec(Display.getCurrent().getDoubleClickTime() + 3, 
				new Runnable() {
					@Override
					public void run() {
						if (!pendingClick) return;
						pendingClick = false;
						Shape style = getShape(x, y);
						setSelectedStyle(style);
						if (style == null) return;
						DiagramController controller = Application.getInstance().getActiveController();
						Diagram diagram = controller.getDiagram();
						controller.clearSelection();
						for (Shape shape : diagram.getShapes()) 
							if (shape.compareStyles(style)) controller.selectShape(shape);
						update(diagram);
					}
				});
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				pendingClick = false;
				Shape style = getShape(e.x, e.y);
				if (style == null) return;
				DiagramController controller = Application.getInstance().getActiveController();
				Diagram diagram = controller.getDiagram();

				for (Shape shape : controller.getSelectedShapes()) 
					shape.copyProperties(style);
				update(diagram);
			}
		});
		
		pack();
		instance = this;
	}

	private Shape getShape(int x, int y) {
		for (Shape shape : shapes) if (shape.contains(x, y)) return shape;
		return null;
	}
	
	protected void paintShapes(GC gc) {
		for (Shape shape : shapes)
			shape.paint(gc);
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		if (getSelectedStyle() != null)
			gc.fillRectangle(getSelectedStyle().getBounds().x, getSelectedStyle().getBounds().y + getSelectedStyle().getBounds().height + 3, getSelectedStyle().getBounds().width, 3);
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
		if (getSelectedStyle() == null && shapes.size() > 0) setSelectedStyle(shapes.get(0));
		boolean found = false;
		for (Shape style : shapes) {
			if (style.compareStyles(getSelectedStyle())) found = true;
		}
		if (!found && shapes.size() > 0) setSelectedStyle(shapes.get(0));
		shapeGroup.redraw();
	}

	private void setSelectedStyle(Shape selectedStyle) {
		this.selectedStyle = selectedStyle;
	}

	public Shape getSelectedStyle() {
		return selectedStyle;
	}	
}