package com.feup.jhotsketch.application;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.OvalShape;
import com.feup.jhotsketch.shape.RectangleShape;
import com.feup.jhotsketch.shape.RegularPolygonShape;
import com.feup.jhotsketch.shape.RhombusShape;
import com.feup.jhotsketch.shape.RoundedRectangleShape;

@PackageName("Application")
public class ShapeCreatorPanel extends Composite{

	public ShapeCreatorPanel(Composite parent, int style) {
		super(parent, style);

		new ShapeControl(this, SWT.BORDER).setShape(new RectangleShape(10, 10, 40, 30));
		new ShapeControl(this, SWT.BORDER).setShape(new OvalShape(10, 10, 40, 30));
		new ShapeControl(this, SWT.BORDER).setShape(new RoundedRectangleShape(10, 10, 40, 30));
		new ShapeControl(this, SWT.BORDER).setShape(new RhombusShape(10, 10, 40, 30));
		new ShapeControl(this, SWT.BORDER).setShape(new RegularPolygonShape(10, 10, 40, 30, 5, -Math.PI / 2));
		new ShapeControl(this, SWT.BORDER).setShape(new RegularPolygonShape(10, 10, 40, 30, 6, Math.PI));
		setLayout(new RowLayout());
		for (Control control : getChildren()) {
			RowData rd = new RowData(60, 50);
			control.setLayoutData(rd);
		}
		pack();
	}

}
