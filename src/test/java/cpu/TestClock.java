package cpu;

import cpu.clock.AbstractClockObserver;
import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import org.junit.Test;

public class TestClock {

    private Clock clock = new ClockImpl();
    private int numObserversNotified;

    private class MockClockObserver extends AbstractClockObserver {

        public MockClockObserver(Clock clock) {
            super(clock);
        }

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
        MockClockObserver mock1 = new MockClockObserver(clock);
        MockClockObserver mock2 = new MockClockObserver(clock);
        MockClockObserver mock3 = new MockClockObserver(clock);

        clock.addCycles(10);

        assert (numObserversNotified == 3);
        assert (clock.getTotalCycles() == 10);

        clock.addCycles(20);

        assert (clock.getTotalCycles() == 30);
    }
}
