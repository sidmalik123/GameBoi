package mmu;

public interface MemorySpace {

    int read(int address);

    void write(int address, int data);
}
