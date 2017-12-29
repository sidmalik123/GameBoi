package cpu;

import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import mmu.MMU;
import mmu.MockMMU;
import org.junit.Test;

public class TestInstructionTimings {

    private MemoryAccessor memoryAccessor;
    private MMU mmu;
    private Clock clock;

    public TestInstructionTimings() {
        mmu = new MockMMU();
        clock = new ClockImpl();
        memoryAccessor = new MemoryAccessorImpl(mmu, clock);
    }

    @Test
    public void testMemoryAccessorTimings() {
        mmu.write(0x4000, 10);
        int oldClockCycles = clock.getTotalCycles();
        assert (memoryAccessor.read(0x4000) == 10);
        assert (clock.getTotalCycles() == oldClockCycles + 4);

        memoryAccessor.write(0x4000, 20);
        assert (mmu.read(0x4000) == 20);
        assert (clock.getTotalCycles() == oldClockCycles + 8);
    }
}
