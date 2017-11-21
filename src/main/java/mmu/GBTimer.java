package mmu;

import core.TimingObserver;

/**
 * Emulates the memory timer in GameBoy
 * */
public interface GBTimer extends TimingObserver {
    /**
     * Sets the clock speed of the timer to timerSpeed
     */
    void setClockSpeed(TimerSpeed timerSpeed);

    /**
     * Enable/Disable the timer
     */
    void setEnabled(boolean enabled);

    /**
     * Returns the current timer value
     * */
    int getTimerValue();

    /**
     * Sets the value the timer gets set to on overflow
     * */
    void setResetValue(int resetValue);
}
