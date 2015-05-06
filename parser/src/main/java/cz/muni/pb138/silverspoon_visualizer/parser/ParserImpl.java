package cz.muni.pb138.silverspoon_visualizer.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Norbert Slivka
 * @version 0.1
 * @since 5.5.2015
 */
public class ParserImpl implements Parser {

    List<Flow> flows;
    Document document;

    private final static Logger LOGGER = Logger.getLogger(ParserImpl.class.getName());

    public ParserImpl() {
        flows = new ArrayList<>();
        document = null;
    }

    @Override
    public void setSource(String path) throws ParserException {
        this.setSource(this.getClass().getResourceAsStream(path));
    }

    @Override
    public void setSource(InputStream file) throws ParserException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(file);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ParserException("Unable to load the file.");
        }


    }

    @Override
    public void load() throws ParserException {
        if (document == null) {
            throw new ParserException("File was not loaded yet.");
        }
        List<Element> contexts = nodeListToList(document.getDocumentElement().getElementsByTagName("camelContext"));
        for (Element context : contexts) {
            List<Element> routes = nodeListToList(context.getElementsByTagName("route"));
            for (Element e : routes) {
                loadRoute(e);
            }
        }
        System.out.println(flows.get(0));
    }

    private void loadRoute(Element route) throws ParserException {
        Flow flow = new FlowImpl();
        PathObject prev = null;
        //for (Element element =(Element) route.getFirstChild(); element != null; element = (Element) element.getNextSibling()) {
        for(Node node = route.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node instanceof Element) {
                Element element = (Element) node;
                PathObject obj = parseElement(element);
                if (obj.getNextType()==null) {
                    obj.setNextType(SuccessionTypes.STRAIGHT);
                }
                // TODO: rework, only working on linear trees
                if (obj != null) {
                    if (prev == null) {
                        flow.setFirst(obj);
                        prev = obj;
                    }
                    else {
                        prev.addNext(obj);
                        prev = obj;
                    }
                }
            }

        }
        flows.add(flow);
    }


    @Override
    public List<Flow> getFlows() {
        return Collections.unmodifiableList(flows);
    }

    private static List<Element> nodeListToList(NodeList list) {
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {
            elements.add((Element) list.item(i));
        }
        return elements;
    }

    private PathObject parseElement(Element element) throws ParserException {
        if (element.getTagName().equals("from") || element.getTagName().equals("to")) {
            String from  = element.getAttribute("uri");
            URI url;
            try {
                url = new URI(from);
            } catch (URISyntaxException e) {
                throw new ParserException("Unable to parse the path.");
            }
            return protocolHandling(url);
        }
        else {
            LOGGER.log(Level.WARNING, "Unknown element encountered: \"" + element.getTagName() + "\"");
        }
        return null;
    }

    private PathObject protocolHandling(URI uri) {
        String protocol = uri.getScheme();
        if (protocol.equals("gpio")) {
            return new GpioPathObject.GpioPathObjectBuilder().setAdditionalInfo(uri.getSchemeSpecificPart()).setName(protocol).setNextType(SuccessionTypes.STRAIGHT).setPin(uri.getHost()).build();
        } else if(protocol.equals("mqtt")) {
            return new SimplePathObject.SimplePathObjectBuilder().setAdditionalInfo(uri.getSchemeSpecificPart()).setName(protocol).setNextType(SuccessionTypes.STRAIGHT).setNextType(SuccessionTypes.ETHERNET).build();
        }
        else {
            return new SimplePathObject.SimplePathObjectBuilder().setAdditionalInfo(uri.getSchemeSpecificPart()).setName(protocol).setNextType(SuccessionTypes.STRAIGHT).build();
        }
    }
}
