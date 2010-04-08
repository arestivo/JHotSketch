package com.feup.jhotsketch.style;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class StyleProperties {
	private Color lineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private Color fillColor = null;
	private int lineWidth = 1;
	private int lineStyle = SWT.LINE_SOLID;
	private int alpha = 255;
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	public Color getLineColor() {
		return lineColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	public Color getFillColor() {
		return fillColor;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
	}
	public int getLineStyle() {
		return lineStyle;
	}
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	public int getAlpha() {
		return alpha;
	}
}
