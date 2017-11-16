package mmu;

public class GBMemorySpace implements MemorySpace {

    private int[] memory;

    public GBMemorySpace(int startAddress, int endAddress) {
        memory = new int[endAddress - startAddress + 1];
    }

    public int read(int address) {
        return memory[address];
    }

    public void write(int address, int data) {
        memory[address] = data;
    }
}
