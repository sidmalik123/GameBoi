package mmu;

public class GBMemorySpace implements MemorySpace {

    private int[] memory;

    public GBMemorySpace(int memorySize) {
        memory = new int[memorySize];
    }

    public int read(int address) {
        return memory[address];
    }

    public void write(int address, int data) {
        memory[address] = data;
    }
}
