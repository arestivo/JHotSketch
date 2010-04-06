package com.feup.jhotsketch.handle;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Handle")
public abstract class Handle {
	protected static int SIZE = 10;

	public Handle() {
	}
	
	public abstract Rectangle getBounds();

	public abstract void paint(GC gc);
	
	public boolean contains(int x, int y) {
		return getBounds().contains(x, y);
	}
}