package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import org.w3c.dom.Element;

/**
 * @author Martin Zilak
 * @version 2015/06/02
 */
public class BeagleboneBlack extends Board {

    public BeagleboneBlack() {
        super("/beaglebone.svg");
    }

    @Override
    public void drawRoute(Route route) {
        RouteDrawer routeDrawer = new RouteDrawer(route, svgDocument);
        routeDrawer.drawRoute();

        Element svgRouteElement = svgDocument.getElementById("route");

        double routeMainRectWidth = Double.parseDouble(svgDocument.getElementById("main_rect").getAttribute("width"));
        if (routeMainRectWidth > 275) {
            Element svgBodyRect = svgDocument.getElementById("body");
            svgBodyRect.setAttribute("width", String.valueOf((Double.parseDouble(svgBodyRect.getAttribute("width")) + routeMainRectWidth - 275)));

            Element svgMovableGroup = svgDocument.getElementById("movable");
            svgMovableGroup.setAttribute("transform", "translate(" + (routeMainRectWidth - 275) + " 0)");

            Element svgMainElement = svgDocument.getDocumentElement();
            svgMainElement.setAttribute("width", String.valueOf((Double.parseDouble(svgMainElement.getAttribute("width")) + routeMainRectWidth - 275)));
        }
    }
}
