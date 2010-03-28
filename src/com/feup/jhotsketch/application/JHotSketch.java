package com.feup.jhotsketch.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.RectangleModel;
import com.feup.jhotsketch.model.RoundedRectangleModel;
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
		shell.setBounds(0, 0, 1200, 600);
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
		gd.widthHint = 200;
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

	private void createTool(Composite composite, String icon, String data) {		
		final Button button = new Button(composite, SWT.FLAT);
		button.setImage(new Image(Display.getCurrent(), "icons/" + icon + ".gif"));
		button.setLayoutData(new RowData(30, 30));
		button.setData(data);

		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String type = (String) button.getData();
				if (type.equals("SQUARE")) dc.getDiagram().addFigure(new RectangleModel(10, 10, 50, 50));
				if (type.equals("CIRCLE")) dc.getDiagram().addFigure(new OvalModel(10, 10, 50, 50));
				if (type.equals("ROUNDED")) dc.getDiagram().addFigure(new RoundedRectangleModel(10, 10, 50, 50));
			}
		});
	}
	
	private void createBasicTools(ExpandBar bar) {
		Composite composite = new Composite (bar, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.pack = false;
		composite.setLayout(layout);
		
		createTool(composite, "square", "SQUARE");
		createTool(composite, "circle", "CIRCLE");
		createTool(composite, "rounded", "ROUNDED");
				
		ExpandItem item = new ExpandItem (bar, SWT.NONE, 0);
		item.setText("Basic");
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
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
