package cpu;

import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import cpu.clock.ClockObserver;
import mmu.TestCartridge;
import org.junit.Test;

public class TestClock {

    private Clock clock = new ClockImpl();
    private int numObserversNotified;

    private class MockClockObserver implements ClockObserver {

        @Override
        public void handleClockIncrement(int increment) {
            ++numObserversNotified;
        }
    }

    @Test
    public void testClockStartsAtZero() {
        assert (clock.getTotalCycles() == 0);
    }

    @Test
    public void testClock() {
        MockClockObserver mock1 = new MockClockObserver();
        MockClockObserver mock2 = new MockClockObserver();
        MockClockObserver mock3 = new MockClockObserver();

        clock.attach(mock1);
        clock.attach(mock2);
        clock.attach(mock3);

        clock.addCycles(10);

        assert (numObserversNotified == 3);
        assert (clock.getTotalCycles() == 10);

        clock.addCycles(20);

        assert (clock.getTotalCycles() == 30);
    }
}
