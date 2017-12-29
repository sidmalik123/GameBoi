package mmu;

import core.TestWithTestModule;
import mmu.memoryspaces.ContinuousMemorySpace;
import mmu.memoryspaces.RestrictedMemory;
import org.junit.Test;


public class TestMMU extends TestWithTestModule {

    private MMU mmu;

    public TestMMU() {
        mmu = testInjector.getInstance(MMU.class);
    }

    @Test
    public void testInitialMemoryValues() {
        assert (mmu.read(0xFF05) == 0x00);
        assert (mmu.read(0xFF06) == 0x00);
        assert (mmu.read(0xFF07) == 0x00);
        assert (mmu.read(0xFF10) == 0x80);
        assert (mmu.read(0xFF11) == 0xBF);
        assert (mmu.read(0xFF12) == 0xF3);
        assert (mmu.read(0xFF14) == 0xBF);
        assert (mmu.read(0xFF16) == 0x3F);
        assert (mmu.read(0xFF17) == 0x00);
        assert (mmu.read(0xFF19) == 0xBF);
        assert (mmu.read(0xFF1A) == 0x7F);
        assert (mmu.read(0xFF1B) == 0xFF);
        assert (mmu.read(0xFF1C) == 0x9F);
        assert (mmu.read(0xFF1E) == 0xBF);
        assert (mmu.read(0xFF20) == 0xFF);
        assert (mmu.read(0xFF21) == 0x00);
        assert (mmu.read(0xFF22) == 0x00);
        assert (mmu.read(0xFF23) == 0xBF);
        assert (mmu.read(0xFF24) == 0x77);
        assert (mmu.read(0xFF25) == 0xF3);
        assert (mmu.read(0xFF26) == 0xF1);
        assert (mmu.read(0xFF40) == 0x91);
        assert (mmu.read(0xFF42) == 0x00);
        assert (mmu.read(0xFF43) == 0x00);
        assert (mmu.read(0xFF45) == 0x00);
        assert (mmu.read(0xFF47) == 0xFC);
        assert (mmu.read(0xFF48) == 0xFF);
        assert (mmu.read(0xFF49) == 0xFF);
        assert (mmu.read(0xFF4A) == 0x00);
        assert (mmu.read(0xFF4B) == 0x00);
        assert (mmu.read(0xFFFF) == 0x00);
    }

    @Test
    public void testRestrictedMemory() {
        for (int i = RestrictedMemory.START_ADDRESS; i <= RestrictedMemory.END_ADDRESS; ++i) {
            mmu.write(i, 1);
            assert (mmu.read(i) == 0x00); // all writes in this are must default to 0
        }
    }

    @Test
    public void testAllMemoryLocationsHaveAMemorySpace() {
        final int startAddress = 0x8000; // address after ROM
        final int endAddress = 0xFFFF;
        for (int i = startAddress; i <= endAddress; ++i) {
            mmu.read(i);
            mmu.write(i, 1);
        }
    }

    @Test
    public void testContinousMemorySpace() {
        int startAddress = 0x2000;
        int endAddress = 0x3FF0;
        ContinuousMemorySpace continuousMemorySpace = new ContinuousMemorySpace(startAddress, endAddress);

        for (int i = startAddress; i <= endAddress; ++i) {
            continuousMemorySpace.write(i, 1);
            assert (continuousMemorySpace.read(i) == 1);
        }

        try {
            continuousMemorySpace.read(startAddress - 1);
            assert (false);
        } catch (IllegalArgumentException ignore) {}

        try {
            continuousMemorySpace.read(endAddress + 1);
            assert (false);
        } catch (IllegalArgumentException ignore) {}

        try {
            continuousMemorySpace.write(startAddress - 1, 0);
            assert (false);
        } catch (IllegalArgumentException ignore) {}

        try {
            continuousMemorySpace.write(endAddress + 1, 0);
            assert (false);
        } catch (IllegalArgumentException ignore) {}
    }
}
