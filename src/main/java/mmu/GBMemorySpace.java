package mmu;

/**
 * Interface to represent a GameBoy Ram memory space
 * */
public interface GBMemorySpace extends MemorySpace {

    /**
     * Returns the size of this memory space in bytes
     * */
    int getMemorySize();

    /**
     * Returns the address at which this
     * memory space starts in the RAM
     * */
    int getStartAddress();

    /**
     * Returns the address at which this
     * memory space ends in the RAM
     * */
    int getEndAddress();
}
