package com.feup.jhotsketch.snap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.controller.PointerController;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("Snap")
public privileged aspect Snap {
	private boolean snapToGrid = true;
	
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
		
	after(JHotSketch application) returning(CoolBar coolbar): createCoolbar(application) {
		createSnapToolbar(application, coolbar);
	}

	pointcut mouseUp(PointerController controller) :
		call(void DiagramController+.mouseUp(..)) && target(controller);
		
	before(PointerController controller) : mouseUp(controller) {
		if (!snapToGrid) return;
		if (controller.operation == PointerController.OPERATION.MOVE) {
			for (FigureModel figure : controller.grabbed) {
				snapToGrid(figure);
			}
		}
		controller.diagram.diagramChanged();
	}
	
	private void snapToGrid(FigureModel figure) {
		figure.setX((figure.getX() + 5) / 10 * 10);
		figure.setY((figure.getY() + 5) / 10 * 10);
	}

	public void createSnapToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem grid = new ToolItem(toolbar, SWT.CHECK);
		grid.setImage(new Image(Display.getCurrent(), "icons/snapgrid.gif"));
		grid.setSelection(true);

		ToolItem object = new ToolItem(toolbar, SWT.CHECK);
		object.setImage(new Image(Display.getCurrent(), "icons/snapobject.gif"));
		object.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);	    

		grid.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				snapToGrid = !snapToGrid;
			}
		});
		
	}

}