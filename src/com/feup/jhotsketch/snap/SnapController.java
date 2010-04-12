package com.feup.jhotsketch.snap;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.controller.HandleController;
import com.feup.jhotsketch.controller.MoveController;
import com.feup.jhotsketch.diagram.Diagram;
import com.feup.jhotsketch.handle.ConnectorPointHandle;
import com.feup.jhotsketch.handle.Handle;
import com.feup.jhotsketch.shape.Shape;
import com.feup.jhotsketch.snap.SnapLine.KIND;

@PackageName("Snap")
public class SnapController {
	private static int SNAPDISTANCE = 5;
	private static SnapController instance;

	private HashMap<Diagram, TreeSet<SnapLine>> vosnaps = new HashMap<Diagram, TreeSet<SnapLine>>();
	private HashMap<Diagram, TreeSet<SnapLine>> hosnaps = new HashMap<Diagram, TreeSet<SnapLine>>();

	private SnapLine bestv;
	private SnapLine besth;

	public static SnapController getInstance() {
		if (instance == null) instance = new SnapController();
		return instance;
	}
	
	public void mouseMove(DiagramController controller) {
		if (controller.getCurrentController() instanceof MoveController) {
			Set<Shape> selected = controller.getSelectedShapes();
			if (selected.size() == 0) return;
	
			Rectangle rectangle = null;
			for (Shape shape : selected) {
				if (rectangle == null) rectangle = shape.getBounds();
				else rectangle = rectangle.union(shape.getBounds());
			}
			bestv = getBestVerticalSnapline(controller, rectangle, selected, null, 0);
			besth = getBestHorizontalSnapline(controller, rectangle, selected, null, 0);
		}
		if (controller.getCurrentController() instanceof HandleController) {
			HandleController hc = (HandleController) controller.getCurrentController();
			Handle handle = hc.getHandle();
			if (handle instanceof ConnectorPointHandle) {
				Point2D point = ((ConnectorPointHandle) handle).getConnector().getPoints().get(((ConnectorPointHandle) handle).getPosition());
				bestv = getBestVerticalSnapline(controller, new Rectangle((int)point.getX(), (int)point.getY(), 0, 0), new HashSet<Shape>(), ((ConnectorPointHandle) handle).getConnector(), ((ConnectorPointHandle) handle).getPosition());
				besth = getBestHorizontalSnapline(controller, new Rectangle((int)point.getX(), (int)point.getY(), 0, 0), new HashSet<Shape>(), ((ConnectorPointHandle) handle).getConnector(), ((ConnectorPointHandle) handle).getPosition());
			}
		}
		
	}

	private SnapLine getBestHorizontalSnapline(DiagramController controller, Rectangle rectangle, Set<Shape> selectedS, Connector selectedC, int position) {
		TreeSet<SnapLine> snaps = hosnaps.get(controller.getDiagram());
		snaps.subSet(new SnapLine(rectangle.y - SNAPDISTANCE), new SnapLine(rectangle.y + rectangle.height + SNAPDISTANCE));

		int bestDistance = SNAPDISTANCE + 1;
		SnapLine bestSnap = null;
		for (SnapLine snap : snaps) {
			if (selectedS.contains(snap.getShape()) || (selectedC != null && selectedC.equals(snap.getConnector())) && position == snap.getConnectorPointPosition()) continue;
			if (snap.getKind() == KIND.CENTER) {
				int distance = Math.abs(snap.getPosition() - rectangle.y - rectangle.height / 2);
				if (distance <= SNAPDISTANCE && distance < bestDistance) {bestDistance = distance; bestSnap = snap;}
			}
		}
		
		return bestSnap;
	}

	private SnapLine getBestVerticalSnapline(DiagramController controller, Rectangle rectangle, Set<Shape> selectedS, Connector selectedC, int position) {
		TreeSet<SnapLine> snaps = vosnaps.get(controller.getDiagram());
		snaps.subSet(new SnapLine(rectangle.x - SNAPDISTANCE), new SnapLine(rectangle.x + rectangle.width + SNAPDISTANCE));
		
		int bestDistance = SNAPDISTANCE + 1;
		SnapLine bestSnap = null;
		for (SnapLine snap : snaps) {
			if (selectedS.contains(snap.getShape()) || (selectedC != null && selectedC.equals(snap.getConnector())) && position == snap.getConnectorPointPosition()) continue;
			if (snap.getKind() == KIND.CENTER) {
				int distance = Math.abs(snap.getPosition() - rectangle.x - rectangle.width / 2);
				if (distance <= SNAPDISTANCE && distance < bestDistance) {bestDistance = distance; bestSnap = snap;}
			}
		}
		
		return bestSnap;
	}

	public void diagramChanged(Diagram diagram) {
		createSnapLines(diagram);
		updateSnapLines(diagram);
	}

	private void updateSnapLines(Diagram diagram) {
		for (Shape shape : diagram.getShapes()) {
			addVerticalSnapLine(diagram, shape.getBounds().x + shape.getBounds().width / 2, SnapLine.KIND.CENTER, shape);
			addHorizontalSnapLine(diagram, shape.getBounds().y + shape.getBounds().height / 2, SnapLine.KIND.CENTER, shape);
			addVerticalSnapLine(diagram, shape.getBounds().x, SnapLine.KIND.EDGE, shape);
			addHorizontalSnapLine(diagram, shape.getBounds().y, SnapLine.KIND.EDGE, shape);
			addVerticalSnapLine(diagram, shape.getBounds().x + shape.getBounds().width, SnapLine.KIND.EDGE, shape);
			addHorizontalSnapLine(diagram, shape.getBounds().y + shape.getBounds().height, SnapLine.KIND.EDGE, shape);
		}
		for (Connector connector : diagram.getConnectors()) {
			int position = 0;
			for (Point2D point : connector.getPoints()) {
				addVerticalSnapLine(diagram, (int)point.getX(), KIND.CENTER, connector, position);				
				addHorizontalSnapLine(diagram, (int)point.getY(), KIND.CENTER, connector, position++);				
			}
		}
	}

	private void addHorizontalSnapLine(Diagram diagram, int i, KIND kind, Shape shape) {
		hosnaps.get(diagram).add(new SnapLine(i, kind, shape));
	}

	private void addVerticalSnapLine(Diagram diagram, int i, KIND kind, Shape shape) {
		vosnaps.get(diagram).add(new SnapLine(i, kind, shape));
	}

	private void addHorizontalSnapLine(Diagram diagram, int i, KIND kind, Connector connector, int cpp) {
		hosnaps.get(diagram).add(new SnapLine(i, kind, connector, cpp));
	}

	private void addVerticalSnapLine(Diagram diagram, int i, KIND kind, Connector connector, int cpp) {
		vosnaps.get(diagram).add(new SnapLine(i, kind, connector, cpp));
	}

	void createSnapLines(Diagram diagram) {
		vosnaps.put(diagram, new TreeSet<SnapLine>());
		hosnaps.put(diagram, new TreeSet<SnapLine>());
	}

	public void paint(Diagram diagram, GC gc) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		gc.setLineStyle(SWT.LINE_SOLID);
		gc.setLineWidth(1);
		gc.setAlpha(255);
		if (bestv != null)
			gc.drawLine(bestv.getPosition(), 0, bestv.getPosition(), 2000);
		if (besth != null)
			gc.drawLine(0, besth.getPosition(), 2000, besth.getPosition());
	}
	
	public void mouseUp(DiagramController controller) {
		if (controller.getCurrentController() instanceof MoveController) {
			Set<Shape> selected = controller.getSelectedShapes();
	
			if (selected.size() > 0) {
				Rectangle rectangle = null;
				for (Shape shape : selected) {
					if (rectangle == null) rectangle = shape.getBounds();
					else rectangle = rectangle.union(shape.getBounds());
				}
				if (bestv != null) {
					int distv = bestv.getPosition() - rectangle.x - rectangle.width / 2;
					controller.moveSelected(distv, 0);
				}
				if (besth != null) {
					int disth = besth.getPosition() - rectangle.y - rectangle.height / 2;
					controller.moveSelected(0, disth);
				}
				diagramChanged(controller.getDiagram());
			}
		}
		if (controller.getCurrentController() instanceof HandleController) {
			HandleController hc = (HandleController) controller.getCurrentController();
			Handle handle = hc.getHandle();
			if (handle instanceof ConnectorPointHandle) {
				ConnectorPointHandle cph = (ConnectorPointHandle) handle;
				if (bestv != null) cph.getConnector().setPointX(cph.getPosition(), bestv.getPosition());
				if (besth != null) cph.getConnector().setPointY(cph.getPosition(), besth.getPosition());
				diagramChanged(controller.getDiagram());
			}
		}
		bestv = null;
		besth = null;
	}
}