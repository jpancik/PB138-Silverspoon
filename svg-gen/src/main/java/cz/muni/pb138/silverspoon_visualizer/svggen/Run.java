package cz.muni.pb138.silverspoon_visualizer.svggen;

import java.io.IOException;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public class Run {
    public static void main(String ... args) throws IOException {
        LoadFile lf = new LoadFile();
        lf.load("beaglebone_black.svg");
    }
}
