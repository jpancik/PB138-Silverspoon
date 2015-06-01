package cz.muni.pb138.silverspoon_visualizer.parser;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public interface Route {

    /**
     * Gets first {@link cz.muni.pb138.silverspoon_visualizer.parser.PathObject} in the route.
     * If the route is represented as a tree, returns root of the tree.
     *
     * @return First object on path.
     */
    public PathObject getFirst();

    /**
     * Setter for the first object.
     *
     * @param first first object on route.
     */
    public void setFirst(PathObject first);

    public int getLength();
}
