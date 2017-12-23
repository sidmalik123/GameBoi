package mmu;

import cpu.clock.ClockObserver;
import mmu.memoryspaces.MemorySpace;
import mmu.memoryspaces.ROM;

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

    /**
     * Sets rom to be used as Read only memory (to load programs)
     * */
    void setROM(ROM rom);
}
