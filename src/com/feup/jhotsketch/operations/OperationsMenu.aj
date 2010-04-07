package com.feup.jhotsketch.operations;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.Application;

@PackageName("Operations")
public aspect OperationsMenu {
	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(Application) && if(type==SWT.BAR);

	after(final Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
	    fileItem.setText("Operations");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileItem.setMenu(fileMenu);
	}
}