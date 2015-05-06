package cz.muni.pb138.silverspoon_visualizer.parser;

import java.util.List;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public interface PathObject {

    /**
     * Gets all direct successors of this object. Their relation to this is defined by {@link #getNextType()}.
     */
    public List<PathObject> getNext();

    /**
     * Gets relation between this element and all its successors.
     */
    public SuccessionTypes getNextType();

    public void setNextType(SuccessionTypes o);

    /**
     * Gets name to be displayed.
     */
    public String getName();

    public void setName(String o);

    /**
     * Gets additional information about this object.
     */
    public String getAdditionalInfo();

    public void setAdditionalInfo(String o);

    public void addNext(PathObject o);
}
