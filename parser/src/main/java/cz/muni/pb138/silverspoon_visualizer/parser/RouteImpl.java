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
    public int getLength() {
        PathObject o = getFirst();
        if (o == null) {
            return 0;
        }
        return getLen(o);
    }

    private int getLen(PathObject o) {
        int mx = -1;
        if (o.getNext().isEmpty()) {
            return 1;
        }
        for (PathObject i : o.getNext()) {
            mx = max(mx, getLen(i));
        }
        mx += 1;
        return mx;
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    @Override
    public String toString() {
        return "ROUTE (len = " + getLength() + "): " + first;
    }
}
