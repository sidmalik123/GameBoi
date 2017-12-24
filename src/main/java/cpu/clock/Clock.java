package cpu.clock;

/**
 * Represents the CPU clock
 * */
public interface Clock {

    /**
     * Adds numCycles cycles to the clock
     * */
    void addCycles(int numCycles);

    /**
     * Returns total cycles elapsed since start
     * */
    int getTotalCycles();

    /**
     * Adds observer to the list of observers
     * */
    void attach(ClockObserver observer);
}
