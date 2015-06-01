package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import cz.muni.pb138.silverspoon_visualizer.parser.PathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import cz.muni.pb138.silverspoon_visualizer.parser.SuccessionTypes;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Martin Zilak
 * @version 2015/06/01
 */
public class RouteDrawer {
    public static final int ROUTE_MODULE_WIDTH = 120;
    public static final int ROUTE_SPACER_WIDTH = 25;
    public static final int ROUTE_MODULE_HEIGHT = 60;
    public static final int ROUTE_SPACER_HEIGHT = 50;
    public static final String ROUTE_MODULE_COLOR = "#D5E5FF";
    public static final String ROUTE_BACKGROUND_COLOR = "#333333";

    private Route route;
    private String svgNS;
    private Document document;

    public RouteDrawer(Route route) {
        this.route = route;
    }

    public Document drawRoute() {
        int i = route.getLength();
        int width = (i + 1) * ROUTE_SPACER_WIDTH + i * ROUTE_MODULE_WIDTH;
        int height = 2 * ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT;

        DOMImplementation domImplementation = SVGDOMImplementation.getDOMImplementation();
        this.svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        this.document = domImplementation.createDocument(svgNS, "svg", null);

        Element svgRoot = document.getDocumentElement();
        svgRoot.setAttributeNS(null, "width", String.valueOf(width));
        svgRoot.setAttributeNS(null, "height", String.valueOf(height));

        Element routeGroup = document.createElementNS(svgNS, "g");
        routeGroup.setAttributeNS(null, "id", "route");

        Element mainRect = new RectBuilder().setX(0).setY(0).setWidth(width).setHeight(height).setFill(ROUTE_BACKGROUND_COLOR).setRX(5).setStroke("#000000").setStrokeWidth(3).getRect();
        routeGroup.appendChild(mainRect);

        PathObject pathObject = route.getFirst();
        int spacer = ROUTE_SPACER_WIDTH;
        int count = 0;

        while (true) {
            String name = pathObject.getName();
            count += 1;

            routeGroup.appendChild(new ModuleBuilder(spacer, name, count).getModule());

            if (pathObject.getNextType() != SuccessionTypes.STRAIGHT) {
                break;
            } else {
                routeGroup.appendChild(new ArrowBuilder(spacer + ROUTE_MODULE_WIDTH, ROUTE_SPACER_WIDTH, count).getArrow());

                pathObject = pathObject.getNext().get(0);
                spacer += ROUTE_SPACER_WIDTH + ROUTE_MODULE_WIDTH;
            }
        }

        svgRoot.appendChild(routeGroup);

        return document;
    }

    private class RectBuilder {
        private Element rect;

        public RectBuilder() {
            this.rect = document.createElementNS(svgNS, "rect");
        }

        public RectBuilder setX(double x) {
            this.rect.setAttributeNS(null, "x", String.valueOf(x));
            return this;
        }

        public RectBuilder setY(double y) {
            this.rect.setAttributeNS(null, "y", String.valueOf(y));
            return this;
        }

        public RectBuilder setWidth(double width) {
            this.rect.setAttributeNS(null, "width", String.valueOf(width));
            return this;
        }

        public RectBuilder setHeight(double height) {
            this.rect.setAttributeNS(null, "height", String.valueOf(height));
            return this;
        }

        public RectBuilder setRX(double rx) {
            this.rect.setAttributeNS(null, "rx", String.valueOf(rx));
            return this;
        }

        public RectBuilder setFill(String fill) {
            this.rect.setAttributeNS(null, "fill", fill);
            return this;
        }

        public RectBuilder setStroke(String stroke) {
            this.rect.setAttributeNS(null, "stroke", stroke);
            return this;
        }

        public RectBuilder setStrokeWidth(double strokeWidth) {
            this.rect.setAttributeNS(null, "stroke-width", String.valueOf(strokeWidth));
            return this;
        }

        public Element getRect() {
            return this.rect;
        }
    }

    private class LineBuilder {
        private Element line;

        public LineBuilder() {
            this.line = document.createElementNS(svgNS, "line");
        }

        public LineBuilder setX1(double x1) {
            this.line.setAttributeNS(null, "x1", String.valueOf(x1));
            return this;
        }

        public LineBuilder setY1(double y1) {
            this.line.setAttributeNS(null, "y1", String.valueOf(y1));
            return this;
        }

        public LineBuilder setX2(double x2) {
            this.line.setAttributeNS(null, "x2", String.valueOf(x2));
            return this;
        }

        public LineBuilder setY2(double y2) {
            this.line.setAttributeNS(null, "y2", String.valueOf(y2));
            return this;
        }

        public Element getLine() {
            return this.line;
        }
    }

    private class ModuleBuilder {
        private double x;
        private String text;
        private int num;

        public ModuleBuilder(double x, String text, int num) {
            this.x = x;
            this.text = text;
            this.num = num;
        }

        public Element getModule() {
            Element moduleGroup = document.createElementNS(svgNS, "g");
            moduleGroup.setAttributeNS(null, "id", "module_" + String.valueOf(num));

            Element moduleRect = new RectBuilder().setX(x).setY(ROUTE_SPACER_HEIGHT).setWidth(ROUTE_MODULE_WIDTH).setHeight(ROUTE_MODULE_HEIGHT).setFill(ROUTE_MODULE_COLOR).setRX(2).setStroke("#000000").setStrokeWidth(1).getRect();
            moduleGroup.appendChild(moduleRect);

            Element moduleText = document.createElementNS(svgNS, "text");
            moduleText.setAttributeNS(null, "x", String.valueOf(x + 15));
            moduleText.setAttributeNS(null, "y", String.valueOf(ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT/2 + 10));
            moduleText.setAttributeNS(null, "font-size", "160%");
            moduleText.setAttributeNS(null, "fill", "#000000");
            moduleText.setTextContent(this.text.toUpperCase());
            moduleGroup.appendChild(moduleText);

            return moduleGroup;
        }
    }

    private class ArrowBuilder {
        private double x;
        private double width;
        private int num;

        public ArrowBuilder(double x, double width, int num) {
            this.x = x;
            this.width = width;
            this.num = num;
        }

        public Element getArrow() {
            Element arrowGroup = document.createElementNS(svgNS, "g");
            arrowGroup.setAttributeNS(null, "id", "arrow_" + String.valueOf(num));
            arrowGroup.setAttributeNS(null, "fill", "none");
            arrowGroup.setAttributeNS(null, "stroke", "#FFFFFF");
            arrowGroup.setAttributeNS(null, "stroke-width", "2");

            arrowGroup.appendChild(new LineBuilder().setX1(x).setY1(ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT / 2).setX2(x + width).setY2(ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT / 2).getLine());
            arrowGroup.appendChild(new LineBuilder().setX1(x + width - 2).setY1(ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT/2).setX2(x + width - 8).setY2(ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT/2 - 5).getLine());
            arrowGroup.appendChild(new LineBuilder().setX1(x + width - 2).setY1(ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT/2).setX2(x + width - 8).setY2(ROUTE_SPACER_HEIGHT + ROUTE_MODULE_HEIGHT/2 + 5).getLine());

            return arrowGroup;
        }
    }
}
