package mmu;

import core.Word;

public class GBMMUImpl implements GBMMU {

    private GBMemorySpaceManager memorySpaceManager;

    public Byte readData(Word address) {
        GBMemorySpace memorySpace = memorySpaceManager.getMemorySpace(address);
        return memorySpace.read(address);
    }

    public void writeData(Word address, Byte data) {
        GBMemorySpace memorySpace = memorySpaceManager.getMemorySpace(address);
        memorySpace.write(address, data);
    }
}
