package cpu.interrupts;

public class GBInterruptImpl implements GBInterrupt {
    /* interruptType represents the type of interrupt this interrupt is*/
    private InterruptType interruptType;
    private boolean isEnabled;
    private boolean isRequested;

    public GBInterruptImpl(InterruptType interruptType) {
        this.interruptType = interruptType;
    }
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isRequested() {
        return isRequested;
    }

    public void setRequested(boolean requested) {
        this.isRequested = requested;
    }

    public InterruptType getInterruptType() {
        return interruptType;
    }
}
