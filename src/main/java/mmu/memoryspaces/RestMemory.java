package mmu.memoryspaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest of the memory
 *
 * Any memory location that does not have any specific
 * MemorySpace attached to it is present here
 * */
public class RestMemory implements MemorySpace {
    private List<MemorySpace> memorySpaces;

    public RestMemory() {
        memorySpaces = new ArrayList<>();
        memorySpaces.add(new ContinuousMemorySpace(0xFF00, 0xFF0E));
        memorySpaces.add(new ContinuousMemorySpace(0xFF10, 0xFF3F));
        memorySpaces.add(new ContinuousMemorySpace(0xFF4C, 0xFF7F));
        memorySpaces.add(new ContinuousMemorySpace(0xFF80, 0xFFFE));
    }

    @Override
    public boolean accepts(int address) {
        for (MemorySpace memorySpace : memorySpaces) {
            if (memorySpace.accepts(address)) return true;
        }
        return false;
    }

    @Override
    public int read(int address) {
        return getMemorySpace(address).read(address);
    }

    @Override
    public void write(int address, int data) {
        getMemorySpace(address).write(address, data);
    }

    private MemorySpace getMemorySpace(int address) {
        for (MemorySpace memorySpace : memorySpaces) {
            if (memorySpace.accepts(address)) return memorySpace;
        }
        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in any memory space");
    }
}
