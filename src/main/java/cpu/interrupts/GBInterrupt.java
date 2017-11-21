package cpu.interrupts;

/**
 * Interface to represent a GameBoy interrupt
 * */
public interface GBInterrupt {

    /**
     * Checks if this interrupt is enabled on the system
     * or not
     * */
    boolean isEnabled();

    /**
     * Enabled this interrupt on the system
     * */
    void setEnabled(boolean enabled);


    /**
     * Requests this interrupt
     * */
    boolean isRequested();

    /**
     * Checks if this interrupt is requested or not
     * */
    void setRequested(boolean requested);


    /**
     * Returns the type of this interrupt
     * */
    InterruptType getInterruptType();
}
