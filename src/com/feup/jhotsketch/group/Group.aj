package com.feup.jhotsketch.group;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.operations.OperationsMenu;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Group")
public aspect Group {

	pointcut menuCreated() :
		call (Menu.new(..)) && within(OperationsMenu);

	after() returning(Menu menu) : menuCreated(){
	    MenuItem group = new MenuItem(menu, SWT.PUSH);
	    group.setText("Group");
	    group.setAccelerator(SWT.CONTROL | 'G');
	    group.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				GroupShape group = new GroupShape();
				Set<Shape> shapes = Application.getInstance().getActiveController().getSelectedShapes();
				Set<Connector> connectors = Application.getInstance().getActiveController().getSelectedConnectors();
				
				group.addShapes(shapes);
				group.addConnectors(connectors);

				Application.getInstance().getActiveController().removeSelected();
				
				Application.getInstance().getActiveController().addShape(group);
			}
		});

	    MenuItem ungroup = new MenuItem(menu, SWT.PUSH);
	    ungroup.setText("Ungroup");
	    ungroup.setAccelerator(SWT.CONTROL | 'U');
	    ungroup.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
			}
		});
	}
	
}
