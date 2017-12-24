package core.exceptions;

/**
 * Throw this when instruction is being processed is not known
 * */
public class UnknownInstructionException extends RuntimeException {

    public UnknownInstructionException(String msg) {
        super(msg);
    }
}
