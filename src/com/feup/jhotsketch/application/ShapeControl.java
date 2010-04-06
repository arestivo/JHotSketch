package com.feup.jhotsketch.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Application")
public class ShapeControl extends Canvas{
	private Shape shape;
	private ShapeControl clone;
	
	public ShapeControl(Composite parent, int style) {
		super(parent, style);

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				paint(e.gc);
			}
		});

		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				Canvas canvas = (Canvas) e.widget;
				Point d = canvas.toDisplay(e.x, e.y);
				Point c = Application.getInstance().getActiveEditor().toControl(d.x, d.y);
				if (clone != null) {
					clone.setLocation(Application.getInstance().getShapeCreatorPanel().getLocation().x + getLocation().x + e.x - canvas.getSize().x / 2, Application.getInstance().getShapeCreatorPanel().getLocation().y + canvas.getLocation().y + e.y - canvas.getSize().y / 2);
					if (c.x >= 0 && c.y >= 0) {
						Shape s = shape.clone();
						s.move(c.x - s.getBounds().width / 2 - 10, c.y - s.getBounds().height / 2 - 10);
						Application.getInstance().getActiveController().addShape(s);
						clone.dispose();
						clone = null;
						Application.getInstance().getActiveController().forceMoveSelected(c.x, c.y);
					}
				} else if (c.x >= 0 && c.y >= 0) Application.getInstance().getActiveController().mouseMove(c.x, c.y);
			}
		});
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
				Canvas canvas = (Canvas) e.widget;
				Point d = canvas.toDisplay(e.x, e.y);
				Point c = Application.getInstance().getActiveEditor().toControl(d.x, d.y);
				if (clone != null) {
					if (c.x >= 0 && c.y >= 0) {
						Shape s = shape.clone();
						s.move(c.x - s.getBounds().width / 2 - 10, c.y - s.getBounds().height / 2 - 10);
						Application.getInstance().getActiveController().addShape(s);
					}
					clone.dispose();
					clone = null;
				} else if (c.x >= 0 && c.y >= 0) Application.getInstance().getActiveController().mouseUp(c.x, c.y, 0);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				clone = new ShapeControl(Display.getCurrent().getActiveShell(), SWT.NONE);
				clone.setShape(shape);
				clone.setBounds(e.x, e.y, 60, 50);
				Canvas canvas = (Canvas) e.widget;
				clone.setLocation(Application.getInstance().getShapeCreatorPanel().getLocation().x + getLocation().x + e.x - canvas.getSize().x / 2, Application.getInstance().getShapeCreatorPanel().getLocation().y + canvas.getLocation().y + e.y - canvas.getSize().y / 2);
				clone.moveAbove(Application.getInstance().getShapeCreatorPanel());
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}

	private void paint(GC gc) {
		gc.setAntialias(SWT.ON);
		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, getSize().x, getSize().y);
		shape.paint(gc);
	}

	public void setShape(Shape shape) {
		this.shape = shape;
		shape.setLineColor(new Color(Display.getCurrent(), new RGB(30, 144, 255)));
		shape.setFillColor(new Color(Display.getCurrent(), new RGB(230, 230, 250)));
	}
}
