package mmu;

/**
 * Interface to represent a general memory space
 * */
public interface MemorySpace {

    /**
     * reads data from the address passed in
     * */
    int read(int address);

    /**
     * writes data passed to the address passed in
     * */
    void write(int address, int data);

    /**
     * Is this memory space read only
     * */
    boolean isReadOnly();
}
