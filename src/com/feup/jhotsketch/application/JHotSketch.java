package com.feup.jhotsketch.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.ControllerFactory;
import com.feup.jhotsketch.controller.PointerController;
import com.feup.jhotsketch.controller.PointerControllerFactory;
import com.feup.jhotsketch.model.CircleModel;
import com.feup.jhotsketch.model.SquareModel;
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

		CoolBar coolbar = new CoolBar(shell, SWT.NONE);
		
		createControllerToolbar(coolbar);
		createCopyPasteToolbar(coolbar);

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
	
	private ToolItem createController(ToolBar toolbar, String icon, final ControllerFactory factory) {
		final ToolItem controller = new ToolItem(toolbar, SWT.RADIO);
		controller.setImage(new Image(Display.getCurrent(), "icons/" + icon + ".gif"));
		controller.setData(factory);
		controller.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				dc.setController(factory.createController(dc.getDiagram()));
			}
		});
		return controller;
	}
	
	private void createControllerToolbar(CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		createController(toolbar, "pointer", new PointerControllerFactory()).setSelection(true);
		
		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);
	}
	
	private void createCopyPasteToolbar(CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem copy = new ToolItem(toolbar, SWT.PUSH);
		copy.setImage(new Image(Display.getCurrent(), "icons/copy.gif"));

		ToolItem cut = new ToolItem(toolbar, SWT.PUSH);
		cut.setImage(new Image(Display.getCurrent(), "icons/cut.gif"));

		ToolItem paste = new ToolItem(toolbar, SWT.PUSH);
		paste.setImage(new Image(Display.getCurrent(), "icons/paste.gif"));

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);	    
	}

	private DiagramView createCanvas(final Shell shell) {
		dc = new DiagramView(shell, 0);
		dc.setController(new PointerController(dc.getDiagram()));
		
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
			@Override
			public void handleEvent(Event event) {
				String type = (String) button.getData();
				if (type.equals("SQUARE")) dc.getDiagram().addFigure(new SquareModel(10, 10, 50, 50));
				if (type.equals("CIRCLE")) dc.getDiagram().addFigure(new CircleModel(10, 10, 50, 50));
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
