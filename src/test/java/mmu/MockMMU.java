package mmu;

import cpu.clock.Clock;
import mmu.memoryspaces.ROM;

/**
 * Mock class all memory is one big array of 0xFFFF bytes
 * */
public class MockMMU implements MMU {

    private int[] memory;
    private Clock clock;

    public MockMMU(Clock clock) {
        memory = new int[0xFFFF];
        this.clock = clock;
    }

    @Override
    public int read(int address) {
        clock.addCycles(4);
        return memory[address];
    }

    @Override
    public void write(int address, int data) {
        memory[address] = data;
        clock.addCycles(4);
    }

    @Override
    public void load(int[] program) {

    }

    @Override
    public void setROM(ROM rom) {

    }
}
