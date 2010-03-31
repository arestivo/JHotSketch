package com.feup.jhotsketch.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.view.DiagramView;

@PackageName("Application")
public class JHotSketch {
	private DiagramView dc = null;
	private static JHotSketch instance;
	
	private void run() {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		GridLayout layout = new GridLayout(3, false);
		shell.setLayout(layout);

		createMenu(shell);		
		
		CoolBar coolbar = new CoolBar(shell, SWT.NONE);
		
		createExpandBar(shell);

		createCanvas(shell);
		
		createPropertyFolder(shell);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 3;
		gd.heightHint = 40;
		coolbar.setLayoutData(gd);
		
		shell.pack();
		shell.setMaximized(true);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
			display.sleep();
		}
		display.dispose();
	}
	
	private void createMenu(Shell shell) {
		Menu menu = new Menu(shell, SWT.BAR);
	    shell.setMenuBar(menu);
	}

	private void createPropertyFolder(Shell shell) {
		TabFolder folder = new TabFolder(shell, SWT.NONE);
		
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.grabExcessHorizontalSpace = false;
		gd.widthHint = 280;
		folder.setLayoutData(gd);
	}

	public DiagramView getCurrentView() {
		return dc;
	}

	public DiagramModel getCurrentDiagram() {
		return dc.getDiagram();
	}
			
	private DiagramView createCanvas(final Shell shell) {
		dc = new DiagramView(shell, 0);
		dc.setController(new DiagramController(dc.getDiagram()));
		
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		dc.setLayoutData(gd);
		
		return dc;
	}

	private void createExpandBar(final Shell shell) {
		ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);

		createBasicTools(bar);
		createUMLTools(bar);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.grabExcessHorizontalSpace = false;
		gd.widthHint = 200;
		bar.setLayoutData(gd);
	}
	
	private void createBasicTools(ExpandBar bar) {
		Composite composite = new Composite (bar, SWT.NONE);
		RowLayout layout = new RowLayout();
		composite.setLayout(layout);

		new ShapeControl(composite, SWT.NONE, "OVAL");
		new ShapeControl(composite, SWT.NONE, "RECTANGLE");
		new ShapeControl(composite, SWT.NONE, "ROUNDED");

		ExpandItem item = new ExpandItem (bar, SWT.NONE, 0);
		item.setText("Basic");
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y * 1);
		item.setControl(composite);	
	}

	private void createUMLTools(ExpandBar bar) {
		Composite composite = new Composite (bar, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.pack = false;
		composite.setLayout(layout);
			
		ExpandItem item = new ExpandItem (bar, SWT.NONE, 0);
		item.setText("UML");
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);
	}
	
	public static void main(String[] args) {
		JHotSketch.getInstance().run();
	}

	public static JHotSketch getInstance() {
		if (instance == null) instance = new JHotSketch();
		return instance;
	}
}
