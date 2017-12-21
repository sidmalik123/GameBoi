package mmu;

import gpu.GPU;
import mmu.memoryspaces.ContinuousMemorySpace;
import mmu.memoryspaces.MemorySpace;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a GameBoy MMU
 * */
public class MMUImpl implements MMU {

    private List<MemorySpace> memorySpaces;

    MMUImpl(List<MemorySpace> memorySpaces) {
        this.memorySpaces = memorySpaces;
    } // package-private

    @Override
    public int read(int address) {
        MemorySpace memorySpace = getMemorySpace(address);
        return memorySpace.read(address);
    }

    @Override
    public void write(int address, int data) {
        MemorySpace memorySpace = getMemorySpace(address);
        memorySpace.write(address, data & 0xFF);
    }

    @Override
    public void load(int[] program) {
        for (int i = 0; i < program.length; ++i) {
            write(i, program[i]);
        }
    }

    /**
     * Returns the memory space that accepts address
     **/
    private MemorySpace getMemorySpace(int address) {
        for (MemorySpace memorySpace : memorySpaces) {
            if (memorySpace.accepts(address)) return memorySpace;
        }
        throw new RuntimeException("No memory space available for address " + Integer.toHexString(address));
    }
}
