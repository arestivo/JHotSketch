package com.feup.jhotsketch.copypaste;

import java.util.HashSet; 
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("CopyPaste")
public privileged aspect CopyPaste {
	private Set<FigureModel> clipboard = null;
	
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
		
	after(JHotSketch application) returning(CoolBar coolbar): createCoolbar(application) {
		createCopyPasteToolbar(application, coolbar);
	}

	public void createCopyPasteToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem copy = new ToolItem(toolbar, SWT.PUSH);
		copy.setImage(new Image(Display.getCurrent(), "icons/copy.gif"));
		copy.setSelection(true);

		ToolItem cut = new ToolItem(toolbar, SWT.PUSH);
		cut.setImage(new Image(Display.getCurrent(), "icons/cut.gif"));
		cut.setSelection(true);

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
				DiagramModel diagram = application.getCurrentDiagram();
				clipboard = new HashSet<FigureModel>();
				for (FigureModel figure : diagram.getSelected()) {
					clipboard.add(figure);
				}
				diagram.removeFigures(diagram.getSelected());
				diagram.diagramChanged();
			}
		});

		copy.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				DiagramModel diagram = application.getCurrentDiagram();
				clipboard = new HashSet<FigureModel>();
				for (FigureModel figure : diagram.getSelected()) {
					clipboard.add(figure.clone());
				}
			}
		});
		
		paste.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (clipboard == null) return;
				DiagramModel diagram = application.getCurrentDiagram();
				for (FigureModel figure : clipboard) {
					diagram.addFigure((FigureModel) figure.clone());
				}
				diagram.diagramChanged();
			}
		});
	}
}