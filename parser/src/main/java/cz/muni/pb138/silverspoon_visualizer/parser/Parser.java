package cz.muni.pb138.silverspoon_visualizer.parser;

import java.io.InputStream;
import java.util.List;

/**
 * @author Norbert Slivka
 * @version 0.1
 * @since 5.5.2015
 */
public interface Parser {

    /**
     * Used to set the source file.
     * Also see: {@link #setSource(java.io.InputStream)}
     *
     * @param path Path to the file starting at the project location.
     *
     * @throws ParserException if the file can not be found or can not be parsed as an XML document.
     */
    public void setSource(String path) throws ParserException;

    /**
     * Loads the data as an XML.
     *
     * @param file XML data stream.
     *
     * @throws ParserException if the data can not be parsed as an XML document.
     */
    public void setSource(InputStream file) throws ParserException;

    /**
     * Parses the data specified by either of {@code setSource}. Must be called before {@link #getRoutes()} can fetch
     * any routes.
     *
     * @throws ParserException if any of the routes contains malformed elements.
     */
    public void load() throws ParserException;

    /**
     * Gets all the routes.
     */
    public List<Route> getRoutes();
}
