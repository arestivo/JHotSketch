package com.feup.jhotsketch.style;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.shape.RectangleShape;
import com.feup.jhotsketch.shape.Shape;

@PackageName("Style")
public class StylesPanel extends Composite{

	public StylesPanel(Composite composite, int style) {
		super(composite, SWT.NONE);
		setLayout(new GridLayout(1, true));

		Group group = new Group(composite,SWT.NONE);
		group.setText("Styles");
		group.setLayout(new RowLayout());

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = 235;
		gd.horizontalIndent = 5;
		gd.verticalAlignment = SWT.FILL;
		group.setLayoutData(gd);
		
		Shape shape = new RectangleShape(6, 6, 14, 34);
		shape.setFillColor(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
		
		new StyleControl(group, SWT.NONE).setShape(shape);
		new StyleControl(group, SWT.NONE).setShape(shape);
		new StyleControl(group, SWT.NONE).setShape(shape);
		new StyleControl(group, SWT.NONE).setShape(shape);
		
		group.pack();
	}

}
