package com.feup.jhotsketch.handle;

import java.util.LinkedList;
import java.util.List;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Handle")
public class HandlerFactory {	
	public static List<Handle> getHandlesFor(Shape shape) {
		List<Handle> handles = new LinkedList<Handle>();
		
		handles.addAll(getResizeHandles(shape));
		
		return handles;
	}

	private static List<Handle> getResizeHandles(Shape shape) {
		List<Handle> handles = new LinkedList<Handle>();
		handles.add(new ResizeHandle(shape, "NW"));
		handles.add(new ResizeHandle(shape, "NE"));
		handles.add(new ResizeHandle(shape, "SW"));
		handles.add(new ResizeHandle(shape, "SE"));
		handles.add(new CenterHandle(shape));
		return handles;
	}

	public static List<Handle> getHandlesFor(Connector connector) {
		List<Handle> handles = new LinkedList<Handle>();
		handles.add(new ConnectorMiddleHandle(connector));
		return handles;
	}

}
