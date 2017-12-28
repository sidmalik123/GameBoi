package mmu.memoryspaces;

/**
 * IO memory in GameBoy, used for sound and joypad
 * */
public class IOMemory implements MemorySpace {
    private static final int IO_MEMORY_1_START_ADDRESS = 0xFF00;
    private static final int IO_MEMORY_1_END_ADDRESS = 0xFF3F;
    private static final int IO_MEMORY_2_START_ADDRESS = 0xFF4C;
    private static final int IO_MEMORY_2_END_ADDRESS = 0xFF7F;

    private MemorySpace ioMemory1;
    private MemorySpace ioMemory2;

    public IOMemory() {
        ioMemory1 = new ContinuousMemorySpace(IO_MEMORY_1_START_ADDRESS, IO_MEMORY_1_END_ADDRESS);
        ioMemory2 = new ContinuousMemorySpace(IO_MEMORY_2_START_ADDRESS, IO_MEMORY_2_END_ADDRESS);
    }

    @Override
    public boolean accepts(int address) {
        return ioMemory1.accepts(address) || ioMemory2.accepts(address);
    }

    @Override
    public int read(int address) {
        if (ioMemory1.accepts(address)) return ioMemory1.read(address);
        if (ioMemory2.accepts(address)) return ioMemory2.read(address);
        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
    }

    @Override
    public void write(int address, int data) {
        if (ioMemory1.accepts(address)) {
            ioMemory1.write(address, data);
        } else if (ioMemory2.accepts(address)) {
            ioMemory2.write(address, data);
        } else {
            throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
        }
    }
}
