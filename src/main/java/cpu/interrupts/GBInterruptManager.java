package cpu.interrupts;


import java.util.List;

/**
 * Interface to manage GameBoy interrupts
 * */
public interface GBInterruptManager {

    /**
     * Requests an interrupt of type interruptType
     * */
    void requestInterrupt(InterruptType interruptType);

    /**
     * sets global setting to enable/disable interrupts
     * */
    void setEnabled(boolean isEnabled);

    /**
     * Returns the current active interrupt
     * */
    GBInterrupt getCurrentInterrupt();

    /**
     * Enables all interupts present in enabledInterrupts
     * */
    void enableInterrupts(List<InterruptType> enabledInterrupts);
}
