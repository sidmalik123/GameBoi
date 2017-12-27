package cpu;

/**
 * Interface to represent a GameBoy CPU
 * */
public interface CPU {

    /**
     *  Runs the CPU so that it starts executing instructions
     * */
    void run();

    /**
     * Stops the CPU
     * */
    void stop();

    /**
     * Halts the CPU
     * */
    void halt();
}
