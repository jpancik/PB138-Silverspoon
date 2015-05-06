package cz.muni.pb138.silverspoon_visualizer.parser;

/**
 * @author Norbert Slivka
 * @version 0.1
 * @since 5.5.2015
 */
public class RouteImpl implements Route {

    private PathObject first;

    public RouteImpl() {
    }

    @Override
    public PathObject getFirst() {
        return first;
    }

    public void setFirst(PathObject first) {
        this.first = first;
    }

    @Override
    public String toString() {
        return "ROUTE: " + first;
    }
}
