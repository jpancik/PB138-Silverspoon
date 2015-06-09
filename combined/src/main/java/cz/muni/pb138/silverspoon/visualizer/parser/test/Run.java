package cz.muni.pb138.silverspoon.visualizer.parser.test;

import cz.muni.pb138.silverspoon.visualizer.parser.Parser;
import cz.muni.pb138.silverspoon.visualizer.parser.ParserException;
import cz.muni.pb138.silverspoon.visualizer.parser.ParserImpl;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public class Run {
    public static  void main(String ... args) {
        Parser parser = new ParserImpl();
        try {
            parser.setSource("/t1.xml");
            parser.load();
        } catch (ParserException e) {
            e.printStackTrace();
        }
        System.out.println(parser.getRoutes().get(0));
    }
}
