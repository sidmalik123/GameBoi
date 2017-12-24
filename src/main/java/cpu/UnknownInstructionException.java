package cpu;

import core.BaseException;

/**
 * Throw this when instruction is being processed is not known
 * */
public class UnknownInstructionException extends BaseException {

    public UnknownInstructionException(String msg) {
        super(msg);
    }
}
