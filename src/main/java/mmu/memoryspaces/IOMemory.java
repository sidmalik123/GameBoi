package mmu.memoryspaces;

/**
 * IO memory in GameBoy
 * */
public class IOMemory implements MemorySpace {
    private static final int IO_MEMORY_START_ADDRESS = 0xFF00;
    private static final int IO_MEMORY_END_ADDRESS = 0xFF7F;

    private MemorySpace ioMemory;

    public IOMemory() {
        ioMemory = new ContinuousMemorySpace(IO_MEMORY_START_ADDRESS, IO_MEMORY_END_ADDRESS);
    }

    @Override
    public boolean accepts(int address) {
        return ioMemory.accepts(address);
    }

    @Override
    public int read(int address) {
        if (ioMemory.accepts(address)) return ioMemory.read(address);
        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
    }

    @Override
    public void write(int address, int data) {
        if (ioMemory.accepts(address))
            ioMemory.write(address, data);
        else
            throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");

    }
}
