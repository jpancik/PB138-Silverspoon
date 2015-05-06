package cz.muni.pb138.silverspoon_visualizer.parser;

/**
 * @author Norbert Slivka
 * @version 0.1
 */
public enum SuccessionTypes {
    /**
     * Simple connection, goes to one next {@link PathObject}.
     */
    STRAIGHT(1,2, " -> "),
    /**
     * Should go into ethernet connection.
     */
    ETHERNET(0,1, " >E")
    ;

    private int from, to;
    private String format;

    /**
     * {@code to} has to be higher than {@code from}. The interval is half-closed.
     * @param from
     * @param to
     */
    SuccessionTypes(int from, int to, String format) {
        if (from >= to) {
            throw new IllegalArgumentException("From has to be lower than To.");
        }
        this.from = from;
        this.to = to;
        this.format = format;
    }

    /**
     *
     * @param x Number of elements.
     *
     * @return True iff {@code x} is in range from {@link #getFrom()} to {@link #getTo()}.
     */
    public boolean inInterval(int x) {
        return x < to && x >= from;
    }

    public int getFrom() {
        return from;
    }


    public int getTo() {
        return to;
    }

    @Override
    public String toString() {
        return format;
    }
}
