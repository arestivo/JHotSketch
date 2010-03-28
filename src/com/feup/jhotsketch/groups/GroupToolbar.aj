package com.feup.jhotsketch.groups;

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

@PackageName("Groups")
public aspect GroupToolbar {
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
	
	after(JHotSketch application) returning(CoolBar coolbar) : createCoolbar(application) {
		createGroupToolbar(application, coolbar);
	}
	
	private void createGroupToolbar(JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem group = new ToolItem(toolbar, SWT.PUSH);
		group.setImage(new Image(Display.getCurrent(), "icons/group.gif"));
		group.setSelection(true);

		ToolItem ungroup = new ToolItem(toolbar, SWT.PUSH);
		ungroup.setImage(new Image(Display.getCurrent(), "icons/ungroup.gif"));
		ungroup.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);	    

		group.addListener(SWT.Selection, new Listener() {			
			@Override
			public void handleEvent(Event event) {
				GroupController.getInstance().group();
			}
		});
		
		ungroup.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				GroupController.getInstance().ungroup();
			}
		});
	}
}
