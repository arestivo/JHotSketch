package com.feup.jhotsketch.persistency;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;

@PackageName("Persistency")
public aspect FileMenu {
	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(Application) && if(type==SWT.BAR);

	after(final Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
	    fileItem.setText("File");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileItem.setMenu(fileMenu);

	    MenuItem newd = new MenuItem(fileMenu, SWT.PUSH);
	    newd.setText("New");
	    newd.setAccelerator(SWT.CONTROL | 'N');
	    newd.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Application.getInstance().newDiagram();
			}
		});

	    MenuItem open = new MenuItem(fileMenu, SWT.PUSH);
	    open.setText("Open...");
	    open.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
			}
		});

	    MenuItem save = new MenuItem(fileMenu, SWT.PUSH);
	    save.setText("Save...");
	    save.setAccelerator(SWT.CONTROL | 'S');
	    save.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
			}
		});

	    
	    MenuItem export = new MenuItem(fileMenu, SWT.PUSH);
	    export.setText("Export...");
	    export.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    	}
		});

	    MenuItem exit = new MenuItem(fileMenu, SWT.PUSH);
	    exit.setText("Exit");
	    exit.setAccelerator(SWT.CONTROL | 'Q');
	    exit.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		System.exit(0);
			}

		});
	}
}