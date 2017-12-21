package mmu.memoryspaces;

/**
 * Represents a part of GameBoy's memory
 *
 * This is intentionally package protected because
 * I want memory to be read and set by MMU APIs
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
