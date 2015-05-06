package cz.muni.pb138.silverspoon_visualizer.parser;

/**
 * @author Norbert Slivka
 * @version 0.1
 * @since 5.5.2015
 */
public class ParserException extends Exception {

    public ParserException() {
        super();
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
