package com.feup.jhotsketch.connector;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Connector")
public class ConnectorModel {
	private ShapeModel source;
	private ShapeModel sink;

	public ConnectorModel(ShapeModel source, ShapeModel sink) {
		this.source = source;
		this.sink = sink;
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

}
