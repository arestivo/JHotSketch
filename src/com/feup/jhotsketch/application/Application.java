package com.feup.jhotsketch.application;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.viewer.Viewer;

@PackageName("Application")
public class Application {

	private static Application instance = null;
	
	private CTabFolder diagramFolder;
	private Set<ApplicationObserver	> observers = new HashSet<ApplicationObserver>(); 
	private ShapeCreatorPanel scp;
	
	public void run() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
	
		GridLayout layout = new GridLayout(3, false);
		shell.setLayout(layout);

		createMenu(shell);

		createLeftPanel(shell);
		createCenterPanel(shell);
		createRightPanel(shell);
		
		newDiagram();
		
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

	public void newDiagram() {
		CTabItem item = new CTabItem(diagramFolder, SWT.CLOSE);
		item.setText("Untitled");

		Viewer editor = new Viewer(diagramFolder, SWT.NONE);
		item.setControl(editor);
		
		diagramFolder.setSelection(item);
		selectedDiagramChanged();
	}

	private void createRightPanel(Shell shell) {
		PropertyPanel propertyPanel = new PropertyPanel(shell, SWT.NONE);
		addApplicationObserver(propertyPanel);
		propertyPanel.pack();

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.grabExcessHorizontalSpace = false;
		gd.widthHint = 250;
		propertyPanel.setLayoutData(gd);
	}

	private void addApplicationObserver(PropertyPanel observer) {
		observers.add(observer);
	}

	private void createCenterPanel(Shell shell) {
		diagramFolder = new CTabFolder(shell, SWT.NONE);
		
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.grabExcessHorizontalSpace = true;
		diagramFolder.setLayoutData(gd);
		diagramFolder.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedDiagramChanged();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void selectedDiagramChanged() {
		for (ApplicationObserver observer : observers) {
			observer.diagramSelected(getActiveEditor());
		}
	}

	private void createLeftPanel(Shell shell) {
		scp = new ShapeCreatorPanel(shell, SWT.BORDER);
		
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.grabExcessHorizontalSpace = false;
		gd.widthHint = 200;
		scp.setLayoutData(gd);
	}
	
	public static void main(String argv[]) {
		Application application = getInstance();
		application.run();
	}
	
	public static Application getInstance() {
		if (instance == null) instance = new Application();
		return instance;
	}

	public Viewer getActiveEditor() {
		return (Viewer) diagramFolder.getSelection().getControl();
	}

	public DiagramController getActiveController() {
		return ((Viewer) diagramFolder.getSelection().getControl()).getController();
	}

	public ShapeCreatorPanel getShapeCreatorPanel() {
		return scp;
	}
}