package mmu;

import gpu.GPU;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a GameBoy MMU
 * */
public class MMUImpl implements MMU {

    // start and end addresses for memory spaces
    private static final int ROM0_START_ADDRESS = 0x0000;
    private static final int ROM0_END_ADDRESS = 0x3FFF;

    private static final int ROM1_START_ADDRESS = 0x4000;
    private static final int ROM1_END_ADDRESS = 0x7FFF;

    private static final int EXTERNAL_RAM_START_ADDRESS = 0xA000;
    private static final int EXTERNAL_RAM_END_ADDRESS = 0xBFFF;

    private static final int WORKING_RAM_START_ADDRESS = 0xC000;
    private static final int WORKING_RAM_END_ADDRESS = 0xDFFF;

    private static final int WORKING_RAM_SHADOW_START_ADDRESS = 0xE000;
    private static final int WORKING_RAM_SHADOW_END_ADDRESS = 0xFDFF;

    private List<MemorySpace> memorySpaces;

    public MMUImpl(GPU gpu) {
        initMemorySpaces();
        memorySpaces.add(gpu);
    }

    @Override
    public int read(int address) {
        MemorySpace memorySpace = getMemorySpace(address);
        return memorySpace.read(address);
    }

    @Override
    public void write(int address, int data) {
        MemorySpace memorySpace = getMemorySpace(address);
        memorySpace.write(address, data & 0xFF);
    }

    /**
     * Returns the memory space that accepts address
     **/
    private MemorySpace getMemorySpace(int address) {
        for (MemorySpace memorySpace : memorySpaces) {
            if (memorySpace.accepts(address)) return memorySpace;
        }
        throw new RuntimeException("No memory space available for address " + Integer.toHexString(address));
    }

    /**
     * Initializes all Continuous blocks of memory
     * */
    private void initMemorySpaces() {
        memorySpaces = new ArrayList<>();
        memorySpaces.add(new ContinuousMemorySpace(ROM0_START_ADDRESS, ROM0_END_ADDRESS));
        memorySpaces.add(new ContinuousMemorySpace(ROM1_START_ADDRESS, ROM1_END_ADDRESS));
        memorySpaces.add(new ContinuousMemorySpace(EXTERNAL_RAM_START_ADDRESS, EXTERNAL_RAM_END_ADDRESS));
        memorySpaces.add(new ContinuousMemorySpace(WORKING_RAM_START_ADDRESS, WORKING_RAM_END_ADDRESS));
        memorySpaces.add(new ContinuousMemorySpace(WORKING_RAM_SHADOW_START_ADDRESS, WORKING_RAM_SHADOW_END_ADDRESS));
    }
}
