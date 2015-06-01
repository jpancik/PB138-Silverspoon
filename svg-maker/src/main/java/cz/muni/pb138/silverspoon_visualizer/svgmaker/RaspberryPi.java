package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import com.kitfox.svg.SVGDiagram;
import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;

/**
 * @author Martin Zilak
 * @version 2015/05/31
 */
public class RaspberryPi extends Board {

    public RaspberryPi() {
        super("/raspi.svg");
    }

    @Override
    public void drawRoute(Route route) {
        RouteDrawer routeDrawer = new RouteDrawer(route, svgDocument);
        routeDrawer.drawRoute();

        Element svgRouteElement = svgDocument.getElementById("route");

        double routeMainRectWidth = Double.parseDouble(svgDocument.getElementById("main_rect").getAttribute("width"));
        if (routeMainRectWidth > 315) {
            Element svgBodyRect = svgDocument.getElementById("body");
            svgBodyRect.setAttribute("width", String.valueOf((Double.parseDouble(svgBodyRect.getAttribute("width")) + routeMainRectWidth - 315)));

            Element svgMovableGroup = svgDocument.getElementById("movable");
            svgMovableGroup.setAttribute("transform", "translate(" + (routeMainRectWidth - 315) + " 0)");

            Element svgMainElement = svgDocument.getDocumentElement();
            svgMainElement.setAttribute("width", String.valueOf((Double.parseDouble(svgMainElement.getAttribute("width")) + routeMainRectWidth - 315)));
        }
    }
}
