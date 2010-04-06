package com.feup.jhotsketch.snap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.preferences.PreferencesMenu;

@PackageName("Snap")
public aspect Snap {
	private boolean snapToObject = true;
	
	pointcut menuCreated() :
		call (Menu.new(..)) && within(PreferencesMenu);
	
	after() returning (Menu menu) : menuCreated() {
	    MenuItem snap = new MenuItem(menu, SWT.CHECK);
	    snap.setSelection(true);
	    snap.setText("Snap to Object");
	    snap.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				snapToObject = !snapToObject;
			}
		});
	}
}
