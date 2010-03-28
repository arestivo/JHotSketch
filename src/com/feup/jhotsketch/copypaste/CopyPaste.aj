package com.feup.jhotsketch.copypaste;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("CopyPaste")
public aspect CopyPaste {
	private Set<FigureModel> clipboard = null;
	
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
		
	after(JHotSketch application) returning(CoolBar coolbar): createCoolbar(application) {
		createCopyPasteToolbar(application, coolbar);
	}

	public void createCopyPasteToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem cut = new ToolItem(toolbar, SWT.PUSH);
		cut.setImage(new Image(Display.getCurrent(), "icons/cut.gif"));
		cut.setSelection(true);

		ToolItem copy = new ToolItem(toolbar, SWT.PUSH);
		copy.setImage(new Image(Display.getCurrent(), "icons/copy.gif"));
		copy.setSelection(true);

		ToolItem paste = new ToolItem(toolbar, SWT.PUSH);
		paste.setImage(new Image(Display.getCurrent(), "icons/paste.gif"));
		paste.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);
		
		cut.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				cut();
			}
		});

		copy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				copy();
			}
		});
		
		paste.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				paste();
			}
		});
	}

	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(JHotSketch) && if(type==SWT.BAR);

	after(Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
	    MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
	    fileItem.setText("Edit");

	    Menu fileMenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
	    fileItem.setMenu(fileMenu);

	    MenuItem cut = new MenuItem(fileMenu, SWT.PUSH);
	    cut.setText("Cut");
	    cut.setAccelerator(SWT.CONTROL | 'X');
	    cut.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		cut();
			}
		});
	    
	    MenuItem copy = new MenuItem(fileMenu, SWT.PUSH);
	    copy.setText("Copy");
	    copy.setAccelerator(SWT.CONTROL | 'C');
	    copy.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		copy();
			}
		});

	    MenuItem paste = new MenuItem(fileMenu, SWT.PUSH);
	    paste.setText("Paste");
	    paste.setAccelerator(SWT.CONTROL | 'V');
	    paste.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		paste();
			}
		});
	}
	
	private void cut() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<FigureModel>();
		for (FigureModel figure : diagram.getSelected()) {
			clipboard.add(figure);
		}
		diagram.removeFigures(diagram.getSelected());
		diagram.diagramChanged();
	}
	
	private void copy() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		clipboard = new HashSet<FigureModel>();
		for (FigureModel figure : diagram.getSelected()) {
			clipboard.add(figure.clone());
		}
	}

	private void paste() {
		if (clipboard == null) return;
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		diagram.unselectAll();
		for (FigureModel figure : clipboard) {
			FigureModel clone = (FigureModel) figure.clone();
			diagram.setSelect(clone);
			diagram.addFigure(clone);
			diagram.moveFigure(clone, 10, 10);
			copy();
		}
		diagram.diagramChanged();
	}
	
}