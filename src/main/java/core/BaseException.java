package core;

/**
 * Base exception with message
 * */
public class BaseException extends RuntimeException {

    public BaseException(String msg) {
        super(msg);
    }
}
