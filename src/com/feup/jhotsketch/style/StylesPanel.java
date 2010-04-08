package com.feup.jhotsketch.style;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.feup.contribution.aida.annotations.PackageName;

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
		
		Label teste = new Label(group, SWT.NONE);
		teste.setText("TESTE");
		
		group.pack();
	}

}
