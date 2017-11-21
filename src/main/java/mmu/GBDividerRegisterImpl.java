package mmu;


/**
 * Emulates the divider register in GameBoy
 * */
public class GBDividerRegisterImpl implements GBTimer {

    private final int SELF_FREQUENCY = 16382;

    private final int CYCLES_TO_COUNT_TO;

    private int cycleCount;

    private final int CPU_FREQUENCY;

    private int timerVal;

    private static final int TIMER_OVERFLOW_VALUE = 255;

    public GBDividerRegisterImpl(int CPU_FREQUENCY) {
        this.CPU_FREQUENCY = CPU_FREQUENCY;
        CYCLES_TO_COUNT_TO = CPU_FREQUENCY/SELF_FREQUENCY;
        cycleCount = 0;
    }

    public void notifyNumCycles(int numCycles) {
        cycleCount += numCycles;
        if (cycleCount >= CYCLES_TO_COUNT_TO) {
            cycleCount = 0;
            ++timerVal;
            if (timerVal == TIMER_OVERFLOW_VALUE) {
                timerVal = 0;
            }
        }
    }


    /**
     * Not implemented - one clock speed for the divider register
     * */
    public void setClockSpeed(TimerSpeed timerSpeed) {

    }

    /**
     * Not implemented this is always enabled
     * */
    public void setEnabled(boolean enabled) {

    }

    public int getTimerValue() {
        return timerVal;
    }


    /**
     * Not implemented reset value is always 0
     * */
    public void setResetValue(int resetValue) {

    }
}
