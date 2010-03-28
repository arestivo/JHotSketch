package com.feup.jhotsketch.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;

@PackageName("Groups")
public aspect GroupMenu {
	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(JHotSketch) && if(type==SWT.BAR);

	after(Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
	    MenuItem groupItem = new MenuItem(menu, SWT.CASCADE);
	    groupItem.setText("Groups");

	    Menu groupMenu = new Menu(shell, SWT.DROP_DOWN);
	    groupItem.setMenu(groupMenu);

	    MenuItem group = new MenuItem(groupMenu, SWT.PUSH);
	    group.setText("Group");
	    group.setAccelerator(SWT.CONTROL | 'G');
	    group.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		GroupController.getInstance().group();
			}

		});
	    
	    MenuItem ungroup = new MenuItem(groupMenu, SWT.PUSH);
	    ungroup.setText("Ungroup");
	    ungroup.setAccelerator(SWT.CONTROL | 'U');
	    ungroup.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		GroupController.getInstance().ungroup();
			}
		});
	}
}