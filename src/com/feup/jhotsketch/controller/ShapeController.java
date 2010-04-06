package com.feup.jhotsketch.controller;

import org.eclipse.swt.graphics.GC;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Controller")
public interface ShapeController {
	public void mouseDown(int x, int y);
	public void mouseUp(int x, int y);
	public void mouseMove(int x, int y);
	public void paint(GC gc);
}