package com.feup.jhotsketch.snap;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Snap")
public class SnapLine implements Comparable<SnapLine>{
	public enum KIND {EDGE, CENTER};

	private int position;
	
	private Shape shape;

	private Connector connector;
	private int connectorPointPosition;

	private KIND kind;
	
	public SnapLine(int position, KIND kind, Shape shape) {
		this.position = position;
		this.shape = shape;
		this.kind = kind;
	}

	public SnapLine(int position, KIND kind, Connector connector, int connectorPointPosition) {
		this.position = position;
		this.connector = connector;
		this.kind = kind;
		this.connectorPointPosition = connectorPointPosition;
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

	public Connector getConnector() {
		return connector;
	}

	public int getConnectorPointPosition() {
		return connectorPointPosition;
	}
}