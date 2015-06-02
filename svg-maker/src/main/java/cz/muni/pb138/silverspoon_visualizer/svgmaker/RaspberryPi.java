package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import com.kitfox.svg.SVGDiagram;
import cz.muni.pb138.silverspoon_visualizer.parser.GpioPathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.PathObject;
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

        Element svgRouteElement = svgDocument.getElementById("dotted_lines");

        double routeMainRectWidth = Double.parseDouble(svgDocument.getElementById("main_rect").getAttribute("width"));
        if (routeMainRectWidth > 315) {
            Element svgBodyRect = svgDocument.getElementById("body");
            svgBodyRect.setAttribute("width", String.valueOf((Double.parseDouble(svgBodyRect.getAttribute("width")) + routeMainRectWidth - 315)));

            Element svgMovableGroup = svgDocument.getElementById("movable");
            svgMovableGroup.setAttribute("transform", "translate(" + (routeMainRectWidth - 315) + " 0)");

            Element svgMainElement = svgDocument.getDocumentElement();
            svgMainElement.setAttribute("width", String.valueOf((Double.parseDouble(svgMainElement.getAttribute("width")) + routeMainRectWidth - 315)));
        }

        /*
        PathObject pathObject = route.getFirst();
        if (pathObject instanceof GpioPathObject) {
            String pin = ((GpioPathObject) pathObject).getPin();

            pin = pin.toLowerCase();
            Element currPin;
            if (pin.contains("_")) {
                currPin = svgDocument.getElementById(pin);
            } else {
                currPin = svgDocument.getElementById("ph7_20");
            }
            
            Element module1 = svgDocument.getElementById("module_1_rect");

            double currPinX = Double.parseDouble(currPin.getAttribute("x"));
            double currPinY = Double.parseDouble(currPin.getAttribute("y"));
            double module1x = Double.parseDouble(module1.getAttribute("x"));
            double module1y = Double.parseDouble(module1.getAttribute("y"));
            double module1width = Double.parseDouble(module1.getAttribute("width"));
            double module1height = Double.parseDouble(module1.getAttribute("height"));
            double module1middleY = (module1y + module1height)/2;
            
            routeDrawer.drawDottedLine(svgRouteElement, currPinX, currPinY, currPinX, (currPinY + module1middleY)/2);
            routeDrawer.drawDottedLine(svgRouteElement, currPinX, (currPinY + module1middleY)/2, module1x - 10, (currPinY + module1middleY)/2);
            routeDrawer.drawDottedLine(svgRouteElement, module1x - 10, (currPinY + module1middleY)/2, module1x - 10, module1middleY);
            routeDrawer.drawDottedLine(svgRouteElement, module1x - 10, module1middleY, module1x, module1middleY);        
            
        }
        */
    }
}
