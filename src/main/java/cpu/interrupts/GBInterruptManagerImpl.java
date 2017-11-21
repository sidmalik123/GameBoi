package cpu.interrupts;


import java.util.List;
import java.util.Map;

public class GBInterruptManagerImpl implements GBInterruptManager {

    private boolean isEnabled; /* set/reset by cpu ops */

    private Map<InterruptType, GBInterrupt> interruptMap; // ordered by priority

    public void requestInterrupt(InterruptType interruptType) {
        interruptMap.get(interruptType).setRequested(true);
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    private boolean isEnabled() {
        return isEnabled;
    }


    /**
     * Returns the highest priority requested and enabled interrupt
     * resets it
     * */
    public GBInterrupt getCurrentInterrupt() {
        if (!isEnabled()) return null;

        for (GBInterrupt interrupt : interruptMap.values()) {
            if (interrupt.isRequested() && interrupt.isEnabled()) {
                interrupt.setRequested(false); // reset the interrupt
                return interrupt;
            }
        }

        return null;
    }


    /**
     * enables interrupts associated with the corresponding InterruptTypes
     * */
    public void enableInterrupts(List<InterruptType> enabledInterrupts) {
        for (InterruptType interruptType : enabledInterrupts) {
            interruptMap.get(interruptType).setEnabled(true);
        }
    }

}
