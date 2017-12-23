package mmu;

import cpu.clock.ClockObserver;

/**
 * Represents GameBoy's Memory Management Unit
 * */
public interface MMU extends ClockObserver {

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
}
