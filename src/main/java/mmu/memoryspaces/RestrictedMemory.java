package mmu.memoryspaces;

public class RestrictedMemory implements MemorySpace {

    public static final int START_ADDRESS = 0xFEA0;
    public static final int END_ADDRESS = 0xFEFF;

    @Override
    public boolean accepts(int address) {
        return address >= START_ADDRESS && address <= END_ADDRESS;
    }

    @Override
    public int read(int address) {
        return 0;
    }

    @Override
    public void write(int address, int data) {
        // all writes default to 0
    }
}
