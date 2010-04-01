package com.feup.jhotsketch.file;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;

@PackageName("File")
public class ExportDiagram {

	public static void export(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setFilterExtensions(new String[] {"*.png"});
		fileDialog.setOverwrite(true);
		String filename = fileDialog.open();
		if (filename == null) return;
		if (!filename.endsWith(".png")) filename += ".png";
		Rectangle bounds = JHotSketch.getInstance().getCurrentDiagram().getSize();
		Image image = new Image(Display.getCurrent(), bounds.width + 10, bounds.height + 10);
		GC gc = new GC(image);
		Transform tr = new Transform(Display.getCurrent());
		tr.translate(-bounds.x + 5, -bounds.y + 5);
		gc.setTransform(tr);
		JHotSketch.getInstance().getCurrentView().paint(gc,true);
		ImageLoader il = new ImageLoader();
		il.data = new ImageData[] {image.getImageData()};
		il.save(filename, SWT.IMAGE_PNG);
	}
}