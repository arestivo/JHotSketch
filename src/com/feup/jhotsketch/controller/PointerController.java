package com.feup.jhotsketch.controller;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.FigureModel;

@PackageName("Controller")
public class PointerController extends DiagramController{
	
	public PointerController(DiagramModel diagram) {
		super(diagram);
	}

	public void mouseClick(Event event) {
		if ((event.stateMask & SWT.CTRL) == 0) diagram.unselectAll();
		diagram.toggleSelected(diagram.getFigureAt(event.x, event.y));
	}

	@Override
	public void mouseDrag(Event event1, Event event2) {
		int dx = event2.x - event1.x;
		int dy = event2.y - event1.y;
		for (FigureModel figure : diagram.getSelected()) {
			if (figure.contains(event1.x, event1.y)) {
				Rectangle r = null;
				for (FigureModel f : diagram.getSelected()) {
					if (r == null) r = f.getBounds();
					else r.add(f.getBounds());
				}
				r.x = r.x + dx;	r.y = r.y + dy;
				diagram.setMoveRectangle(r);
				return;
			}
		}

		FigureModel figure = diagram.getFigureAt(event1.x, event1.y);
		if (figure != null) {
			Rectangle r = figure.getBounds();
			r.x = r.x + dx;	r.y = r.y + dy;
			diagram.setMoveRectangle(r);
		}
		else
			diagram.setSelectionRectangle(event1.x, event1.y, event2.x, event2.y);
	}
	
	public void mouseDrop(Event event1, Event event2) {
		for (FigureModel figure : diagram.getSelected()) {
			if (figure.contains(event1.x, event1.y)) {
				dragSelected(diagram.getSelected(), event1, event2);
				diagram.removeMoveRectangle();
				return;
			}
		}
		FigureModel figure = diagram.getFigureAt(event1.x, event1.y);
		if (figure != null) {
			diagram.removeMoveRectangle();
			diagram.unselectAll();
			dragFigure(figure, event1, event2);
			if (!figure.getSelected()) diagram.toggleSelected(figure);
		} else {
			diagram.removeSelectionRectangle();
			if ((event2.stateMask & SWT.SHIFT) == 0) diagram.unselectAll();
			for (FigureModel figure2 : diagram.getFigures()) {
				if(figure2.inside(event1.x, event1.y, event2.x, event2.y)) diagram.setSelect(figure2);
			}
		}
	}

	private void dragSelected(Set<FigureModel> selected, Event event1, Event event2) {
		for (FigureModel figure : selected)
			dragFigure(figure, event1, event2);
	}

	private void dragFigure(FigureModel figure, Event event1, Event event2) {
		if (figure == null) return;
		diagram.moveFigure(figure, event2.x - event1.x, event2.y - event1.y);
	}
}