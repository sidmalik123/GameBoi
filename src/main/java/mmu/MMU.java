package mmu;

public interface MMU {

    int readData(int address);

    void writeData(int address, int data);

    // Loads the program in memory and return the start address
    int loadProgram();

}
