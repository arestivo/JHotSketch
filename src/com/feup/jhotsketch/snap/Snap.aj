package com.feup.jhotsketch.snap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;
import com.feup.jhotsketch.view.DiagramView;

@PackageName("Snap")
public aspect Snap {
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
	
	pointcut mouseUp(DiagramController controller) :
		call(void DiagramController+.mouseUp(..)) && target(controller);
		
	after(DiagramController controller) : mouseUp(controller) {
		if (!SnapController.getInstance().snapToObject()) return;
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
			SnapController.getInstance().snapVertical(diagram, x1, x2);
			SnapController.getInstance().snapHorizontal(diagram, y1, y2);
			diagram.diagramChanged();
		}
	}

	pointcut mouseMove(DiagramController controller) :
		call(void DiagramController+.mouseMove(..)) && target(controller);
		
	after(DiagramController controller) : mouseMove(controller) {
		if (!SnapController.getInstance().snapToObject()) return;
		if (controller.getOperation() != DiagramController.OPERATION.MOVE) return;
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
			diagram.setVerticalSnapLine(SnapController.getInstance().getBestSnapVertical(diagram, x1, x2));
			diagram.setHorizontalSnapLine(SnapController.getInstance().getBestSnapHorizontal(diagram, y1, y2));
		}
	}
	
	pointcut createDiagram() :
		call (DiagramModel.new());
	
	after() returning (DiagramModel diagram) : createDiagram() {
		SnapController.getInstance().createSnapLines(diagram);
	}
	
	pointcut drawDiagram(DiagramView view, GC gc) :
		call(void DiagramView.paint(GC)) && target(view) && args(gc);
	
	after(DiagramView view, GC gc) : drawDiagram(view, gc) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
		if (SnapController.getInstance().snapToObject()) {
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
		SnapController.getInstance().createSnapLines(diagram);
		for (FigureModel figure : diagram.getFigures()) {
			SnapController.getInstance().addVerticalSnapLine(diagram, figure.getBounds().x, SnapLine.KIND.EDGE, figure);
			SnapController.getInstance().addHorizontalSnapLine(diagram, figure.getBounds().y, SnapLine.KIND.EDGE, figure);
			SnapController.getInstance().addVerticalSnapLine(diagram, figure.getBounds().x + figure.getBounds().width, SnapLine.KIND.EDGE, figure);
			SnapController.getInstance().addHorizontalSnapLine(diagram, figure.getBounds().y + figure.getBounds().height, SnapLine.KIND.EDGE, figure);
			SnapController.getInstance().addVerticalSnapLine(diagram, figure.getBounds().x + figure.getBounds().width / 2, SnapLine.KIND.CENTER, figure);
			SnapController.getInstance().addHorizontalSnapLine(diagram, figure.getBounds().y + figure.getBounds().height / 2, SnapLine.KIND.CENTER, figure);
		}
	}
}