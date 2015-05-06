package cz.muni.pb138.silverspoon_visualizer.parser;

/**
 * @author Norbert Slivka
 * @version 0.1
 * @since 5.5.2015
 */
public class FlowImpl implements Flow {

    private PathObject first;

    public FlowImpl() {
    }

    public void setFirst(PathObject first) {
        this.first = first;
    }

    @Override
    public PathObject getFirst() {
        return first;
    }

    @Override
    public String toString() {
        return "FLOW:  "+first;
    }
}
