package com.feup.jhotsketch.clipboard;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Clipboard")
public aspect EditMenu {
	protected Set<Shape> shapeClipboard = new HashSet<Shape>();
	protected Set<Connector> connectorClipboard = new HashSet<Connector>();
	
	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(Application) && if(type==SWT.BAR);

	after(final Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
	    fileItem.setText("Edit");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileItem.setMenu(fileMenu);

	    MenuItem select = new MenuItem(fileMenu, SWT.PUSH);
	    select.setText("Select All");
	    select.setAccelerator(SWT.CONTROL | 'A');
	    select.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Application.getInstance().getActiveController().selectAll();
			}
		});

	    MenuItem cut = new MenuItem(fileMenu, SWT.PUSH);
	    cut.setText("Cut");
	    cut.setAccelerator(SWT.CONTROL | 'X');
	    cut.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				DiagramController controller = Application.getInstance().getActiveController();				
				shapeClipboard.clear(); connectorClipboard.clear();
				for (Shape shape : controller.getSelectedShapes()) {
					shapeClipboard.add(shape);
				}
				for (Connector connector : controller.getSelectedConnectors()) {
					connectorClipboard.add(connector);
				}
				controller.removeSelected();
			}
		});

	    MenuItem copy = new MenuItem(fileMenu, SWT.PUSH);
	    copy.setText("Copy");
	    copy.setAccelerator(SWT.CONTROL | 'C');
	    copy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
			}
		});

	    
	    MenuItem paste = new MenuItem(fileMenu, SWT.PUSH);
	    paste.setText("Paste");
	    paste.setAccelerator(SWT.CONTROL | 'V');
	    paste.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    	}
		});

	}
}