package mmu;

import java.util.HashMap;
import java.util.Map;

public class GBMemorySpaceManager implements MemorySpaceManager {

    private Map<MemoryType, MemorySpace> memorySpaceMap;


    public GBMemorySpaceManager() {
        memorySpaceMap = new HashMap<MemoryType, MemorySpace>();
        memorySpaceMap.put(MemoryType.ROM0, new GBMemorySpace(MemoryType.ROM0));
        memorySpaceMap.put(MemoryType.ROM1, new GBMemorySpace(MemoryType.ROM1));
        memorySpaceMap.put(MemoryType.VRAM, new GBMemorySpace(MemoryType.VRAM));
        memorySpaceMap.put(MemoryType.EXTERNAL_RAM, new GBMemorySpace(MemoryType.EXTERNAL_RAM));
        memorySpaceMap.put(MemoryType.WORKING_RAM, new GBMemorySpace(MemoryType.WORKING_RAM));
        memorySpaceMap.put(MemoryType.WORKING_RAM_SHADOW, new GBMemorySpace(MemoryType.WORKING_RAM_SHADOW));
        memorySpaceMap.put(MemoryType.SPRITE_MEMORY, new GBMemorySpace(MemoryType.SPRITE_MEMORY));
        memorySpaceMap.put(MemoryType.IO_MEMORY, new GBMemorySpace(MemoryType.IO_MEMORY));
        memorySpaceMap.put(MemoryType.ZERO_PAGE_RAM, new GBMemorySpace(MemoryType.ZERO_PAGE_RAM));
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

    private boolean isBetween(int x, int start, int end) { // [start, end]
        return start <= x && x <= end;
    }
}
