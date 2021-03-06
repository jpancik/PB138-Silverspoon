package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import cz.muni.pb138.silverspoon_visualizer.parser.GpioPathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.PathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.Route;
import cz.muni.pb138.silverspoon_visualizer.parser.SuccessionTypes;
import org.w3c.dom.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents Beaglebone Black. It holds path to it and is responsible for generating some parts of the route.
 *
 * @author Martin Zilak
 * @version 2015/06/02
 */
public class BeagleboneBlack extends Board {

    public BeagleboneBlack() {
        super("/beaglebone.svg");
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
        if (routeMainRectWidth > 275) {
            boardTranslate = routeMainRectWidth - 275;
            
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

        boolean routeFromStartAbove = true;

        PathObject pathObject = route.getFirst();
        if (pathObject instanceof GpioPathObject) {
            String pin = ((GpioPathObject) pathObject).getPin();
            String pinBackup = String.valueOf(pin);

            pin = pin.toLowerCase();
            Element currPin;

            if ((!(pin.substring(0, 2).equals("p8") || pin.substring(0, 2).equals("p9")))) {
                throw new IllegalArgumentException("Beaglebone Black only has p8 and p9 pin headers! You entered: " + pin + ".");
            } else {
                if (pin.contains("_")) {
                    if (Double.parseDouble(pin.substring(3)) > 46.0 || Double.parseDouble(pin.substring(3)) < 1.0) {
                        throw new IllegalArgumentException("Specified pin doesn't exist! (Pin format: \"pinheader_number\", case is irrelevant, pinheader is either p8 or p9. Beaglebone Black has pins from pinheader_1 to pinheader_46, your was " + pin + ".)");
                    }
                    currPin = svgDocument.getElementById(pin);
                } else {
                    currPin = svgDocument.getElementById("p8_23");
                }
            }

            Element firstModule = svgDocument.getElementById("module_1_rect");

            double currPinX = Double.parseDouble(currPin.getAttribute("x")) + 7;
            double currPinY = Double.parseDouble(currPin.getAttribute("y")) + 7;
            double firstModuleX = Double.parseDouble(firstModule.getAttribute("x")) + translateX;
            double firstModuleY = Double.parseDouble(firstModule.getAttribute("y")) + translateY;
            double firstModuleHeight = Double.parseDouble(firstModule.getAttribute("height"));
            double firstModuleMiddleY = firstModuleY + (firstModuleHeight/2);

            if (currPinY > firstModuleMiddleY) {
                routeFromStartAbove = false;
            }

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

            double ethernetX = Double.parseDouble(ethernet.getAttribute("x"));
            double ethernetY = Double.parseDouble(ethernet.getAttribute("y"));
            double ethernetHeight = Double.parseDouble(ethernet.getAttribute("height"));
            double ethernetWidth = Double.parseDouble(ethernet.getAttribute("width"));
            double ethernetTwoThirdsX = ethernetX + (ethernetWidth * 0.66);

            int moduleCount = route.getLength();
            Element lastModule = svgDocument.getElementById("module_" + String.valueOf(moduleCount) + "_rect");

            double lastModuleX = Double.parseDouble(lastModule.getAttribute("x")) + Double.parseDouble(lastModule.getAttribute("width")) + translateX;
            double lastModuleY = Double.parseDouble(lastModule.getAttribute("y")) + translateY;
            double lastModuleHeight = Double.parseDouble(lastModule.getAttribute("height"));
            double lastModuleMiddleY = lastModuleY + (lastModuleHeight/2);

            int spacer = 35;
            routeDrawer.drawDottedLine(svgRouteElement, lastModuleX, lastModuleMiddleY, lastModuleX + spacer, lastModuleMiddleY, false);
            if (routeFromStartAbove) {
                routeDrawer.drawDottedLine(svgRouteElement, lastModuleX + spacer ,lastModuleMiddleY, lastModuleX + spacer, ethernetY + ethernetHeight + spacer, false);
                routeDrawer.drawDottedLine(svgRouteElement, lastModuleX + spacer, ethernetY + ethernetHeight + spacer, ethernetTwoThirdsX, ethernetY + ethernetHeight + spacer, false);
                routeDrawer.drawDottedLine(svgRouteElement, ethernetTwoThirdsX, ethernetY + ethernetHeight + spacer, ethernetTwoThirdsX, ethernetY + ethernetHeight, true);
            } else {
                routeDrawer.drawDottedLine(svgRouteElement, lastModuleX + spacer ,lastModuleMiddleY, lastModuleX + spacer, ethernetY - spacer, false);
                routeDrawer.drawDottedLine(svgRouteElement, lastModuleX + spacer, ethernetY - spacer, ethernetTwoThirdsX, ethernetY - spacer, false);
                routeDrawer.drawDottedLine(svgRouteElement, ethernetTwoThirdsX, ethernetY - spacer, ethernetTwoThirdsX, ethernetY, true);
            }
        }
    }
}
