package cpu.clock;

import com.google.inject.Inject;

/**
 * Attaches itself to the GameBoy clock
 * */
public abstract class AbstractClockObserver implements ClockObserver {

    @Inject
    public AbstractClockObserver(Clock clock) {
        clock.attach(this);
    }
}
