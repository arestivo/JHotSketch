package com.feup.jhotsketch.snap;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Snap")
public class SnapLine implements Comparable<SnapLine>{
	public enum KIND {EDGE, CENTER};

	private int position;
	private Shape shape;

	private KIND kind;
	
	public SnapLine(int position, KIND kind, Shape shape) {
		this(position);
		this.shape = shape;
		this.kind = kind;
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

	public Shape getShape() {
		return shape;
	}
}