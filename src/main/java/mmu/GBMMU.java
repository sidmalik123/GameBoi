package mmu;

import java.util.HashMap;
import java.util.Map;

public class GBMMU implements MMU {

    private Map<MemoryType, MemorySpace> memorySpaceMap;

    private static final int RESTRCITED_AREA_START_ADDRESS = 0xFEA0;
    private static final int RESTRICTED_AREA_END_ADDRESS = 0xFEFF;

    private ROMBankMode romBankMode; // set on game load -> read from 0x147


    public GBMMU() {
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

    public int readData(int address) {
        MemorySpace memorySpace = getMemorySpace(address);
        return memorySpace.read(address - memorySpace.getStartAddress());
    }

    public void writeData(int address, int data) {
        MemorySpace memorySpace = getMemorySpace(address);
        if (memorySpace.isReadOnly())
            throw new IllegalArgumentException("Cannot write to read only memory address " + address);

        if (isBetween(address, RESTRCITED_AREA_START_ADDRESS, RESTRICTED_AREA_END_ADDRESS))
            throw new IllegalArgumentException("Cannot write to restricted area [" +
                    RESTRCITED_AREA_START_ADDRESS + ", " + RESTRICTED_AREA_END_ADDRESS + "]");


        // @Todo write to shadow ram here if working ram is being written to

        memorySpace.write(address - memorySpace.getStartAddress(), data);
    }

    public int loadProgram() {
        return 0;
    }

    private boolean isBetween(int x, int start, int end) { // [start, end]
        return start <= x && x <= end;
    }
}
