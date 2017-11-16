package mmu;

public class GBMMU implements MMU {

    private GBMemorySpaceManager memorySpaceManager;

    public int readData(int address) {
        return memorySpaceManager.read(address);
    }

    public void writeData(int address, int data) {
        memorySpaceManager.write(address, data);
    }

    public int loadProgram() {
        return 0;
    }
}
