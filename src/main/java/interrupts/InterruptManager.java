package interrupts;

import mmu.memoryspaces.MemorySpace;

import java.util.List;

/**
 * Interface to manage GameBoy interrupts
 * */
public interface InterruptManager extends MemorySpace {
    int INTERRUPT_REQUEST_REGISTER = 0xFF0F;
    int INTERRUPT_ENABLE_REGISTER = 0xFFFF;

    /**
     * sets global setting to enable/disable interrupts
     * */
    void setInterruptsEnabled(boolean areInterruptsEnabled);

    /**
     * Requests interrupt of type interruptType
     * */
    void requestInterrupt(InterruptType interruptType);

    /**
     * Returns a list of pending interrupts
     * */
    List<InterruptType> getPendingInterrupts();
}
