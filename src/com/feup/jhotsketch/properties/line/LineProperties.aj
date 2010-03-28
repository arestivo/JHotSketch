package com.feup.jhotsketch.properties.line;

import org.eclipse.swt.SWT; 
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;

@PackageName("Line.Properties")
public aspect LineProperties {	
		
	pointcut createPropertyFolder() : 
		call(TabFolder.new(..)) &&
		within(JHotSketch);
		
	TabFolder around() : createPropertyFolder() {
		TabFolder folder = proceed();
		
		TabItem line = new TabItem(folder, SWT.NONE);
		line.setText("Line");
		
		Composite lineComposite = new Composite(folder, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		lineComposite.setLayout(layout);

		LineWidth.aspectOf().createLineWidthControls(lineComposite);
		LineStyle.aspectOf().createLineStyleControls(lineComposite);
		LineColor.aspectOf().createLineColorControls(lineComposite);
		
		line.setControl(lineComposite);	
		lineComposite.pack();
		
		return folder;
	}
	
}