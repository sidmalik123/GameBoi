package mmu;

public class GBMMU implements MMU {

    private GBMemorySpaceManager memorySpaceManager;

    public int readData(int address) {
        MemorySpace memorySpace = memorySpaceManager.getMemorySpace(address);
        return memorySpace.read(address);
    }

    public void writeData(int address, int data) {
        MemorySpace memorySpace = memorySpaceManager.getMemorySpace(address);
        memorySpace.write(address, data);
    }

    public int loadProgram() {
        return 0;
    }
}
