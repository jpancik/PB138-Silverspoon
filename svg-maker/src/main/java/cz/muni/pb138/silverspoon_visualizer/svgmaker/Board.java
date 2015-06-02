package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents the base concept of any board.
 *
 * @author Martin Zilak
 * @version 2015/05/30
 */
public abstract class Board {
    protected Document svgDocument;

    /**
     * @param path Path to the .svg template.
     */
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

    /**
     * This method returns you the .svg in it's current state (with modifications, if any were done).
     *
     * @return Document containing .svg.
     */
    public Document getSVG() {
        return this.svgDocument;
    }

    public abstract void drawRoute(Route route);
}
