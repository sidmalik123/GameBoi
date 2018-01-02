package timers;

import com.google.inject.Inject;
import core.BitUtils;
import interrupts.Interrupt;
import interrupts.InterruptManager;
import mmu.MMU;

/**
 * Concrete class for Timer
 * */
public class TimerImpl implements Timer {

    private MMU mmu;
    private InterruptManager interruptManager;

    public static final int CLOCK_ENABLED_BIT = 2;

    private static final int NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ0 = 1024; // Freq = 4096
    private static final int NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ1 = 16; // Freq = 262144
    private static final int NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ2 = 64; // Freq = 65536
    private static final int NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ3 = 256; // Freq = 16382
    private static final int MAX_TIMER_VALUE = 255;

    private int numCyclesSinceLastIncrement;
    private int numCyclesToIncrementAfter;

    @Inject
    public TimerImpl(MMU mmu, InterruptManager interruptManager) {
        this.mmu = mmu;
        this.interruptManager = interruptManager;
        setNumCyclesToIncrementAfter();
    }

    @Override
    public void handleClockIncrement(int increment) {
        if (isClockEnabled()) {
            numCyclesSinceLastIncrement += increment;
            if (numCyclesSinceLastIncrement >= numCyclesToIncrementAfter) {
                numCyclesSinceLastIncrement = 0;
                setNumCyclesToIncrementAfter(); // check after every increment

                if (getCurrentTimerValue() == MAX_TIMER_VALUE) {
                    mmu.write(MMU.TIMER_VALUE_ADDRESS, mmu.read(MMU.TIMER_RESET_VALUE_ADDRESS)); // reset timer
                    interruptManager.requestInterrupt(Interrupt.TIMER);
                } else {
                    mmu.write(MMU.TIMER_VALUE_ADDRESS, mmu.read(MMU.TIMER_VALUE_ADDRESS) + 1); // increment timer
                }
            }
        }
    }

    private boolean isClockEnabled() {
        return BitUtils.isBitSet(mmu.read(MMU.TIMER_CONTROLS_ADDRESS), CLOCK_ENABLED_BIT);
    }

    private void setNumCyclesToIncrementAfter() {
        switch (mmu.read(MMU.TIMER_CONTROLS_ADDRESS) & 0b11) {
            case 0: numCyclesToIncrementAfter = NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ0; break;
            case 1: numCyclesToIncrementAfter = NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ1; break;
            case 2: numCyclesToIncrementAfter = NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ2; break;
            case 3: numCyclesToIncrementAfter = NUM_CYLES_TO_INCREMENT_AFTER_FOR_FREQ3; break;
        }
    }

    private int getCurrentTimerValue() {
        return mmu.read(MMU.TIMER_VALUE_ADDRESS);
    }
}
