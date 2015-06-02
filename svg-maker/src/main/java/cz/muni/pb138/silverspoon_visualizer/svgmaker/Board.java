package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import java.io.*;
import java.net.URI;

/**
 * @author Martin Zilak
 * @version 2015/05/30
 */
public abstract class Board {
    protected Document svgDocument;

    protected Board(String path) {
        try {
            InputStream svgInputStream = this.getClass().getResourceAsStream(path);

            String parser = XMLResourceDescriptor.getXMLParserClassName();

            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);

            svgDocument = factory.createDocument(path, svgInputStream);
        } catch (FileNotFoundException e) {
            System.err.print("Can't load file located at: " + path);
        } catch (IOException e) {
            System.err.print("Can't load file located at: " + path);
        }
    }

    public Document getSVG() {
        return this.svgDocument;
    }

    public abstract void drawRoute(Route route);
}