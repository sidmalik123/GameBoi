package mmu;

import java.util.HashMap;
import java.util.Map;

public class GBMemorySpaceManager implements MemorySpaceManager {

    private enum MemoryType {ROM0, ROM1, VRAM, EXTERNAL_RAM, WORKING_RAM, WORKING_RAM_SHADOW, SPRITE_MEMORY, IO_MEMORY, ZERO_PAGE_RAM};

    private Map<MemoryType, MemorySpace> memorySpaceMap;

    // start and end addresses for memory spaces
    private static final int ROM0_START_ADDRESS = 0x0000;
    private static final int ROM0_END_ADDRESS = 0x3FFF;

    private static final int ROM1_START_ADDRESS = 0x4000;
    private static final int ROM1_END_ADDRESS = 0x7FFF;

    private static final int VRAM_START_ADDRESS = 0x4000;
    private static final int VRAM_END_ADDRESS = 0x7FFF;

    private static final int EXTERNAL_RAM_START_ADDRESS = 0x4000;
    private static final int EXTERNAL_RAM_END_ADDRESS = 0x7FFF;

    private static final int WORKING_RAM_START_ADDRESS = 0x4000;
    private static final int WORKING_RAM_END_ADDRESS = 0x7FFF;

    private static final int WORKING_RAM_SHADOW_START_ADDRESS = 0x4000;
    private static final int WORKING_RAM_SHADOW_END_ADDRESS = 0x7FFF;

    private static final int SPRITE_START_ADDRESS = 0x4000;
    private static final int SPRITE_END_ADDRESS = 0x7FFF;

    private static final int IO_MEMORY_START_ADDRESS = 0x4000;
    private static final int IO_MEMORY_END_ADDRESS = 0x7FFF;

    private static final int ZERO_PAGE_START_ADDRESS = 0x4000;
    private static final int ZERO_PAGE_END_ADDRESS = 0x7FFF;

    public GBMemorySpaceManager() {
        memorySpaceMap = new HashMap<MemoryType, MemorySpace>();
        memorySpaceMap.put(MemoryType.ROM0, new GBMemorySpace(ROM0_START_ADDRESS, ROM0_END_ADDRESS));
        memorySpaceMap.put(MemoryType.ROM1, new GBMemorySpace(ROM1_START_ADDRESS, ROM1_END_ADDRESS));
        memorySpaceMap.put(MemoryType.VRAM, new GBMemorySpace(VRAM_START_ADDRESS, VRAM_END_ADDRESS));
        memorySpaceMap.put(MemoryType.EXTERNAL_RAM, new GBMemorySpace(EXTERNAL_RAM_START_ADDRESS, EXTERNAL_RAM_END_ADDRESS));
        memorySpaceMap.put(MemoryType.WORKING_RAM, new GBMemorySpace(WORKING_RAM_START_ADDRESS, WORKING_RAM_END_ADDRESS));
        memorySpaceMap.put(MemoryType.WORKING_RAM_SHADOW, new GBMemorySpace(WORKING_RAM_SHADOW_START_ADDRESS, WORKING_RAM_SHADOW_END_ADDRESS));
        memorySpaceMap.put(MemoryType.SPRITE_MEMORY, new GBMemorySpace(SPRITE_START_ADDRESS, SPRITE_END_ADDRESS));
        memorySpaceMap.put(MemoryType.IO_MEMORY, new GBMemorySpace(IO_MEMORY_START_ADDRESS, IO_MEMORY_END_ADDRESS));
        memorySpaceMap.put(MemoryType.ZERO_PAGE_RAM, new GBMemorySpace(ZERO_PAGE_START_ADDRESS, ZERO_PAGE_END_ADDRESS));
    }

    private MemorySpace getMemorySpace(int address) {
        for (MemorySpace memorySpace : memorySpaceMap.values()) {
            if (isBetween(address, memorySpace.getStartAddress(), memorySpace.getEndAddress()))
                return memorySpace;
        }
        throw new IllegalArgumentException("Illegal Address " + address + " Supplied");
    }

    public int read(int address) {
        MemorySpace memorySpace = getMemorySpace(address);
        return memorySpace.read(address - memorySpace.getStartAddress());
    }

    public void write(int address, int data) {
        MemorySpace memorySpace = getMemorySpace(address);
        memorySpace.write(address - memorySpace.getStartAddress(), data);
    }

    private boolean isBetween(int x, int start, int end) {
        return start <= x && x <= end;
    }
}
