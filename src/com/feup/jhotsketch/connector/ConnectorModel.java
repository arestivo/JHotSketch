package com.feup.jhotsketch.connector;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Connector")
public class ConnectorModel {
	private ShapeModel source;
	private ShapeModel sink;
	
	public enum END {NONE, HOLLOWARROW, FILLEDARROW, SIMPLEARROW, HOLLOWSQUARE, FILLEDSQUARE, HOLLOWCIRCLE, FILLEDCIRCLE};
	private END sourceEnd;
	private END sinkEnd;
	private int endSize = 10;
	
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

}
