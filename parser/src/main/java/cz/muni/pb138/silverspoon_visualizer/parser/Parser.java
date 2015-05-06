package cz.muni.pb138.silverspoon_visualizer.parser;

import java.io.InputStream;
import java.util.List;

/**
 * @author Norbert Slivka
 * @version 0.1
 * @since 5.5.2015
 */
public interface Parser {
    void setSource(String path) throws ParserException;
    void setSource(InputStream file) throws ParserException;

    void load() throws ParserException;

    List<Flow> getFlows();
}
