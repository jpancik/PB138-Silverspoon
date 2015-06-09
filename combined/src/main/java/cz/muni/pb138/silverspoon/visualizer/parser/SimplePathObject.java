package cz.muni.pb138.silverspoon.visualizer.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public class SimplePathObject implements PathObject {

    private List<PathObject> next;
    private SuccessionTypes nextType;
    private String name;
    private String additionalInformation;
    
    public SimplePathObject() {
        next=new ArrayList<PathObject>();
        name=null;
        additionalInformation = null;
        nextType=null;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addNext(PathObject o) {
        next.add(o);
    }
    @Override
    public void setNextType(SuccessionTypes o) {
        nextType = o;
    }

    @Override
    public void setAdditionalInfo(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }


    @Override
    public List<PathObject> getNext() {
        return Collections.unmodifiableList(next);
    }

    @Override
    public SuccessionTypes getNextType() {
        return nextType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAdditionalInfo() {
        return additionalInformation;
    }




    public static class SimplePathObjectBuilder {
        private SimplePathObject obj;

        public SimplePathObjectBuilder() {
            obj = new SimplePathObject();
        }

        public SimplePathObjectBuilder addNext(PathObject o) {
            obj.addNext(o);
            return this;
        }

        public SimplePathObjectBuilder setNextType(SuccessionTypes o) {
            obj.setNextType(o);
            return this;
        }

        public SimplePathObjectBuilder setName(String name) {
            obj.setName(name);
            return this;
        }

        public SimplePathObjectBuilder setAdditionalInfo(String additionalInformation) {
            obj.setAdditionalInfo(additionalInformation);
            return this;
        }

        public SimplePathObject build() {
            return obj;
        }
    }

    @Override
    public String toString() {
        return " "+name+getNextType()+(getNext().size() > 0 ?getNext().get(0) : "");

    }
}
