package mmu;

/**
 * Represents GameBoy's Memory Management Unit
 * */
public interface MMU {

    /**
     * @return the value in memory at address
     **/
    int read(int address);

    /**
     * Writes data to memory at address
     * */
    void write(int address, int data);
}
