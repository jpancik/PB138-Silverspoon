package cz.muni.pb138.silverspoon_visualizer.parser;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public interface GpioPathObject extends PathObject {

    public String getPin();

    public void setPin(String name);
}
