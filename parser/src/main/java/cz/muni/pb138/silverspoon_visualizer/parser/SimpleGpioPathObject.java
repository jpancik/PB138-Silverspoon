package cz.muni.pb138.silverspoon_visualizer.parser;

import java.util.List;

/**
 * @author Norbert Slivka
 * @version 0.1
 * @since 5.5.2015
 */
public class SimpleGpioPathObject implements GpioPathObject {

    private SimplePathObject obj;
    private String pin;

    private SimpleGpioPathObject() {
        obj = new SimplePathObject();
        pin = null;
    }

    @Override
    public List<PathObject> getNext() {
        return obj.getNext();
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public SuccessionTypes getNextType() {
        return obj.getNextType();
    }

    @Override
    public void setNextType(SuccessionTypes o) {
        obj.setNextType(o);
    }

    @Override
    public String getName() {
        return obj.getName();
    }

    @Override
    public void setName(String o) {
        obj.setName(o);
    }

    @Override
    public String getAdditionalInfo() {
        return obj.getAdditionalInfo();
    }

    @Override
    public void setAdditionalInfo(String o) {
        obj.setAdditionalInfo(o);
    }

    @Override
    public void addNext(PathObject o) {
        obj.addNext(o);
    }

    private void setObj(SimplePathObject o) {
        obj = o;
    }

    @Override
    public String toString() {
        return " " + obj.getName() + "@" + pin + obj.getNextType() + (obj.getNext().size() > 0 ? obj.getNext().get(0) : "");
    }

    public static class GpioPathObjectBuilder {

        private SimpleGpioPathObject obj;

        public GpioPathObjectBuilder() {
            obj = new SimpleGpioPathObject();
        }

        public GpioPathObjectBuilder addNext(PathObject o) {
            obj.addNext(o);
            return this;
        }

        public GpioPathObjectBuilder setNextType(SuccessionTypes o) {
            obj.setNextType(o);
            return this;
        }

        public GpioPathObjectBuilder setName(String name) {
            obj.setName(name);
            return this;
        }

        public GpioPathObjectBuilder setAdditionalInfo(String additionalInformation) {
            obj.setAdditionalInfo(additionalInformation);
            return this;
        }

        public GpioPathObjectBuilder setPin(String pin) {
            obj.pin = pin;
            return this;
        }

        public SimpleGpioPathObject build() {
            return obj;
        }
    }
}
