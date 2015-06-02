package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import com.kitfox.svg.SVGDiagram;
import cz.muni.pb138.silverspoon_visualizer.parser.GpioPathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.PathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents Raspberry Pi B+. It holds path to it and is responsible for generating some parts of the route.
 *
 * @author Martin Zilak
 * @version 2015/05/31
 */
public class RaspberryPi extends Board {

    public RaspberryPi() {
        super("/raspi.svg");
    }

    /**
     * This method is responsible for generating some parts of the route (mainly dotted lines) and is also responsible for expanding the .svg in order to accommodate full width of the route.
     *
     * @param route Route object.
     * @throws IllegalArgumentException when data about pin in route isn't correct.
     */
    @Override
    public void drawRoute(Route route) {
        RouteDrawer routeDrawer = new RouteDrawer(route, svgDocument);
        routeDrawer.drawRoute();

        //next part handles expanding the .svg
        double routeMainRectWidth = Double.parseDouble(svgDocument.getElementById("main_rect").getAttribute("width"));
        if (routeMainRectWidth > 315) {
            Element svgBodyRect = svgDocument.getElementById("body");
            svgBodyRect.setAttribute("width", String.valueOf((Double.parseDouble(svgBodyRect.getAttribute("width")) + routeMainRectWidth - 315)));

            Element svgMovableGroup = svgDocument.getElementById("movable");
            svgMovableGroup.setAttribute("transform", "translate(" + (routeMainRectWidth - 315) + " 0)");

            Element svgMainElement = svgDocument.getDocumentElement();
            svgMainElement.setAttribute("width", String.valueOf((Double.parseDouble(svgMainElement.getAttribute("width")) + routeMainRectWidth - 315)));
        }

        //next part handles drawing dotted line from starting pin to first route module
        Element svgRouteElement = svgDocument.getElementById("dotted_lines");

        PathObject pathObject = route.getFirst();
        if (pathObject instanceof GpioPathObject) {
            String pin = ((GpioPathObject) pathObject).getPin();
            String pinBackup = String.valueOf(pin);

            pin = pin.toLowerCase();
            Element currPin;
            if ((!pin.substring(0, 3).equals("ph7"))) {
                throw new IllegalArgumentException("Raspberry Pi 2+ only has PH7 pin header! You entered: " + pin + ".");
            } else {
                if (pin.contains("_")) {
                    if (Double.parseDouble(pin.substring(4)) > 40.0 || Double.parseDouble(pin.substring(4)) < 1.0) {
                        throw new IllegalArgumentException("Specified pin doesn't exist! (Pin format: \"ph7_number\", case is irrelevant. Raspberry Pi B+ has pins from ph7_1 to ph7_40, your was " + pin + ".)");
                    }
                    currPin = svgDocument.getElementById(pin);
                } else {
                    currPin = svgDocument.getElementById("ph7_20");
                }
            }

            Element svgRouteGroup = svgDocument.getElementById("route");
            String routeTransform = svgRouteGroup.getAttribute("transform");

            Pattern pattern = Pattern.compile("translate\\((\\d*)\\s(\\d*)\\)");
            Matcher matcher = pattern.matcher(routeTransform);

            double translateX = 0;
            double translateY = 0;

            while (matcher.find()) {
                translateX = Double.parseDouble(matcher.group(1));
                translateY = Double.parseDouble(matcher.group(2));
            }

            Element module1 = svgDocument.getElementById("module_1_rect");

            double currPinX = Double.parseDouble(currPin.getAttribute("x")) + 8;
            double currPinY = Double.parseDouble(currPin.getAttribute("y")) + 7;
            double module1x = Double.parseDouble(module1.getAttribute("x")) + translateX;
            double module1y = Double.parseDouble(module1.getAttribute("y")) + translateY;
            double module1height = Double.parseDouble(module1.getAttribute("height"));
            double module1middleY = module1y + (module1height/2);

            int offset = 35;
            routeDrawer.drawDottedLine(svgRouteElement, currPinX, currPinY, currPinX, (currPinY + module1middleY)/2, pinBackup);
            routeDrawer.drawDottedLine(svgRouteElement, currPinX, (currPinY + module1middleY)/2, module1x - offset, (currPinY + module1middleY)/2);
            routeDrawer.drawDottedLine(svgRouteElement, module1x - offset, (currPinY + module1middleY)/2, module1x - offset, module1middleY);
            routeDrawer.drawDottedLine(svgRouteElement, module1x - offset, module1middleY, module1x, module1middleY);
        }
    }
}
