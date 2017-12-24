package mmu;

import core.BaseException;

public class ReadOnlyMemoryException extends BaseException {
    public ReadOnlyMemoryException(String msg) {
        super(msg);
    }
}
