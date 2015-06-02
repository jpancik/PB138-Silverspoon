package cz.muni.pb138.silverspoon_visualizer.svgmaker;

import cz.muni.pb138.silverspoon_visualizer.parser.GpioPathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.PathObject;
import cz.muni.pb138.silverspoon_visualizer.parser.Route;
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

        double routeMainRectWidth = Double.parseDouble(svgDocument.getElementById("main_rect").getAttribute("width"));
        if (routeMainRectWidth > 275) {
            Element svgBodyRect = svgDocument.getElementById("body");
            svgBodyRect.setAttribute("width", String.valueOf((Double.parseDouble(svgBodyRect.getAttribute("width")) + routeMainRectWidth - 275)));

            Element svgMovableGroup = svgDocument.getElementById("movable");
            svgMovableGroup.setAttribute("transform", "translate(" + (routeMainRectWidth - 275) + " 0)");

            Element svgMainElement = svgDocument.getDocumentElement();
            svgMainElement.setAttribute("width", String.valueOf((Double.parseDouble(svgMainElement.getAttribute("width")) + routeMainRectWidth - 275)));
        }

        Element svgRouteElement = svgDocument.getElementById("dotted_lines");

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

            double currPinX = Double.parseDouble(currPin.getAttribute("x")) + 7;
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
