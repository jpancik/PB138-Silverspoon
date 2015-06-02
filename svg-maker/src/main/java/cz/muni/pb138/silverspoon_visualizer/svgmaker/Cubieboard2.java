package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import cz.muni.pb138.silverspoon_visualizer.parser.GpioPathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.PathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import cz.muni.pb138.silverspoon_visualizer.parser.SuccessionTypes;
import org.w3c.dom.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents Cubieboard2. It holds path to it and is responsible for generating some parts of the route.
 *
 * @author Martin Zilak
 * @version 2015/06/02
 */
public class Cubieboard2 extends Board {
    public Cubieboard2() {
        super("/cubieboard.svg");
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

        double boardTranslate = 0;

        //next part handles expanding the .svg
        double routeMainRectWidth = Double.parseDouble(svgDocument.getElementById("main_rect").getAttribute("width"));
        if (routeMainRectWidth > 365) {
            boardTranslate = routeMainRectWidth - 365;
            
            Element svgBodyRect = svgDocument.getElementById("body");
            svgBodyRect.setAttribute("width", String.valueOf((Double.parseDouble(svgBodyRect.getAttribute("width")) + boardTranslate)));

            Element svgMovableGroup = svgDocument.getElementById("movable");
            svgMovableGroup.setAttribute("transform", "translate(" + boardTranslate + " 0)");

            Element svgMainElement = svgDocument.getDocumentElement();
            svgMainElement.setAttribute("width", String.valueOf((Double.parseDouble(svgMainElement.getAttribute("width")) + boardTranslate)));
        }

        //next part handles drawing dotted line from starting pin to first route module
        Element svgRouteElement = svgDocument.getElementById("dotted_lines");

        double translateX = 0;
        double translateY = 0;

        Element svgRouteGroup = svgDocument.getElementById("route");
        String routeTransform = svgRouteGroup.getAttribute("transform");

        Pattern pattern = Pattern.compile("translate\\((\\d*)\\s(\\d*)\\)");
        Matcher matcher = pattern.matcher(routeTransform);

        while (matcher.find()) {
            translateX = Double.parseDouble(matcher.group(1));
            translateY = Double.parseDouble(matcher.group(2));
        }

        PathObject pathObject = route.getFirst();
        if (pathObject instanceof GpioPathObject) {
            String pin = ((GpioPathObject) pathObject).getPin();
            String pinBackup = String.valueOf(pin);

            pin = pin.toLowerCase();
            Element currPin;

            if ((!(pin.substring(0, 1).equals("1") || pin.substring(0, 1).equals("2")))) {
                throw new IllegalArgumentException("Cubieboard2 only has 1 and 2 pin headers! You entered: " + pin + ".");
            } else {
                if (pin.contains("_")) {
                    if (Double.parseDouble(pin.substring(2)) > 48.0 || Double.parseDouble(pin.substring(2)) < 1.0) {
                        throw new IllegalArgumentException("Specified pin doesn't exist! (Pin format: \"pinheader_number\", case is irrelevant, pinheader is either 1 or 2. Cubieboard2 has pins from pinheader_1 to pinheader_48, your was " + pin + ".)");
                    }
                    currPin = svgDocument.getElementById(pin);
                } else {
                    currPin = svgDocument.getElementById("1_24");
                }
            }

            Element firstModule = svgDocument.getElementById("module_1_rect");

            double currPinX = Double.parseDouble(currPin.getAttribute("x")) + 7;
            double currPinY = Double.parseDouble(currPin.getAttribute("y")) + 7;
            double firstModuleX = Double.parseDouble(firstModule.getAttribute("x")) + translateX;
            double firstModuleY = Double.parseDouble(firstModule.getAttribute("y")) + translateY;
            double firstModuleHeight = Double.parseDouble(firstModule.getAttribute("height"));
            double firstModuleMiddleY = firstModuleY + (firstModuleHeight/2);

            int offset = 35;
            routeDrawer.drawDottedLine(svgRouteElement, currPinX, currPinY, currPinX, (currPinY + firstModuleMiddleY)/2, pinBackup);
            routeDrawer.drawDottedLine(svgRouteElement, currPinX, (currPinY + firstModuleMiddleY)/2, firstModuleX - offset, (currPinY + firstModuleMiddleY)/2, false);
            routeDrawer.drawDottedLine(svgRouteElement, firstModuleX - offset, (currPinY + firstModuleMiddleY)/2, firstModuleX - offset, firstModuleMiddleY, false);
            routeDrawer.drawDottedLine(svgRouteElement, firstModuleX - offset, firstModuleMiddleY, firstModuleX, firstModuleMiddleY, true);
        }

        //next part handles drawing dotted line from last route module to route end
        while (pathObject.getNextType() == SuccessionTypes.STRAIGHT) {
            pathObject = pathObject.getNext().get(0);
        }

        if (pathObject.getNextType() == SuccessionTypes.ETHERNET) {
            Element ethernet = svgDocument.getElementById("ethernet");

            double ethernetX = Double.parseDouble(ethernet.getAttribute("x")) + boardTranslate;
            double ethernetY = Double.parseDouble(ethernet.getAttribute("y"));
            double ethernetWidth = Double.parseDouble(ethernet.getAttribute("width"));
            double ethernetTwoThirdsX = ethernetX + (ethernetWidth * 0.66);

            int moduleCount = route.getLength();
            Element lastModule = svgDocument.getElementById("module_" + String.valueOf(moduleCount) + "_rect");

            double lastModuleX = Double.parseDouble(lastModule.getAttribute("x")) + Double.parseDouble(lastModule.getAttribute("width")) + translateX;
            double lastModuleY = Double.parseDouble(lastModule.getAttribute("y")) + translateY;
            double lastModuleHeight = Double.parseDouble(lastModule.getAttribute("height"));
            double lastModuleMiddleY = lastModuleY + (lastModuleHeight/2);

            routeDrawer.drawDottedLine(svgRouteElement, lastModuleX, lastModuleMiddleY, ethernetTwoThirdsX, lastModuleMiddleY, false);
            routeDrawer.drawDottedLine(svgRouteElement, ethernetTwoThirdsX, lastModuleMiddleY, ethernetTwoThirdsX, ethernetY, true);
        }
    }
}
