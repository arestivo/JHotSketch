package com.feup.jhotsketch.snap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.model.DiagramModel;

@PackageName("Snap")
public aspect Snap {
	private boolean snapToGrid = true;
	
	pointcut createCopyPaste(JHotSketch application, CoolBar coolbar) :
		target(application) &&
		call(void createCopyPasteToolbar(CoolBar)) && 
		args(coolbar);
	
	after(JHotSketch application, CoolBar coolbar) : createCopyPaste(application, coolbar) {
		createSnapToolbar(application, coolbar);
	}

	pointcut setPoint(int v) :
		this(DiagramModel) &&
		target(FigureModel+) &&
		(call(void setX(int)) || call(void setY(int))) &&
		args(v);

	pointcut setMoveRectangle(Rectangle r) :
		target(DiagramModel) &&
		call(void setMoveRectangle(Rectangle)) &&
		args(r);
	
	void around(int x) : setPoint(x) {
		if (snapToGrid)
			proceed((x + 5) / 10 * 10);
		else
			proceed(x);
	}

	void around(Rectangle r) : setMoveRectangle(r) {
		if (snapToGrid) {
			r.x = (r.x + 5) / 10 * 10;
			r.y = (r.y + 5) / 10 * 10;
		}
		proceed(r);
	}

	
	public void createSnapToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem grid = new ToolItem(toolbar, SWT.CHECK);
		grid.setImage(new Image(Display.getCurrent(), "icons/snapgrid.gif"));
		grid.setSelection(true);

		ToolItem object = new ToolItem(toolbar, SWT.CHECK);
		object.setImage(new Image(Display.getCurrent(), "icons/snapobject.gif"));
		object.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);	    

		grid.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				snapToGrid = !snapToGrid;
			}
		});
		
	}

}