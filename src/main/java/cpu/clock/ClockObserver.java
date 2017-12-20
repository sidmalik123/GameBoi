package cpu.clock;

public interface ClockObserver {

    /**
     * method to handle CPU clock increment
     * */
    void handleClockIncrement(int increment);
}
