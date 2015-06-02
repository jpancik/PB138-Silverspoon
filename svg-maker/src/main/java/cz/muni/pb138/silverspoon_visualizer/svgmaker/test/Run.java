package cz.muni.pb138.silverspoon_visualizer.svgmaker.test;

import com.kitfox.svg.SVGDiagram;
import cz.muni.pb138.silverspoon_visualizer.parser.Parser;
import cz.muni.pb138.silverspoon_visualizer.parser.ParserException;
import cz.muni.pb138.silverspoon_visualizer.parser.ParserImpl;
import cz.muni.pb138.silverspoon_visualizer.svgmaker.*;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author Martin Zilak
 * @version 2015/06/01
 */
public class Run {
    public static  void main(String ... args) {
        Parser parser = new ParserImpl();
        Board pi;

        try {
            parser.setSource("/t1.xml");
            parser.load();

            pi = new BeagleboneBlack();
            pi.drawRoute(parser.getRoutes().get(0));

            Document document = pi.getSVG();

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

        } catch (ParserException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
