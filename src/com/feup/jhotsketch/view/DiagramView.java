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
import com.feup.jhotsketch.model.Handle;
import com.feup.jhotsketch.model.ShapeModel;

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
		addListener(SWT.MouseDoubleClick, listener);
	}
	
	private void paint(GC gc) {
		gc.setAntialias(SWT.ON);
		for (ShapeModel shape : diagram.getFigures()) {
			ShapeView.createView(shape).draw(shape, gc);
		}
		for (ShapeModel shape : diagram.getFigures()) {
			drawHandles(gc, shape);
		}
		gc.setLineStyle(SWT.LINE_DOT);
		gc.setLineWidth(1);
		if (diagram.getSelectionRectangle() != null) 
			gc.drawRectangle(diagram.getSelectionRectangle());
	}

	private void drawHandles(GC gc, ShapeModel shape) {
		if (shape.getSelected()) {
			for (Handle handle : shape.getHandles()) {
				gc.setBackground(handle.getColor());
				if (handle.getKind().equals("SQUARE")) gc.fillRectangle(handle.getBounds());
				if (handle.getKind().equals("TRIANGLE")) {
					int pa[] = {handle.getBounds().x + Handle.getSize() / 2, handle.getBounds().y, 
							    handle.getBounds().x, handle.getBounds().y + Handle.getSize(), 
							    handle.getBounds().x + Handle.getSize(), handle.getBounds().y + Handle.getSize()};
					gc.fillPolygon(pa);					
				}
				if (handle.getKind().equals("RHOMBUS")) {
					int pa[] = {handle.getBounds().x + Handle.getSize() / 2, handle.getBounds().y, 
							    handle.getBounds().x + Handle.getSize(), handle.getBounds().y + Handle.getSize() / 2, 
							    handle.getBounds().x + Handle.getSize() / 2, handle.getBounds().y + Handle.getSize(),
							    handle.getBounds().x, handle.getBounds().y + Handle.getSize() / 2};
					gc.fillPolygon(pa);					
				}
			}
		}
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

	public void diagramChanged(DiagramModel diagram) {
		redraw();
	}

	Listener listener = new Listener() {		
		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.MouseDown:
				controller.mouseDown(event);
				break;
			case SWT.MouseUp:
				controller.mouseUp(event);
				break;
			case SWT.MouseMove:
				controller.mouseMove(event);
				break;
			case SWT.MouseDoubleClick:
				controller.mouseDoubleClick(event);
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