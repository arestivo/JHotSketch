package com.feup.jhotsketch.connector;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import org.eclipse.swt.graphics.Point;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Connector")
public class ConnectorModel{
	private ShapeModel source;
	private ShapeModel sink;
	
	public enum END {NONE, HOLLOWARROW, FILLEDARROW, SIMPLEARROW, HOLLOWSQUARE, FILLEDSQUARE, HOLLOWCIRCLE, FILLEDCIRCLE};
	private END sourceEnd;
	private END sinkEnd;
	private int endSize = 10;
	private boolean selected = false;
	private Rectangle bounds;
	
	public ConnectorModel(ShapeModel source, ShapeModel sink) {
		this.source = source;
		this.sink = sink;
		this.sourceEnd = END.FILLEDSQUARE;
		this.sinkEnd = END.FILLEDARROW;
	}
	
	public void setSource(ShapeModel source) {
		this.source = source;
	}

	public ShapeModel getSource() {
		return source;
	}

	public void setSink(ShapeModel sink) {
		this.sink = sink;
	}

	public ShapeModel getSink() {
		return sink;
	}

	public void setSourceEnd(END sourceEnd) {
		this.sourceEnd = sourceEnd;
	}

	public END getSourceEnd() {
		return sourceEnd;
	}

	public void setSinkEnd(END sinkEnd) {
		this.sinkEnd = sinkEnd;
	}

	public END getSinkEnd() {
		return sinkEnd;
	}

	public void setEndSize(int endSize) {
		this.endSize = endSize;
	}

	public int getEndSize() {
		return endSize;
	}

	@Override
	public ShapeModel clone() {
		return null;
	}

	public void toggleSelected() {
		this.selected  = !this.selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean contains(int x, int y) {
		Point p1 = source.getCenter();
		Point p2 = sink.getCenter();
		Line2D line = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
		double dist = line.ptLineDist(new Point2D.Double(x, y));
		if (dist < 5 && bounds.contains(x, y)) return true;
		return false;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
}
