package mmu;

import core.Word;

public abstract class GBMemorySpace implements MemorySpace<Byte, Word> {

    private Byte[] memory;

    public GBMemorySpace(int memorySize) {
        memory = new Byte[memorySize];
    }

    public Byte read(Word address) {
        return memory[address.getValue()];
    }

    public void write(Word address, Byte data) {
        memory[address.getValue()] = data;
    }
}
