package mmu;

/**
 * Represents a part of GameBoy's memory
 * */
public interface MemorySpace {

    /**
     * Checks if address falls in this memory space
     * */
    boolean accepts(int address);

    /**
     * Returns the byte at address
     * */
    int read(int address);

    /**
     * Sets byte at address to have value data
     * */
    void write(int address, int data);
}
