package mmu;

public class GBMemorySpace implements MemorySpace {

    private int[] memory;

    MemoryType memoryType;

    public GBMemorySpace(MemoryType memoryType) {
        memory = new int[memoryType.getEndAddress() - memoryType.getStartAddress() + 1];
    }

    public int read(int address) {
        return memory[address];
    }

    public void write(int address, int data) {
        memory[address] = data;
    }

    public int getStartAddress() {
        return memoryType.getStartAddress();
    }

    public int getEndAddress() {
        return memoryType.getEndAddress();
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }
}
