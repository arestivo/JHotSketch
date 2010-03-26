package com.feup.jhotsketch.snap;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
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
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.snap.SnapLine.KIND;
import com.feup.jhotsketch.view.DiagramView;

@PackageName("Snap")
public aspect Snap {
	private boolean snapToObject = true;

	private static int SNAPDISTANCE = 5;
	
	private HashMap<DiagramModel, TreeSet<SnapLine>> vosnaps = new HashMap<DiagramModel, TreeSet<SnapLine>>();
	private HashMap<DiagramModel, TreeSet<SnapLine>> hosnaps = new HashMap<DiagramModel, TreeSet<SnapLine>>();
	
	private SnapLine DiagramModel.verticalSnapLine = null;
	private SnapLine DiagramModel.horizontalSnapLine = null;
	public void DiagramModel.setVerticalSnapLine(SnapLine line) {
		verticalSnapLine = line;
	}
	public SnapLine DiagramModel.getVerticalSnapLine() {
		return verticalSnapLine;
	}
	public void DiagramModel.setHorizontalSnapLine(SnapLine line) {
		horizontalSnapLine = line;
	}
	public SnapLine DiagramModel.getHorizontalSnapLine() {
		return horizontalSnapLine;
	}
	
	pointcut createCoolbar(JHotSketch application) :
		this(application) &&
		call(CoolBar.new(..));
		
	after(JHotSketch application) returning(CoolBar coolbar): createCoolbar(application) {
		createSnapToolbar(application, coolbar);
	}

	pointcut mouseUp(DiagramController controller) :
		call(void DiagramController+.mouseUp(..)) && target(controller);
		
	after(DiagramController controller) : mouseUp(controller) {
		DiagramModel diagram = controller.getDiagram();
		int x1 = 100000; int y1 = 1000000; int x2 = 0; int y2 = 0; boolean found = false;
		for (FigureModel figure : diagram.getSelected()) {
			x1 = Math.min(x1, figure.getBounds().x);
			y1 = Math.min(y1, figure.getBounds().y);
			x2 = Math.max(x2, figure.getBounds().x + figure.getBounds().width);
			y2 = Math.max(y2, figure.getBounds().y + figure.getBounds().height);
			found = true;
		}
		if (found) {
			snapVertical(diagram, x1, x2);
			snapHorizontal(diagram, y1, y2);
			diagram.diagramChanged();
		}
	}

	pointcut mouseMove(DiagramController controller) :
		call(void DiagramController+.mouseMove(..)) && target(controller);
		
	after(DiagramController controller) : mouseMove(controller) {
		DiagramModel diagram = controller.getDiagram();
		int x1 = 100000; int y1 = 1000000; int x2 = 0; int y2 = 0; boolean found = false;
		for (FigureModel figure : diagram.getSelected()) {
			x1 = Math.min(x1, figure.getBounds().x);
			y1 = Math.min(y1, figure.getBounds().y);
			x2 = Math.max(x2, figure.getBounds().x + figure.getBounds().width);
			y2 = Math.max(y2, figure.getBounds().y + figure.getBounds().height);
			found = true;
		}
		if (found) {
			diagram.setVerticalSnapLine(getBestSnapVertical(diagram, x1, x2));
			diagram.setHorizontalSnapLine(getBestSnapHorizontal(diagram, y1, y2));
		}
	}
	
	SnapLine getBestSnapHorizontal(DiagramModel diagram, int y1, int y2) {
		int bestsnap = SNAPDISTANCE + 1; SnapLine best = null;
		SortedSet<SnapLine> candidates = hosnaps.get(diagram).subSet(new SnapLine(y1 - SNAPDISTANCE), new SnapLine(y2 + SNAPDISTANCE));
		for (SnapLine snapLine : candidates) {
			if (snapLine.getPosition() == y1) continue;
			if (snapLine.getPosition() == y2) continue;
			if (snapLine.getPosition() == y1 + (y2 - y1) / 2) continue;
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - y1) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - y1);
				best = snapLine;
			}
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - y2) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - y2);
				best = snapLine;
			}
			if (snapLine.getKind() == SnapLine.KIND.CENTER && Math.abs(snapLine.getPosition() - (y1 + (y2 - y1) / 2)) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - (y1 + (y2 - y1) / 2));
				best = snapLine;
			}
		}
		if (bestsnap < SNAPDISTANCE) return best;
		return null;
	}

	SnapLine getBestSnapVertical(DiagramModel diagram, int x1, int x2) {
		int bestsnap = SNAPDISTANCE + 1; SnapLine best = null;
		SortedSet<SnapLine> candidates = vosnaps.get(diagram).subSet(new SnapLine(x1 - SNAPDISTANCE), new SnapLine(x2 + SNAPDISTANCE));
		for (SnapLine snapLine : candidates) {
			if (snapLine.getPosition() == x1) continue;
			if (snapLine.getPosition() == x2) continue;
			if (snapLine.getPosition() == x1 + (x2 - x1) / 2) continue;
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - x1) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - x1);
				best = snapLine;
			}
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - x2) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - x2);
				best = snapLine;
			}
			if (snapLine.getKind() == SnapLine.KIND.CENTER && Math.abs(snapLine.getPosition() - (x1 + (x2 - x1) / 2)) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - (x1 + (x2 - x1) / 2));
				best = snapLine;
			}
		}
		if (bestsnap < SNAPDISTANCE) return best;
		return null;
	}
	
	private void snapHorizontal(DiagramModel diagram, int y1, int y2) {
		SnapLine best = getBestSnapHorizontal(diagram, y1, y2);
		if (best == null) return;
		if (best.getKind() == KIND.EDGE) {
			boolean top = Math.abs(best.getPosition() - y1) < SNAPDISTANCE;
			for (FigureModel figure : diagram.getSelected())
				if (top) figure.move(0, best.getPosition() - y1);
				else figure.move(0, best.getPosition() - y2);
		} else for (FigureModel figure : diagram.getSelected())
			figure.move(0, best.getPosition() - (y1 + (y2 - y1) / 2));
		diagram.setHorizontalSnapLine(null);
	}

	private void snapVertical(DiagramModel diagram, int x1, int x2) {
		SnapLine best = getBestSnapVertical(diagram, x1, x2);
		if (best == null) return;
		if (best.getKind() == KIND.EDGE) {
			boolean left = Math.abs(best.getPosition() - x1) < SNAPDISTANCE;
			for (FigureModel figure : diagram.getSelected())
				if (left) figure.move(best.getPosition() - x1, 0);
				else figure.move(best.getPosition() - x2, 0);
		} else for (FigureModel figure : diagram.getSelected())
			figure.move(best.getPosition() - (x1 + (x2 - x1) / 2), 0);
		diagram.setVerticalSnapLine(null);
	}

	public void createSnapToolbar(final JHotSketch application, CoolBar coolbar) {
		ToolBar toolbar = new ToolBar(coolbar, SWT.FLAT);

		ToolItem object = new ToolItem(toolbar, SWT.CHECK);
		object.setImage(new Image(Display.getCurrent(), "icons/snapobject.gif"));
		object.setSelection(true);

		toolbar.pack();
		
	    Point size = toolbar.getSize();

	    CoolItem cool = new CoolItem(coolbar, SWT.NONE);
		cool.setControl(toolbar);
		
	    Point preferred = cool.computeSize(size.x, size.y);
		cool.setPreferredSize(preferred);	    

		object.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				snapToObject = !snapToObject;
				application.getCurrentDiagram().diagramChanged();
			}
		});
	}

	pointcut createDiagram() :
		call (DiagramModel.new());
	
	after() returning (DiagramModel diagram) : createDiagram() {
		vosnaps.put(diagram, new TreeSet<SnapLine>());
		hosnaps.put(diagram, new TreeSet<SnapLine>());
	}
	
	pointcut drawDiagram(DiagramView view, GC gc) :
		call(void DiagramView.paint(GC)) && target(view) && args(gc);
	
	after(DiagramView view, GC gc) : drawDiagram(view, gc) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
		if (snapToObject) {
			SnapLine snapv = view.getDiagram().getVerticalSnapLine();
			if (snapv!=null) gc.drawLine(snapv.getPosition(), 0, snapv.getPosition(), 2000);
			SnapLine snaph = view.getDiagram().getHorizontalSnapLine();
			if (snaph!=null) gc.drawLine(0, snaph.getPosition(), 2000, snaph.getPosition());
		}
	}
	
	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		vosnaps.put(diagram, new TreeSet<SnapLine>());
		hosnaps.put(diagram, new TreeSet<SnapLine>());
		for (FigureModel figure : diagram.getFigures()) {
			vosnaps.get(diagram).add(new SnapLine(figure.getBounds().x, SnapLine.KIND.EDGE));
			hosnaps.get(diagram).add(new SnapLine(figure.getBounds().y, SnapLine.KIND.EDGE));
			vosnaps.get(diagram).add(new SnapLine(figure.getBounds().x + figure.getBounds().width, SnapLine.KIND.EDGE));
			hosnaps.get(diagram).add(new SnapLine(figure.getBounds().y + figure.getBounds().height, SnapLine.KIND.EDGE));
			vosnaps.get(diagram).add(new SnapLine(figure.getBounds().x + figure.getBounds().width / 2, SnapLine.KIND.CENTER));
			hosnaps.get(diagram).add(new SnapLine(figure.getBounds().y + figure.getBounds().height / 2, SnapLine.KIND.CENTER));
		}
	}
}