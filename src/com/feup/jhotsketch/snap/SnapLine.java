package com.feup.jhotsketch.snap;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Snap")
public class SnapLine implements Comparable<SnapLine>{
	public enum KIND {EDGE, CENTER};

	private int position;
	private ShapeModel figure;

	private KIND kind;
	
	public SnapLine(int position, KIND kind, ShapeModel figure) {
		this(position);
		this.figure = figure;
		this.setKind(kind);
	}

	public SnapLine(int position) {
		this.position = position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}

	@Override
	public int compareTo(SnapLine o) {
		return o.getPosition() < getPosition()?1:-1;
	}

	public void setKind(KIND kind) {
		this.kind = kind;
	}

	public KIND getKind() {
		return kind;
	}

	public ShapeModel getFigure() {
		return figure;
	}
}