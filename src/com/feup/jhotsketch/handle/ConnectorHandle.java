package com.feup.jhotsketch.handle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;

@PackageName("Handle")
public class ConnectorHandle extends Handle{

	private Connector connector;
	
	public ConnectorHandle(Connector connector) {
		this.connector = connector;
	}

	@Override
	public void paint(GC gc) {
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(getBounds());
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(((int)connector.getCenter().getX() - SIZE / 2), (int)(connector.getCenter().getY() - SIZE / 2), SIZE, SIZE);
	}
}
