package com.feup.jhotsketch.snap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.controller.MoveController;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.preferences.PreferencesMenu;

@PackageName("Snap")
public aspect Snap {
	private boolean snapToObject = true;
	
	pointcut menuCreated() :
		call (Menu.new(..)) && within(PreferencesMenu);
	
	after() returning (Menu menu) : menuCreated() {
	    MenuItem snap = new MenuItem(menu, SWT.CHECK);
	    snap.setSelection(true);
	    snap.setText("Snap to Object");
	    snap.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				snapToObject = !snapToObject;
			}
		});
	}

	pointcut newDiagram() :
		call (Diagram.new(..));
	
	after() returning (Diagram diagram) : newDiagram() {
		SnapController.getInstance().createSnapLines(diagram);
	}

	pointcut diagramChanged(Diagram diagram) :
		execution(void Diagram.diagramChanged(..)) && this(diagram) && !cflow(mouseMove(DiagramController));
	
	after(Diagram diagram) : diagramChanged(diagram) {
		SnapController.getInstance().diagramChanged(diagram);
	}
		
	pointcut mouseMove(DiagramController controller) :
		execution(void DiagramController.mouseMove(..)) && this(controller);
		
	after(DiagramController controller) : mouseMove(controller) {
		if (controller.getCurrentController() instanceof MoveController)
			SnapController.getInstance().mouseMove(controller);
	}

	pointcut mouseUp(DiagramController controller) :
		execution(void DiagramController.mouseUp(..)) && this(controller);
		
	after(DiagramController controller) : mouseUp(controller) {
		SnapController.getInstance().mouseUp(controller);
	}
	
	pointcut diagramPaint(Diagram diagram, GC gc) :
		call (void Diagram.paint(GC)) && args(gc) && target(diagram);

	after(Diagram diagram, GC gc) : diagramPaint(diagram, gc) {
		SnapController.getInstance().paint(diagram, gc);
	}
}