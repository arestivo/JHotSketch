package com.feup.jhotsketch.copypaste;

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

public aspect CopyPasteToolbar {
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
		
	after(JHotSketch application) returning(CoolBar coolbar): createCoolbar(application) {
		createCopyPasteToolbar(application, coolbar);
	}

	public void createCopyPasteToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem cut = new ToolItem(toolbar, SWT.PUSH);
		cut.setImage(new Image(Display.getCurrent(), "icons/cut.gif"));
		cut.setSelection(true);

		ToolItem copy = new ToolItem(toolbar, SWT.PUSH);
		copy.setImage(new Image(Display.getCurrent(), "icons/copy.gif"));
		copy.setSelection(true);

		ToolItem paste = new ToolItem(toolbar, SWT.PUSH);
		paste.setImage(new Image(Display.getCurrent(), "icons/paste.gif"));
		paste.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);
		
		cut.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				CopyPaste.getInstance().cut();
			}
		});

		copy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				CopyPaste.getInstance().copy();
			}
		});
		
		paste.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				CopyPaste.getInstance().paste();
			}
		});
	}
}