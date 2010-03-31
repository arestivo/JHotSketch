package com.feup.jhotsketch.connector;

import java.awt.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.geometry.Intersector;
import com.feup.jhotsketch.model.Handle;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("Connector")
public class ConnectorView {
	public static void drawConnector(GC gc, ConnectorModel connector) {
		ShapeModel source = connector.getSource();
		ShapeModel sink = connector.getSink();
		Point p1 = new Point(source.getBounds().x + source.getBounds().width / 2, source.getBounds().y + source.getBounds().height / 2);
		Point p2 = new Point(sink.getBounds().x + sink.getBounds().width / 2, sink.getBounds().y + sink.getBounds().height / 2);
		if (sink instanceof OvalModel) p2 = Intersector.intersectOval(p1, p2, sink.getBounds());
		else p2 = Intersector.intersectRectangle(p1, p2, sink.getBounds());
		if (source instanceof OvalModel) p1 = Intersector.intersectOval(p2, p1, source.getBounds());
		else p1 = Intersector.intersectRectangle(p2, p1, source.getBounds());
		gc.drawLine(p1.x, p1.y, p2.x, p2.y);
		
		ConnectorEndPainter.paintConnectorEnd(p1, p2, connector.getSinkEnd(), connector.getEndSize(), gc);
		ConnectorEndPainter.paintConnectorEnd(p2, p1, connector.getSourceEnd(), connector.getEndSize(), gc);
		
		connector.setBounds(new Rectangle(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y)));
		
		if (connector.isSelected()) {
			gc.setBackground(Display.getCurrent().getSystemColor((SWT.COLOR_DARK_GRAY)));
			gc.fillRectangle((p1.x + p2.x) / 2 - Handle.getSize() / 2, (p1.y + p2.y) / 2 - Handle.getSize() / 2, Handle.getSize(), Handle.getSize());
		}
	}

}
