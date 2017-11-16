package mmu;

public interface MemorySpaceManager {

    int read(int address);

    void write(int address, int data);
}
