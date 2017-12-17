package mmu;

/**
 * Represents a memory space that is present as one continuous block of memory
 * */
public class ContinuousMemorySpace implements MemorySpace {

    private int[] memoryBlock;

    private final int START_ADDRESS;
    private final int END_ADDRESS;

    public ContinuousMemorySpace(int START_ADDRESS, int END_ADDRESS) {
        this.START_ADDRESS = START_ADDRESS;
        this.END_ADDRESS = END_ADDRESS;
        memoryBlock = new int[END_ADDRESS - START_ADDRESS + 1];
    }

    @Override
    public boolean accepts(int address) {
        return address >= START_ADDRESS && address <= END_ADDRESS;
    }

    @Override
    public int read(int address) {
        checkAddress(address);
        return memoryBlock[getAddressIndex(address)];
    }

    @Override
    public void write(int address, int data) {
        checkAddress(address);
        memoryBlock[getAddressIndex(address)] = data;
    }

    /**
     * Checks that address falls in this memory space
     * */
    private void checkAddress(int address) {
        if (!accepts(address))
            throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
    }

    /**
     * Translates address to an index of memoryBlock
     * */
    private int getAddressIndex(int address) {
        return address - START_ADDRESS;
    }
}
