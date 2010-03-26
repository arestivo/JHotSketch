package com.feup.jhotsketch.ordering;

import java.util.LinkedList; 
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

@PackageName("Ordering")
public aspect Ordering {
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
		
	after(JHotSketch application) returning(CoolBar coolbar): createCoolbar(application) {
		createOrderingToolbar(application, coolbar);
	}

	public void createOrderingToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem back = new ToolItem(toolbar, SWT.PUSH);
		back.setImage(new Image(Display.getCurrent(), "icons/back" + ".gif"));
		back.setSelection(true);

		ToolItem front = new ToolItem(toolbar, SWT.PUSH);
		front.setImage(new Image(Display.getCurrent(), "icons/front.gif"));
		front.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);
		
		back.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				back();
			}
		});

		front.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				front();
			}
		});
	}

	private void back() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		LinkedList<FigureModel> selected = new LinkedList<FigureModel>(); selected.addAll(diagram.getSelected());
		diagram.removeFigures(selected);
		diagram.addFiguresAtStart(selected);
		diagram.setSelect(selected);
		diagram.diagramChanged();
	}

	private void front() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		LinkedList<FigureModel> selected = new LinkedList<FigureModel>(); selected.addAll(diagram.getSelected());
		diagram.removeFigures(selected);
		diagram.addFigures(selected);
		diagram.setSelect(selected);
		diagram.diagramChanged();
	}
}