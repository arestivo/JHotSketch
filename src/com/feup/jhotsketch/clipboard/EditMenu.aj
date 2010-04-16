package com.feup.jhotsketch.clipboard;

import java.util.HashMap;
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
				cut();
			}
		});

	    MenuItem copy = new MenuItem(fileMenu, SWT.PUSH);
	    copy.setText("Copy");
	    copy.setAccelerator(SWT.CONTROL | 'C');
	    copy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				copy();
			}

		});
	    
	    MenuItem paste = new MenuItem(fileMenu, SWT.PUSH);
	    paste.setText("Paste");
	    paste.setAccelerator(SWT.CONTROL | 'V');
	    paste.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		paste();
	    	}
		});

	}

	private void cut() {
		DiagramController controller = Application.getInstance().getActiveController();				
		copy();
		controller.removeSelected();
	}
	
	private void copy() {
		DiagramController controller = Application.getInstance().getActiveController();				
		shapeClipboard.clear(); connectorClipboard.clear();
		HashMap<Shape, Shape> clones = new HashMap<Shape, Shape>();
		for (Shape shape : controller.getSelectedShapes()) {
			Shape clone = shape.clone();
			clones.put(shape, clone);
			shapeClipboard.add(clone);
		}
		for (Connector connector : controller.getSelectedConnectors()) {
			Shape source = clones.get(connector.getSource());
			Shape target = clones.get(connector.getTarget());
			if (source == null) source = connector.getSource();
			if (target == null) target = connector.getTarget();

			Connector clone = connector.clone();
			clone.setSource(source);
			clone.setTarget(target);
			
			connectorClipboard.add(clone);
		}
	}
	
	private void paste() {
		DiagramController controller = Application.getInstance().getActiveController();
		HashMap<Shape, Shape> clones = new HashMap<Shape, Shape>();
		Set<Connector> ncontrollers = new HashSet<Connector>();
		for (Shape shape : shapeClipboard) {
			shape.move(20, 20);
			Shape clone = shape.clone();
			clones.put(shape, clone);
			controller.addShape(clone);
		}
		for (Connector connector : connectorClipboard) {
			Shape source = clones.get(connector.getSource());
			Shape target = clones.get(connector.getTarget());
			if (source == null) source = connector.getSource();
			if (target == null) target = connector.getTarget();

			Connector clone = connector.clone();
			clone.setSource(source);
			clone.setTarget(target);
			controller.addConnector(clone);
			ncontrollers.add(clone);
		}
		controller.selectMany(clones.values(), ncontrollers);
	}

}