package mmu;

import com.google.inject.Singleton;
import cpu.clock.ClockObserver;
import cpu.clock.ClockSubject;
import gpu.GPU;
import mmu.memoryspaces.ContinuousMemorySpace;
import mmu.memoryspaces.MemorySpace;
import mmu.memoryspaces.ROM;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a GameBoy MMU
 * */
@Singleton
public class MMUImpl extends ClockSubject implements MMU {

    private List<MemorySpace> memorySpaces;
    private ROM rom;

    public MMUImpl(List<MemorySpace> memorySpaces) {
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

    @Override
    public void setROM(ROM rom) {
        this.rom = rom;
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

    @Override
    protected void notifyClockIncrement(int increment) {
        for (ClockObserver observer : this.observers) observer.handleClockIncrement(increment);
    }

    @Override
    public void handleClockIncrement(int increment) {
        notifyClockIncrement(increment);
    }
}
