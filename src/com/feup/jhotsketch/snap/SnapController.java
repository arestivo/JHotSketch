package com.feup.jhotsketch.snap;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.ShapeModel;
import com.feup.jhotsketch.snap.SnapLine.KIND;

@PackageName("Snap")
public class SnapController {
	private static int SNAPDISTANCE = 5;
	private static SnapController instance;
	private boolean snapToObject = true;

	private HashMap<DiagramModel, TreeSet<SnapLine>> vosnaps = new HashMap<DiagramModel, TreeSet<SnapLine>>();
	private HashMap<DiagramModel, TreeSet<SnapLine>> hosnaps = new HashMap<DiagramModel, TreeSet<SnapLine>>();
	
	SnapLine getBestSnapHorizontal(DiagramModel diagram, int y1, int y2) {
		int bestsnap = SNAPDISTANCE + 1; SnapLine best = null;
		SortedSet<SnapLine> candidates = hosnaps.get(diagram).subSet(new SnapLine(y1 - SNAPDISTANCE), new SnapLine(y2 + SNAPDISTANCE));
		for (SnapLine snapLine : candidates) {
			if (diagram.getSelected().contains(snapLine.getFigure())) continue;
			if (snapLine.getKind() == SnapLine.KIND.CENTER && Math.abs(snapLine.getPosition() - (y1 + (y2 - y1) / 2)) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - (y1 + (y2 - y1) / 2));
				best = snapLine;
			} else
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - y1) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - y1);
				best = snapLine;
			} else
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - y2) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - y2);
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
			if (diagram.getSelected().contains(snapLine.getFigure())) continue;
			if (snapLine.getKind() == SnapLine.KIND.CENTER && Math.abs(snapLine.getPosition() - (x1 + (x2 - x1) / 2)) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - (x1 + (x2 - x1) / 2));
				best = snapLine;
			} else
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - x1) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - x1);
				best = snapLine;
			} else
			if (snapLine.getKind() == SnapLine.KIND.EDGE && Math.abs(snapLine.getPosition() - x2) < bestsnap) {
				bestsnap = Math.abs(snapLine.getPosition() - x2);
				best = snapLine;
			}
		}
		if (bestsnap < SNAPDISTANCE) return best;
		return null;
	}
	
	public void snapHorizontal(DiagramModel diagram, int y1, int y2) {
			SnapLine best = getBestSnapHorizontal(diagram, y1, y2);
		if (best == null) return;
		if (best.getKind() == KIND.EDGE) {
			boolean top = Math.abs(best.getPosition() - y1) < SNAPDISTANCE;
			for (ShapeModel shape : diagram.getSelected())
				if (top) shape.move(0, best.getPosition() - y1);
				else shape.move(0, best.getPosition() - y2);
		} else for (ShapeModel shape : diagram.getSelected())
			shape.move(0, best.getPosition() - (y1 + (y2 - y1) / 2));
		diagram.setHorizontalSnapLine(null);
	}

	public void snapVertical(DiagramModel diagram, int x1, int x2) {
		SnapLine best = getBestSnapVertical(diagram, x1, x2);
		if (best == null) return;
		if (best.getKind() == KIND.EDGE) {
			boolean left = Math.abs(best.getPosition() - x1) < SNAPDISTANCE;
			for (ShapeModel shape : diagram.getSelected())
				if (left) shape.move(best.getPosition() - x1, 0);
				else shape.move(best.getPosition() - x2, 0);
		} else for (ShapeModel shape : diagram.getSelected())
			shape.move(best.getPosition() - (x1 + (x2 - x1) / 2), 0);
		diagram.setVerticalSnapLine(null);
	}

	public static SnapController getInstance() {
		if (instance == null) instance = new SnapController();
		return instance;
	}

	public void createSnapLines(DiagramModel diagram) {
		vosnaps.put(diagram, new TreeSet<SnapLine>());
		hosnaps.put(diagram, new TreeSet<SnapLine>());
	}

	public void addVerticalSnapLine(DiagramModel diagram, int x, KIND kind, ShapeModel shape) {
		vosnaps.get(diagram).add(new SnapLine(x, kind, shape));		
	}

	public void addHorizontalSnapLine(DiagramModel diagram, int y, KIND kind, ShapeModel shape) {
		hosnaps.get(diagram).add(new SnapLine(y, kind, shape));				
	}

	public void toggleSnapToObject() {
		snapToObject = !snapToObject;
	}

	public boolean snapToObject() {
		return snapToObject;
	}
}
