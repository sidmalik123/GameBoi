package mmu;

public class GBMemorySpace implements MemorySpace {

    private int[] memory;

    private final int START_ADDRESS;

    private final int END_ADDRESS;

    public GBMemorySpace(int startAddress, int endAddress) {
        this.START_ADDRESS = startAddress;
        this.END_ADDRESS = endAddress;
        memory = new int[endAddress - startAddress + 1];
    }

    public int read(int address) {
        return memory[address];
    }

    public void write(int address, int data) {
        memory[address] = data;
    }

    public int getStartAddress() {
        return this.START_ADDRESS;
    }

    public int getEndAddress() {
        return this.END_ADDRESS;
    }
}
