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
    int getByte(int address);

    /**
     * Sets byte at address to have value data
     * */
    void setByte(int address, int data);
}
