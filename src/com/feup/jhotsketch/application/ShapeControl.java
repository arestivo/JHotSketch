package com.feup.jhotsketch.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.RectangleModel;
import com.feup.jhotsketch.model.RoundedRectangleModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.view.ShapeView;

@PackageName("Application")
public class ShapeControl extends Composite{
	public static int CONTROLWIDTH = 60;
	public static int CONTROLHEIGHT = 40;
	public static int PADDING = 5;
		
	private ShapeModel model;
	
	public ShapeControl(Composite parent, int style, String type) {
		super(parent, style);
		setLayoutData(new RowData(CONTROLWIDTH, CONTROLHEIGHT));
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				paint(event.gc);
			}
		});

		if (type.equals("OVAL")) model = new OvalModel(PADDING, PADDING, CONTROLWIDTH - PADDING * 2, CONTROLHEIGHT - PADDING * 2);
		if (type.equals("RECTANGLE")) model = new RectangleModel(PADDING, PADDING, CONTROLWIDTH - PADDING * 2, CONTROLHEIGHT - PADDING * 2);
		if (type.equals("ROUNDED")) model = new RoundedRectangleModel(PADDING, PADDING, CONTROLWIDTH - PADDING * 2, CONTROLHEIGHT - PADDING * 2);
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				JHotSketch.getInstance().getCurrentDiagram().addFigure(model.clone());
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {				
			}
		});
	}
	
	private void paint(GC gc) {
		gc.setAntialias(SWT.ON);
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, CONTROLWIDTH, CONTROLHEIGHT);
		gc.drawRectangle(0, 0, CONTROLWIDTH, CONTROLHEIGHT);
		ShapeView.createView(model).draw(model, gc);
	}

}
