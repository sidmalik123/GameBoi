package cpu;

/**
 * All accesses to memory from the CPU must be from this interface
 *
 * this interface takes care of the number of clock cycles it takes for the cpu to access memory
 * */
public interface MemoryAccessor {

    /**
     * Read from memory at address
     * */
    int read(int address);

    /**
     * Write data to memory at address
     * */
    void write(int address, int data);
}
