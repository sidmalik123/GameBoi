package mmu;

import mmu.memoryspaces.ROM;

/**
 * Mock class all memory is one big array of 0xFFFF bytes
 * */
public class MockMMU implements MMU {

    private int[] memory;

    public MockMMU() {
        memory = new int[0xFFFF];
    }

    @Override
    public int read(int address) {
        return memory[address];
    }

    @Override
    public void write(int address, int data) {
        memory[address] = data;
    }

    @Override
    public void load(int[] program) {

    }

    @Override
    public void setROM(ROM rom) {

    }
}
