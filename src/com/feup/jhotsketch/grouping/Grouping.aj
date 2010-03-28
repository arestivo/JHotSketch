package com.feup.jhotsketch.grouping;

import java.util.LinkedList;
import java.util.List;

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
import com.feup.jhotsketch.view.FigureView;

@PackageName("Grouping")
public aspect Grouping {

	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
	
	after(JHotSketch application) returning(CoolBar coolbar) : createCoolbar(application) {
		createGroupToolbar(application, coolbar);
	}
	
	pointcut createView(FigureModel figure) :
		call(FigureView FigureView.createView(FigureModel)) && args(figure);
	
	FigureView around(FigureModel figure) : createView(figure) {
		if (figure instanceof GroupModel) return new GroupView();
		return proceed(figure);
	}
	
	private void createGroupToolbar(JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem group = new ToolItem(toolbar, SWT.PUSH);
		group.setImage(new Image(Display.getCurrent(), "icons/group.gif"));
		group.setSelection(true);

		ToolItem ungroup = new ToolItem(toolbar, SWT.PUSH);
		ungroup.setImage(new Image(Display.getCurrent(), "icons/ungroup.gif"));
		ungroup.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);	    

		group.addListener(SWT.Selection, new Listener() {			
			@Override
			public void handleEvent(Event event) {
				group();
			}
		});
		
		ungroup.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ungroup();
			}
		});
	}
	
	pointcut menuCreated(Shell shell, int type) : 
		call (Menu.new(Decorations, int)) && args(shell, type) && within(JHotSketch) && if(type==SWT.BAR);

	after(Shell shell, int type) returning(Menu menu) : menuCreated(shell, type) {
	    MenuItem groupItem = new MenuItem(menu, SWT.CASCADE);
	    groupItem.setText("Groups");

	    Menu groupMenu = new Menu(shell, SWT.DROP_DOWN);
	    groupItem.setMenu(groupMenu);

	    MenuItem group = new MenuItem(groupMenu, SWT.PUSH);
	    group.setText("Group");
	    group.setAccelerator(SWT.CONTROL | 'G');
	    group.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		group();
			}

		});
	    
	    MenuItem ungroup = new MenuItem(groupMenu, SWT.PUSH);
	    ungroup.setText("Ungroup");
	    ungroup.setAccelerator(SWT.CONTROL | 'U');
	    ungroup.addListener(SWT.Selection, new Listener() {
	    	@Override
			public void handleEvent(Event event) {
	    		ungroup();
			}
		});
	}

	private void group() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		List<FigureModel> selected = diagram.getSelected();
		if (selected.size() <= 1) return;
		for (FigureModel figure : selected) {
			figure.setSelected(false);
		}
		GroupModel group = new GroupModel();
		group.addFigures(selected);
		diagram.removeFigures(selected);
		diagram.addFigure(group);
		diagram.unselectAll();
		diagram.setSelect(group);
	}

	private void ungroup() {
		DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
		List<FigureModel> selected = new LinkedList<FigureModel>(); 
		selected.addAll(diagram.getSelected());
		for (FigureModel figure : selected) {
			if (figure instanceof GroupModel) {
				diagram.removeFigure(figure);
				diagram.addFigures(((GroupModel)figure).getFigures());
				diagram.setSelect(((GroupModel)figure).getFigures());
			}
		}
	}

}