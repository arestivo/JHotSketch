package com.feup.jhotsketch.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Application")
public aspect FileMenu {

	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(JHotSketch) && if(type==SWT.BAR);

	after(Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
	    MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
	    fileItem.setText("File");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileItem.setMenu(fileMenu);

	    MenuItem group = new MenuItem(fileMenu, SWT.PUSH);
	    group.setText("Exit");
	    group.setAccelerator(SWT.CONTROL | 'Q');
	    group.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		System.exit(0);
			}

		});
	}
}
