package interrupts;

import mmu.memoryspaces.MemorySpace;

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
     * Requests interrupt of type interrupt
     * */
    void requestInterrupt(Interrupt interrupt);

    /**
     * Returns the highest priority pending interrupt
     * */
    Interrupt getPendingInterrupt();
}
