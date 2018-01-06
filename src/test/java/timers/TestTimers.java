package timers;

import interrupts.InterruptManager;
import interrupts.InterruptManagerImpl;
import mmu.MMU;
import mmu.MMUImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestTimers {

    private Timers timers;
    private MMU mmu;
    private InterruptManager interruptManager;

    public TestTimers() {
        mmu = new MMUImpl();
        interruptManager = new InterruptManagerImpl(mmu);
        timers = new TimersImpl(mmu, interruptManager);
    }

    @Test
    public void testDividerUpdate() {
        for (int dividerVal = 0; dividerVal <= 255; ++dividerVal) {
            for (int i = 0; i < 256; ++i) {
                timers.handleClockIncrement(1);
                if (i < 255) {
                    assertEquals(mmu.read(MMU.DIVIDER_REGISTER_ADDRESS), dividerVal);
                } else { // incremented after 256 cycles
                    if (dividerVal < 255) {
                        assertEquals(mmu.read(MMU.DIVIDER_REGISTER_ADDRESS), dividerVal + 1);
                    } else {
                        assertEquals(mmu.read(MMU.DIVIDER_REGISTER_ADDRESS), 0); // gets reset
                    }
                }
            }
        }
    }
}
