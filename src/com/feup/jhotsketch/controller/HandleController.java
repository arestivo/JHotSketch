package com.feup.jhotsketch.controller;

import java.util.List; 

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.handle.CenterHandle;
import com.feup.jhotsketch.handle.ConnectorMiddleHandle;
import com.feup.jhotsketch.handle.ConnectorPointHandle;
import com.feup.jhotsketch.handle.Handle;
import com.feup.jhotsketch.handle.ResizeHandle;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Controller")
public class HandleController implements ShapeController{
	private Handle handle;
	private Point lastPoint;

	private DiagramController controller;

	public HandleController(Handle handle, DiagramController controller) {
		this.handle = handle;
		this.controller = controller;
	}

	@Override
	public void mouseDown(int x, int y) {
		lastPoint = new Point(x, y);		
	}
	
	@Override
	public void mouseMove(int x, int y) {
		if (handle instanceof ResizeHandle) resize(x, y);
		if (handle instanceof CenterHandle) ((CenterHandle)handle).setConnectorLine(x, y);
		if (handle instanceof ConnectorPointHandle) {
			((ConnectorPointHandle) handle).getConnector().movePoint(((ConnectorPointHandle) handle).getPosition(),x,y);
		}
		lastPoint = new Point(x, y);
	}

	private void resize(int x, int y) {
		ResizeHandle rhandle = (ResizeHandle) handle;
		int dx = x - lastPoint.x;
		int dy = y - lastPoint.y;
		if (rhandle.getId().equals("NW")) {
			rhandle.getShape().move(dx, dy);
			rhandle.getShape().resize(-dx, -dy);
		}
		if (rhandle.getId().equals("NE")) {
			rhandle.getShape().move(0, dy);
			rhandle.getShape().resize(dx, -dy);
		}
		if (rhandle.getId().equals("SW")) {
			rhandle.getShape().move(dx, 0);
			rhandle.getShape().resize(-dx, dy);
		}
		if (rhandle.getId().equals("SE")) {
			rhandle.getShape().resize(dx, dy);
		}
	}

	@Override
	public void mouseUp(int x, int y) {
		if (handle instanceof ResizeHandle) ((ResizeHandle)handle).getShape().correctBounds();
		if (handle instanceof CenterHandle) {
			Shape shape = ((CenterHandle)handle).getShape();
			connect(shape, shape.getConnectorLine());
			((CenterHandle)handle).resetConnectorLine();
		}
	}

	private void connect(Shape source, Point point) {
		if (point == null) return;
		List<Shape> shapes = controller.getDiagram().getShapesAt(point.x, point.y);
		if (shapes.size() > 0) {
			Shape target = shapes.get(0);
			controller.addConnector(new Connector(source, target));
		}
	}

	@Override
	public void paint(GC gc) {
	}

	public void doubleClick(int x, int y) {
		if (handle instanceof ConnectorMiddleHandle) {
			ConnectorMiddleHandle cmh = (ConnectorMiddleHandle) handle;
			cmh.getConnector().addPoint(cmh.getPoint(), cmh.getPosition());
		}
		if (handle instanceof ConnectorPointHandle) {
			ConnectorPointHandle cph = (ConnectorPointHandle) handle;
			cph.getConnector().removePoint(cph.getPosition());
		}
	}
}
