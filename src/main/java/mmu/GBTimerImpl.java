package mmu;

import cpu.interrupts.GBInterruptManager;
import cpu.interrupts.InterruptType;

public class GBTimerImpl implements GBTimer {
    private int cycleCounter;

    private int numCyclesToCountTo;

    private final int CPU_FREQUENCY;

    private TimerSpeed timerSpeed;

    private boolean isEnabled;

    private int timerVal;

    private int timerResetValue; // set to 0 by default

    private GBInterruptManager interruptManager;

    private static final int TIMER_OVERFLOW_VALUE = 255;

    public GBTimerImpl(int CPU_FREQUENCY) {
        this.CPU_FREQUENCY = CPU_FREQUENCY;
        timerResetValue = 0;
    }

    /**
     * increments timerVal every numCyclesToCountTo
     * on overflow, timerVal is set to 0 and a Timer Interrupt is
     * requested
     * */
    public void notifyNumCycles(int numCycles) {
        if (!isEnabled()) return;

        cycleCounter += numCycles;

        if (cycleCounter >= numCyclesToCountTo) {
            ++timerVal;
            if (timerVal == TIMER_OVERFLOW_VALUE) {
                timerVal = timerResetValue;
                interruptManager.requestInterrupt(InterruptType.TIMER);
            }
        }
    }

    /**
     * Sets the clock speed if it's different from current,
     * numCyclesToCountTo is set based on the new timerSpeed
     * */
    public void setClockSpeed(TimerSpeed timerSpeed) {
        if (this.timerSpeed == timerSpeed) return;
        this.timerSpeed = timerSpeed;
        this.numCyclesToCountTo = CPU_FREQUENCY/timerSpeed.getFrequency();
    }

    private boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getTimerValue() {
        return timerVal;
    }

    public void setResetValue(int resetValue) {
        this.timerResetValue = resetValue;
    }
}
