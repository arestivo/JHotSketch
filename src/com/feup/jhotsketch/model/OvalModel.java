package com.feup.jhotsketch.model;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class OvalModel extends ShapeModel {
	
	public OvalModel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public boolean contains(int x, int y) {
		return ovalContains(bounds.x, bounds.y, bounds.width, bounds.height, x, y);
	}

	// Stolen from Eclipse TPTP
	public static boolean ovalContains( float x1, float x2, float f, float g, int px, int py )
	{
		float cx = x1 + f / 2;
		float cy = x2 + g / 2;
		float dx = px - cx;
		float dy = py - cy;
		float rp = (float) (Math.sqrt(dx * dx + dy * dy));
		float rx = Math.abs(f/2);
		float ry = Math.abs(g/2);
		if(rx == ry) return rp <= rx;
		if(dy == 0) return (Math.abs(dx) <= rx);
		if(dx == 0) return (Math.abs(dy) <= ry);
		float a = (float) Math.atan2( dy, dx );
		float xa = (float)(rx * Math.cos(a));
		float ya = (float)(ry * Math.sin(a));
		float ra = (float)(Math.sqrt(xa * xa + ya * ya));
		return (rp <= ra);
	}

	@Override
	public ShapeModel clone() {
		OvalModel clone = new OvalModel(bounds.x, bounds.y, bounds.width, bounds.height);
		return clone;
	}

}
