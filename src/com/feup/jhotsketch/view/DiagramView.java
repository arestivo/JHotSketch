package com.feup.jhotsketch.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.DiagramObserver;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("View")
public class DiagramView extends Composite implements DiagramObserver{
	private final DiagramModel diagram;
	private DiagramController controller = null;
	
	private float centerx, centery, zoom;
	
	public DiagramView(Composite parent, int style) {
		super(parent,style|SWT.BORDER);

		diagram = new DiagramModel();
		diagram.addObserver(this);
						
		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				paint(event.gc);
			}
		});
		
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseMove, listener);
	}
	
	private void paint(GC gc) {
		gc.setAntialias(SWT.ON);
		for (FigureModel figure : diagram.getFigures()) {
			FigureView.createView(figure).draw(this, figure, gc);
		}
		gc.setLineStyle(SWT.LINE_DOT);
		gc.setLineWidth(1);
		if (diagram.getSelectionRectangle() != null) 
			gc.drawRectangle(diagram.getSelectionRectangle());
		if (diagram.getMoveRectangle() != null) 
			gc.drawRectangle(diagram.getMoveRectangle());
	}

	public DiagramModel getDiagram() {
		return diagram;
	}

	public void setCenterx(float centerx) {
		this.centerx = centerx;
	}

	public float getCenterx() {
		return centerx;
	}

	public void setCentery(float centery) {
		this.centery = centery;
	}

	public float getCentery() {
		return centery;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public float getZoom() {
		return zoom;
	}

	@Override
	public void diagramChanged(DiagramModel diagram) {
		redraw();
	}

	Listener listener = new Listener() {
		private Event lastEvent = new Event();
		private long lastEventTime = 0;
		
		@Override
		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.MouseDown:
				lastEventTime = System.currentTimeMillis();
				lastEvent = event;
				break;
			case SWT.MouseUp:
				long time = System.currentTimeMillis();

				if (lastEvent.type == SWT.MouseDown && time - lastEventTime < 300 && !distanced(event, lastEvent)) 
					controller.mouseClick(event);
				else if (lastEvent.type == SWT.MouseDown && distanced(event, lastEvent)) 
					controller.mouseDrop(lastEvent, event);

				lastEventTime = System.currentTimeMillis();
				lastEvent = event;
				break;
			case SWT.MouseMove:
				if (lastEvent.type == SWT.MouseDown && distanced(event, lastEvent)) 
					controller.mouseDrag(lastEvent, event);
				break;
			}
		}
	};

	protected boolean distanced(Event event1, Event event2) {
		return (event1.x != event2.x || event1.y != event2.y);
	}

	public void setController(DiagramController controller) {
		this.controller = controller;
	}
}