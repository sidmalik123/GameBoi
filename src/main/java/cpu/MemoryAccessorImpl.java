package cpu;

import com.google.inject.Inject;
import cpu.clock.Clock;
import mmu.MMU;

/**
 * Concrete class for MemoryAccessor
 * */
public class MemoryAccessorImpl implements MemoryAccessor {

    private MMU mmu;
    private Clock clock;

    private static final int NUM_CYCLES_TO_READ_MEMORY = 4;
    private static final int NUM_CYCLES_TO_WRITE_TO_MEMORY = 4;

    @Inject
    public MemoryAccessorImpl(MMU mmu, Clock clock) {
        this.mmu = mmu;
        this.clock = clock;
    }

    @Override
    public int read(int address) {
        clock.addCycles(NUM_CYCLES_TO_READ_MEMORY);
        return mmu.read(address);
    }

    @Override
    public void write(int address, int data) {
        mmu.write(address, data);
        clock.addCycles(NUM_CYCLES_TO_WRITE_TO_MEMORY);
    }
}
