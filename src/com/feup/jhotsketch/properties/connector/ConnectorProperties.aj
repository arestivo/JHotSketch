package com.feup.jhotsketch.properties.connector;

import org.eclipse.swt.SWT; 
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;

@PackageName("Connector.Properties")
public aspect ConnectorProperties {	
		
	pointcut createPropertyFolder() : 
		call(TabFolder.new(..)) &&
		within(JHotSketch);
		
	TabFolder around() : createPropertyFolder() {
		TabFolder folder = proceed();
		
		TabItem line = new TabItem(folder, SWT.NONE);
		line.setText("Connector");
		
		Composite lineComposite = new Composite(folder, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		lineComposite.setLayout(layout);

//		ConnectorWidth.aspectOf().createLineWidthControls(lineComposite);
//		ConnectorStyle.aspectOf().createLineStyleControls(lineComposite);
		ConnectorColor.aspectOf().createLineColorControls(lineComposite);
		
		line.setControl(lineComposite);	
		lineComposite.pack();
		
		return folder;
	}
	
}