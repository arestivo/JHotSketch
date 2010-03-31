package com.feup.jhotsketch.properties.connector;

import org.eclipse.swt.SWT; 
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;

@PackageName("Properties.Connector")
public aspect ConnectorProperties {	
	private TabItem shapeTI = null;
	
	pointcut createPropertyFolder() : 
		call(TabFolder.new(..)) &&
		within(JHotSketch);
		
	TabFolder around() : createPropertyFolder() {
		TabFolder folder = proceed();
		
		shapeTI = new TabItem(folder, SWT.NONE);
		shapeTI.setText("Connector");
		
		Composite lineComposite = new Composite(folder, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		lineComposite.setLayout(layout);

//		ConnectorWidth.aspectOf().createLineWidthControls(lineComposite);
		ConnectorLineStyle.aspectOf().createLineStyleControls(lineComposite);
		ConnectorColor.aspectOf().createLineColorControls(lineComposite);
		ConnectorArrowSize.aspectOf().createArrowControls(lineComposite);
		
		shapeTI.setControl(lineComposite);	
		lineComposite.pack();
		
		return folder;
	}
	
	pointcut diagramChanged(DiagramModel diagram) :
		call (void DiagramModel.diagramChanged()) &&
		target(diagram);
	
	after(DiagramModel diagram) : diagramChanged(diagram){
		if (diagram.getSelectedConnectors().size() != 0) shapeTI.getParent().setSelection(shapeTI);
	}
}