package mmu;

import mmu.cartridge.Cartridge;

/**
 * Mock class all memory is one big array of 0xFFFF bytes
 * */
public class MockMMU implements MMU {

    private int[] memory;

    public MockMMU() {
        memory = new int[0xFFFF + 1];
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
    public void load(Cartridge cartridge) {

    }

    @Override
    public void setCurrLineNum(int lineNum) {

    }

    @Override
    public void setDividerRegisterValue(int val) {

    }
}
