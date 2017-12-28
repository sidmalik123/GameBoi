package mmu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cpu.clock.Clock;
import cpu.clock.ClockObserver;
import mmu.memoryspaces.MemorySpace;
import mmu.memoryspaces.ROM;

import java.util.List;

/**
 * Concrete implementation of a GameBoy MMU
 * */
@Singleton
public class MMUImpl implements MMU {

    private List<MemorySpace> memorySpaces;
    private ROM rom;
    private Clock clock;

    private static final int NUM_CYCLES_TO_READ_BYTE = 4;
    private static final int NUM_CYCLES_TO_WRITE_BYTE = 4;

    @Inject
    public MMUImpl(List<MemorySpace> memorySpaces, Clock clock) {
        this.memorySpaces = memorySpaces;
        this.clock = clock;
        // init memory values
        write(0xFF10, 0x80);
        write(0xFF11, 0xBF);
        write(0xFF12, 0xF3);
        write(0xFF14, 0xBF);
        write(0xFF16, 0x3F);
        write(0xFF19, 0xBF);
        write(0xFF1A, 0x7F);
        write(0xFF1B, 0xFF);
        write(0xFF1C, 0x9F);
        write(0xFF1E, 0xBF);
        write(0xFF20, 0xFF);
        write(0xFF23, 0xBF);
        write(0xFF24, 0x77);
        write(0xFF25, 0xF3);
        write(0xFF26, 0xF1);
        write(0xFF40, 0x91);
        write(0xFF47, 0xFC);
        write(0xFF48, 0xFF);
        write(0xFF49, 0xFF);
    }

    @Override
    public int read(int address) {
        MemorySpace memorySpace = getMemorySpace(address);
        clock.addCycles(NUM_CYCLES_TO_READ_BYTE);
        return memorySpace.read(address);
    }

    @Override
    public void write(int address, int data) {
        MemorySpace memorySpace = getMemorySpace(address);
        memorySpace.write(address, data & 0xFF);
        clock.addCycles(NUM_CYCLES_TO_WRITE_BYTE);
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
}
