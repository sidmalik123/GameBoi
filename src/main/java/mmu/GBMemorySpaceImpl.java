package mmu;

/**
 * GBMemorySpace implementation class
 * */
public class GBMemorySpaceImpl implements GBMemorySpace {

    private int[] memory;

    MemoryType memoryType;

    public GBMemorySpaceImpl(MemoryType memoryType) {
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

    public boolean isReadOnly() {
        return memoryType.isReadOnly();
    }

    public int getMemorySize() {
        return memoryType.getEndAddress() - memoryType.getStartAddress() + 1;
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }
}
