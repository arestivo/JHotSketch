package com.feup.jhotsketch.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.connector.Connector;
import com.feup.jhotsketch.connector.Connector.ENDTYPE;
import com.feup.jhotsketch.controller.ControllerObserver;
import com.feup.jhotsketch.controller.DiagramController;
import com.feup.jhotsketch.viewer.Viewer;

@PackageName("Application")
public class PropertyPanel extends Composite implements ApplicationObserver, ControllerObserver{
	private DiagramController currentController;

	private Button lineColor;
	private Button fillColor;
	private Scale  lineWidth;
	private List<Button> lineStyle = new LinkedList<Button>();

	private List<Button> sourceEndType = new LinkedList<Button>();
	private List<Button> targetEndType = new LinkedList<Button>();

	private Scale targetEndSize;
	private Scale sourceEndSize;

	private Scale alpha;

	public PropertyPanel(Composite parent, int style) {
		super(parent, style);	
		
		setLayout(new GridLayout(1, true));
		createControls(this);
		
		pack();
	}

	private void createControls(Composite panel) {
		Group group = new Group(panel, SWT.BORDER);
		group.setLayout(new GridLayout(2, false));
		group.setText("Properties");
		
		createLineColorControl(group);
		createFillColorControl(group);
		createAlphaControl(group);
		createLineWidthControl(group);
		createLineStyleControl(group);
		createEndSizeControl(group);
		createEndTypeControl(group);

		GridData gd = new GridData();
		gd.verticalIndent = 10;
		group.setLayoutData(gd);
		
		group.pack();
		group.setSize(200, group.getSize().y);
	}

	private void createAlphaControl(Group group) {
		Label label = new Label(group, SWT.NONE);
		label.setText("Alpha");

		alpha = new Scale(group, SWT.NONE);
		alpha.setMinimum(0);
		alpha.setMaximum(255);
		alpha.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int a = alpha.getSelection();
				currentController.setSelectedAlpha(a);
			}
		});
	}

	private void createEndTypeControl(Composite panel) {
		GridData gd = new GridData(); gd.horizontalSpan = 2;

		Label labelSource = new Label(panel, SWT.NONE);
		labelSource.setText("Source End Style");
		labelSource.setLayoutData(gd);
		
		Composite compositeSource = new Composite(panel, SWT.NONE);
		compositeSource.setLayoutData(gd);

		sourceEndType.add(createSourceEndTypeButton(compositeSource, "solid", Connector.ENDTYPE.NONE));
		sourceEndType.add(createSourceEndTypeButton(compositeSource, "hollowcircleleft", Connector.ENDTYPE.HOLLOWCIRCLE));
		sourceEndType.add(createSourceEndTypeButton(compositeSource, "filledcircleleft", Connector.ENDTYPE.FILLEDCIRCLE));
		sourceEndType.add(createSourceEndTypeButton(compositeSource, "simplearrowleft", Connector.ENDTYPE.SIMPLEARROW));
		sourceEndType.add(createSourceEndTypeButton(compositeSource, "hollowarrowleft", Connector.ENDTYPE.HOLLOWARROW));
		sourceEndType.add(createSourceEndTypeButton(compositeSource, "filledarrowleft", Connector.ENDTYPE.FILLEDARROW));
		sourceEndType.add(createSourceEndTypeButton(compositeSource, "hollowsquareleft", Connector.ENDTYPE.HOLLOWSQUARE));
		sourceEndType.add(createSourceEndTypeButton(compositeSource, "filledsquareleft", Connector.ENDTYPE.FILLEDSQUARE));

		Label labelTarget = new Label(panel, SWT.NONE);
		labelTarget.setText("Target End Style");
		labelTarget.setLayoutData(gd);

		Composite compositeTarget = new Composite(panel, SWT.NONE);
		compositeTarget.setLayoutData(gd);

		targetEndType.add(createTargetEndTypeButton(compositeTarget, "solid", Connector.ENDTYPE.NONE));
		targetEndType.add(createTargetEndTypeButton(compositeTarget, "hollowcircleright", Connector.ENDTYPE.HOLLOWCIRCLE));
		targetEndType.add(createTargetEndTypeButton(compositeTarget, "filledcircleright", Connector.ENDTYPE.FILLEDCIRCLE));
		targetEndType.add(createTargetEndTypeButton(compositeTarget, "simplearrowright", Connector.ENDTYPE.SIMPLEARROW));
		targetEndType.add(createTargetEndTypeButton(compositeTarget, "hollowarrowright", Connector.ENDTYPE.HOLLOWARROW));
		targetEndType.add(createTargetEndTypeButton(compositeTarget, "filledarrowright", Connector.ENDTYPE.FILLEDARROW));
		targetEndType.add(createTargetEndTypeButton(compositeTarget, "hollowsquareright", Connector.ENDTYPE.HOLLOWSQUARE));
		targetEndType.add(createTargetEndTypeButton(compositeTarget, "filledsquareright", Connector.ENDTYPE.FILLEDSQUARE));

		compositeSource.setLayout(new FillLayout());
		compositeSource.pack();		

		compositeTarget.setLayout(new FillLayout());
		compositeTarget.pack();
	}

	private Button createSourceEndTypeButton(Composite composite, String icon, final ENDTYPE endType) {
		Button button = new Button(composite, SWT.TOGGLE);
		button.setImage(new Image(Display.getCurrent(), "icons/" + icon + ".gif"));
		button.setData(endType);
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				currentController.setSelectedSourceEndType(endType);
			}
		});
		return button;
	}

	private Button createTargetEndTypeButton(Composite composite, String icon, final ENDTYPE endType) {
		Button button = new Button(composite, SWT.TOGGLE);
		button.setImage(new Image(Display.getCurrent(), "icons/" + icon + ".gif"));
		button.setData(endType);
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				currentController.setSelectedTargetEndType(endType);
			}
		});
		return button;
	}

	private void createEndSizeControl(Composite panel) {
		Label labelSource = new Label(panel, SWT.NONE);
		labelSource.setText("Source End Size");

		sourceEndSize = new Scale(panel, SWT.NONE);
		sourceEndSize.setMinimum(8);
		sourceEndSize.setMaximum(20);
		sourceEndSize.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int size = sourceEndSize.getSelection();
				currentController.setSelectedSourceEndSize(size);
			}
		});
		
		Label labelTarget = new Label(panel, SWT.NONE);
		labelTarget.setText("Target End Size");
		
		targetEndSize = new Scale(panel, SWT.NONE);
		targetEndSize.setMinimum(8);
		targetEndSize.setMaximum(20);
		targetEndSize.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int size = targetEndSize.getSelection();
				currentController.setSelectedTargetEndSize(size);
			}
		});
}

	private void createLineStyleControl(Composite panel) {
		Label label = new Label(panel, SWT.NONE);
		label.setText("Line Style");
		
		Composite composite = new Composite(panel, SWT.NONE);

		lineStyle.add(createLineStyleButton(composite, "solid", SWT.LINE_SOLID));
		lineStyle.add(createLineStyleButton(composite, "dash", SWT.LINE_DASH));
		lineStyle.add(createLineStyleButton(composite, "dot", SWT.LINE_DOT));

		composite.setLayout(new FillLayout());
		composite.pack();
	}

	private Button createLineStyleButton(Composite composite, String icon, final int style) {
		Button button = new Button(composite, SWT.TOGGLE);
		button.setImage(new Image(Display.getCurrent(), "icons/" + icon + ".gif"));
		button.setData(new Integer(style));
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				currentController.setSelectedLineStyle(style);
			}
		});
		return button;
	}

	private void createLineWidthControl(Composite panel) {
		Label label = new Label(panel, SWT.NONE);
		label.setText("Line Width");

		lineWidth = new Scale(panel, SWT.NONE);
		lineWidth.setMinimum(1);
		lineWidth.setMaximum(20);
		lineWidth.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int width = lineWidth.getSelection();
				currentController.setSelectedLineWidth(width);
			}
		});
	}

	private void createFillColorControl(final Composite panel) {
		Label label = new Label(panel, SWT.NONE);
		label.setText("Background Color");

		Composite composite = new Composite(panel, SWT.NONE);
		
		fillColor = new Button(composite, SWT.NONE);
		fillColor.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		fillColor.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ColorDialog dialog = new ColorDialog(panel.getShell());
				dialog.open();
				if (dialog.getRGB() != null)
					currentController.setSelectedFillColor(new Color(Display.getCurrent(), dialog.getRGB()));
			}
		});
		
		Button removeFill = new Button(composite, SWT.NONE);
		removeFill.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		removeFill.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				currentController.setSelectedFillColor(null);
			}
		});
		
		composite.setLayout(new FillLayout());
		composite.pack();
	}

	private void createLineColorControl(final Composite panel) {
		Label label = new Label(panel, SWT.NONE);
		label.setText("Line Color");

		lineColor = new Button(panel, SWT.NONE);
		lineColor.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		lineColor.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				ColorDialog dialog = new ColorDialog(panel.getShell());
				dialog.open();
				if (dialog.getRGB() != null)
					currentController.setSelectedLineColor(new Color(Display.getCurrent(), dialog.getRGB()));
			}
		});

	}

	@Override
	public void controllerChanged(DiagramController controller) {
		updateProperties();
	}

	private void updateProperties() {
		Set<Color> lineColors = currentController.getSelectedLineColors();
		Set<Color> fillColors = currentController.getSelectedFillColors();
		Set<Integer> lineWidths = currentController.getSelectedLineWidths();
		Set<Integer> alphas = currentController.getSelectedAlphas();
		Set<Integer> lineStyles = currentController.getSelectedLineStyles();
		Set<Integer> targetEndSizes = currentController.getSelectedTargetEndSizes();
		Set<Integer> sourceEndSizes = currentController.getSelectedSourceEndSizes();
		Set<Connector.ENDTYPE> sourceEndTypes = currentController.getSelectedSourceEndTypes();
		Set<Connector.ENDTYPE> targetEndTypes = currentController.getSelectedTargetEndTypes();

		if (lineColors.size() == 1)	{
			lineColor.setBackground((Color) lineColors.toArray()[0]);		
			lineColor.setImage(new Image(Display.getCurrent(), "icons/solid.gif"));
		} else {
			lineColor.setBackground(null); 
			lineColor.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		}
		
		if (fillColors.size() == 1)	{
			fillColor.setBackground((Color) fillColors.toArray()[0]);		
			fillColor.setImage(new Image(Display.getCurrent(), "icons/square.gif"));
		} else {
			fillColor.setBackground(null); 
			fillColor.setImage(new Image(Display.getCurrent(), "icons/null.gif"));
		}

		if (lineWidths.size() == 1) {
			lineWidth.setSelection(((Integer)lineWidths.toArray()[0]).intValue());
		} else {
			lineWidth.setSelection(1);
		}

		if (alphas.size() == 1) {
			alpha.setSelection(((Integer)alphas.toArray()[0]).intValue());
		} else {
			alpha.setSelection(1);
		}

		for (Button button : lineStyle) {
			button.setSelection(false);
		}
		if (lineStyles.size() == 1) {
			for (Button button : lineStyle) {
				if (button.getData().equals(lineStyles.toArray()[0])) button.setSelection(true);
			}
		}

		if (targetEndSizes.size() == 1) {
			targetEndSize.setSelection(((Integer)targetEndSizes.toArray()[0]).intValue());
		} else {
			targetEndSize.setSelection(4);
		}

		if (sourceEndSizes.size() == 1) {
			sourceEndSize.setSelection(((Integer)sourceEndSizes.toArray()[0]).intValue());
		} else {
			sourceEndSize.setSelection(4);
		}

		for (Button button : sourceEndType) {
			button.setSelection(false);
		}
		if (sourceEndTypes.size() == 1) {
			for (Button button : sourceEndType) {
				if (button.getData().equals(sourceEndTypes.toArray()[0])) button.setSelection(true);
			}
		}

		for (Button button : targetEndType) {
			button.setSelection(false);
		}
		if (targetEndTypes.size() == 1) {
			for (Button button : targetEndType) {
				if (button.getData().equals(targetEndTypes.toArray()[0])) button.setSelection(true);
			}
		}
}

	@Override
	public void diagramSelected(Viewer viewer) {
		if (currentController != null)	currentController.removeControllerObserver(this);
		currentController = viewer.getController();
		currentController.addControllerObserver(this);
		updateProperties();
	}
}