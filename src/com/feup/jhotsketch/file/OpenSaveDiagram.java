package com.feup.jhotsketch.file;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.application.JHotSketch;
import com.feup.jhotsketch.model.DiagramModel;
import com.feup.jhotsketch.model.OvalModel;
import com.feup.jhotsketch.model.RectangleModel;
import com.feup.jhotsketch.model.RoundedRectangleModel;
import com.feup.jhotsketch.model.ShapeModel;

@PackageName("File")
public class OpenSaveDiagram {
	public static void save(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setFilterExtensions(new String[] {"*.jhs"});
		fileDialog.setOverwrite(true);
		String filename = fileDialog.open();
		if (filename == null) return;
		if (!filename.endsWith(".jhs")) filename += ".jhs";
		try {
			DOMImplementation impl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
			Document document = impl.createDocument(null, "diagram", null);
			Element root = document.getDocumentElement();
			Element sroot = document.createElement("shapes");
			root.appendChild(sroot);
			for (ShapeModel shape : JHotSketch.getInstance().getCurrentDiagram().getFigures()) {
				Element e = getXMLNode(document, shape);
				sroot.appendChild(e);
			}
			Transformer tr = TransformerFactory.newInstance().newTransformer();
			StreamResult result = new StreamResult(new File(filename));
			tr.transform(new DOMSource(document), result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Element getXMLNode(Document document, ShapeModel shape) {
		Element e = document.createElement("shape");
		e.setAttribute("x", ""+shape.getBounds().x);
		e.setAttribute("y", ""+shape.getBounds().y);
		e.setAttribute("width", ""+shape.getBounds().width);
		e.setAttribute("height", ""+shape.getBounds().height);
		if (shape instanceof RectangleModel) e.setAttribute("type", "rectangle");
		if (shape instanceof RoundedRectangleModel) {
			RoundedRectangleModel rrect = (RoundedRectangleModel) shape;
			e.setAttribute("type", "roundedrectangle");
			e.setAttribute("radiusx", "" + rrect.getRadiusX());
			e.setAttribute("radiusy", "" + rrect.getRadiusY());
		}
		if (shape instanceof OvalModel) e.setAttribute("type", "oval");
		return e;
	}

	public static void open(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setFilterExtensions(new String[] {"*.jhs"});
		fileDialog.setOverwrite(true);
		String filename = fileDialog.open();
		if (filename == null) return;
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filename));
			NodeList shapes = document.getChildNodes().item(0).getChildNodes().item(0).getChildNodes();
			DiagramModel diagram = JHotSketch.getInstance().getCurrentDiagram();
			for (int i = 0; i < shapes.getLength(); i++) {
				Node shape = shapes.item(i);
				diagram.addFigure(createShapeFromNode(diagram, shape));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private static ShapeModel createShapeFromNode(DiagramModel diagram, Node shape) {
		String type = shape.getAttributes().getNamedItem("type").getNodeValue();
		int x = new Integer(shape.getAttributes().getNamedItem("x").getNodeValue()).intValue();
		int y = new Integer(shape.getAttributes().getNamedItem("y").getNodeValue()).intValue();
		int width = new Integer(shape.getAttributes().getNamedItem("width").getNodeValue()).intValue();
		int height = new Integer(shape.getAttributes().getNamedItem("height").getNodeValue()).intValue();
		if (type.equals("rectangle")) return new RectangleModel(x, y, width, height);
		if (type.equals("oval")) return new OvalModel(x, y, width, height);
		if (type.equals("roundedrectangle")) {
			RoundedRectangleModel rrect = new RoundedRectangleModel(x, y, width, height);
			int radiusx = new Integer(shape.getAttributes().getNamedItem("radiusx").getNodeValue());
			int radiusy = new Integer(shape.getAttributes().getNamedItem("radiusy").getNodeValue());
			rrect.setRadiusX(radiusx);
			rrect.setRadiusY(radiusy);
			return rrect;
		}
		return null;
	}
}