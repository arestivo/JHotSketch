package com.feup.jhotsketch.viewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.controller.ControllerObserver;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.diagram.DiagramObserver;

@PackageName("Viewer")
public class Viewer extends Composite implements DiagramObserver, ControllerObserver
{
	private Diagram diagram;
	private DiagramController controller;
	
	public Viewer(Composite parent, int style) {
		super(parent, style | SWT.BORDER);

		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));		

		diagram = new Diagram();
		controller = new DiagramController(getDiagram());

		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				paint(e.gc);
			}
		});
		
		getDiagram().addDiagramObserver(this);
		getController().addControllerObserver(this);
		addMouseListener(getController());
		addMouseMoveListener(getController());
	}

	private void paint(GC gc) {
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		gc.setTextAntialias(SWT.ON);
		getDiagram().paint(gc);
		getController().paint(gc);
	}
	
	public Diagram getDiagram() {
		return diagram;
	}

	@Override
	public void diagramChanged(Diagram diagram) {
		redraw();
	}

	@Override
	public void controllerChanged(DiagramController controller) {
		redraw();
	}

	public DiagramController getController() {
		return controller;
	}
}