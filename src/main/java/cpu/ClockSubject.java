package cpu;

import core.AbstractSubject;

/**
 * Subject class to write to CPU clock,
 * this is intentionally package protected
 * */
abstract class ClockSubject extends AbstractSubject<ClockObserver> {

    /**
     * Notifies ClockObservers that CPU clock was incremented by increment
     * */
    abstract void notifyClockIncrement(int increment);
}
