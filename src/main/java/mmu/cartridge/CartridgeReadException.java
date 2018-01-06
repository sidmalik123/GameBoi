package mmu.cartridge;

public class CartridgeReadException extends RuntimeException {
    public CartridgeReadException(String msg, Throwable err) {
        super(msg, err);
    }
}
