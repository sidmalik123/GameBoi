package mmu;

import mmu.memoryspaces.ROM;

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

    /**
     * loads program into memory
     * */
    void load(int[] program);

    /**
     * Sets rom to be used as Read only memory (to load programs)
     * */
    void setROM(ROM rom);
}
