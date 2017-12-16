package cpu;

/**
 * Represents a micro operation in a GameBoy instruction
 * */
public interface MicroOp {

    /**
     * @return the number of CPU clock cycles,
     * it took to execute this micro operation
     * */
    int getNumCycles();
}
