package interrupts;

/**
 * Interface to manage GameBoy interrupts
 * */
public interface InterruptManager {
    /**
     * sets global setting to enable/disable interrupts
     * */
    void setInterruptsEnabled(boolean areInterruptsEnabled);

    /**
     * Requests interrupt of type interrupt
     * */
    void requestInterrupt(Interrupt interrupt);

    /**
     * Returns the highest priority pending interrupt
     * */
    Interrupt getPendingInterrupt();
}
