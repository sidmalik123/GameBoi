package cpu;

import core.BaseException;

/**
 * Throw this when the instruction being processed is not known
 * */
public class UnknownInstructionException extends BaseException {

    public UnknownInstructionException(String msg) {
        super(msg);
    }
}
