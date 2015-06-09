package cz.muni.pb138.silverspoon.visualizer.gui;

import cz.muni.pb138.silverspoon.visualizer.parser.Parser;
import cz.muni.pb138.silverspoon.visualizer.parser.ParserException;
import cz.muni.pb138.silverspoon.visualizer.parser.ParserImpl;
import cz.muni.pb138.silverspoon.visualizer.svgmaker.Board;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for XML related functions.
 *
 * @author juraj@pancik.com
 */
public final class XMLUtils {

    private XMLUtils() {
    }

    public static String loadXMLFileIntoString(File file) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        dBuilder = dbFactory.newDocumentBuilder();

        Document document = dBuilder.parse(file);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(baos);
            transformer.transform(source, result);

            return baos.toString(StandardCharsets.UTF_8.name());
        }
    }

    public static Document createSVGDocument(Board board, String data) throws ParserException {
        Parser parser = new ParserImpl();
        parser.setSource(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        parser.load();

        if(parser.getRoutes().isEmpty()) throw new IllegalStateException("There has to be at least one route in given xml!");

        board.drawRoute(parser.getRoutes().get(0));

        return board.getSVG();
    }

    public static void createSVGFile(File file, Document document) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result svgOutput = new StreamResult(file);
        transformer.transform(new DOMSource(document), svgOutput);
    }
}
