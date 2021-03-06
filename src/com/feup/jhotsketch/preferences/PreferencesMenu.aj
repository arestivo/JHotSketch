package com.feup.jhotsketch.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;

@PackageName("Preferences")
public aspect PreferencesMenu {
	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(Application) && if(type==SWT.BAR);

	after(final Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
		MenuItem preferencesItem = new MenuItem(menu, SWT.CASCADE);
	    preferencesItem.setText("Preferences");

	    Menu preferencesMenu = new Menu(shell, SWT.DROP_DOWN);
	    preferencesItem.setMenu(preferencesMenu);
	}
}