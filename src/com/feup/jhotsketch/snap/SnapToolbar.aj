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

import com.feup.jhotsketch.application.JHotSketch;

public aspect SnapToolbar {
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
		
	after(JHotSketch application) returning(CoolBar coolbar): createCoolbar(application) {
		createSnapToolbar(application, coolbar);
	}
	public void createSnapToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem object = new ToolItem(toolbar, SWT.CHECK);
		object.setImage(new Image(Display.getCurrent(), "icons/snapobject.gif"));
		object.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);	    

		object.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				SnapController.getInstance().toggleSnapToObject();
				application.getCurrentDiagram().diagramChanged();
			}
		});
	}
}
