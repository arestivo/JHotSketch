package com.feup.jhotsketch.copypaste;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Decorations;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;

@PackageName("CopyPaste")
public aspect CopyPasteMenu {
	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(JHotSketch) && if(type==SWT.BAR);

	after(Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
	    MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
	    fileItem.setText("Edit");

	    Menu fileMenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
	    fileItem.setMenu(fileMenu);

	    MenuItem cut = new MenuItem(fileMenu, SWT.PUSH);
	    cut.setText("Cut");
	    cut.setAccelerator(SWT.CONTROL | 'X');
	    cut.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		CopyPaste.getInstance().cut();
			}
		});
	    
	    MenuItem copy = new MenuItem(fileMenu, SWT.PUSH);
	    copy.setText("Copy");
	    copy.setAccelerator(SWT.CONTROL | 'C');
	    copy.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		CopyPaste.getInstance().copy();
			}
		});

	    MenuItem paste = new MenuItem(fileMenu, SWT.PUSH);
	    paste.setText("Paste");
	    paste.setAccelerator(SWT.CONTROL | 'V');
	    paste.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		CopyPaste.getInstance().paste();
			}
		});
	}
}