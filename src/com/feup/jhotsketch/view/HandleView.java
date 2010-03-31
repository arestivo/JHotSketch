package com.feup.jhotsketch.view;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.Handle;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("View")
public class HandleView {

	public static void drawHandles(GC gc, ShapeModel shape) {
		if (shape.getSelected()) {
			for (Handle handle : shape.getHandles()) {
				drawHandle(gc, handle);
			}
		}
	}

	public static void drawHandle(GC gc, Handle handle) {
		gc.setBackground(handle.getColor());
		if (handle.getKind().equals("SQUARE")) gc.fillRectangle(handle.getBounds());
		if (handle.getKind().equals("TRIANGLE")) {
			int pa[] = {handle.getBounds().x + Handle.getSize() / 2, handle.getBounds().y, 
					    handle.getBounds().x, handle.getBounds().y + Handle.getSize(), 
					    handle.getBounds().x + Handle.getSize(), handle.getBounds().y + Handle.getSize()};
			gc.fillPolygon(pa);					
		}
		if (handle.getKind().equals("RHOMBUS")) {
			int pa[] = {handle.getBounds().x + Handle.getSize() / 2, handle.getBounds().y, 
					    handle.getBounds().x + Handle.getSize(), handle.getBounds().y + Handle.getSize() / 2, 
					    handle.getBounds().x + Handle.getSize() / 2, handle.getBounds().y + Handle.getSize(),
					    handle.getBounds().x, handle.getBounds().y + Handle.getSize() / 2};
			gc.fillPolygon(pa);					
		}
	}
}