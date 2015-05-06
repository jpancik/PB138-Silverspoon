package cz.muni.pb138.silverspoon_visualizer.parser;

import java.util.List;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public interface PathObject {

    List<PathObject> getNext();
    SuccessionTypes getNextType();
    String getName();
    String getAdditionalInfo();

    void addNext(PathObject o);
    void setNextType(SuccessionTypes o);
    void setName(String o);
    void setAdditionalInfo(String o);

}
