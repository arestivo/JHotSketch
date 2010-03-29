package com.feup.jhotsketch.file;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;

@PackageName("File")
public aspect FileMenu {

	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(JHotSketch) && if(type==SWT.BAR);

	after(final Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
	    MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
	    fileItem.setText("File");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileItem.setMenu(fileMenu);

	    MenuItem export = new MenuItem(fileMenu, SWT.PUSH);
	    export.setText("Export...");
	    export.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		FileDialog fileDialog = new FileDialog(shell);
	    		fileDialog.setFilterExtensions(new String[] {"*.png"});
	    		fileDialog.setOverwrite(true);
	    		String filename = fileDialog.open();
	    		if (filename == null) return;
	    		Image image = new Image(Display.getCurrent(), 800, 600);
	    		JHotSketch.getInstance().getCurrentView().print(new GC(image));
	    		ImageLoader il = new ImageLoader();
	    		il.data = new ImageData[] {image.getImageData()};
	    		il.save(filename, SWT.IMAGE_PNG);
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
