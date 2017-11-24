package mmu;

public interface MemorySpace {

    int read(int address);

    void write(int address, int data);

    int getStartAddress();

    int getEndAddress();

    boolean isReadOnly();

    int getMemorySize();
}
