package cpu.clock;

import core.AbstractSubject;

/**
 * Subject class to write to CPU clock,
 * this is intentionally package protected
 * */
public abstract class ClockSubject extends AbstractSubject<ClockObserver> {

    /**
     * Notifies ClockObservers that CPU clock was incremented by increment
     * */
    protected abstract void notifyClockIncrement(int increment);
}
