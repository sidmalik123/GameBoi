package cpu.clock;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class for Clock
 * */
@Singleton
public class ClockImpl implements Clock {

    private int totalNumCycles;
    private List<ClockObserver> observers;

    public ClockImpl() {
        totalNumCycles = 0;
        observers = new ArrayList<>();
    }

    /**
     * Adds to the totat count, notify observers
     * */
    @Override
    public void addCycles(int numCycles) {
        totalNumCycles += numCycles;
        for (ClockObserver observer : observers)
            observer.handleClockIncrement(numCycles);
    }

    @Override
    public int getTotalCycles() {
        return totalNumCycles;
    }

    @Override
    public void attach(ClockObserver observer) {
        observers.add(observer);
    }
}
