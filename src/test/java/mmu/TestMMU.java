package mmu;

import core.TestWithTestModule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestMMU extends TestWithTestModule {

    private MMU mmu;

    public TestMMU() {
        mmu = testInjector.getInstance(MMU.class);
    }

    @Test
    public void testRestrictedMemory() {
        for (int i = MMU.RESTRICTED_MEMORY_START_ADDRESS; i <= MMU.RESTRICTED_MEMORY_END_ADDRESS; ++i) {
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
    public void testDMA() {
        for (int i = 0; i < 0xA0; ++i) {
            mmu.write(0xA000 + i, i);
        }
        mmu.write(MMU.DMA_ADDRESS, 0xA0);
        for (int i = 0; i < 0xA0; ++i) {
            assertEquals(mmu.read(MMU.SPRITE_START_ADDRESS + i), i);
        }
    }

    @Test
    public void testShadowWritesToMainRam() {
        mmu.write(0xE005, 0x57);
        assert (mmu.read(0xE005) == 0x57);
        assert (mmu.read(0xC005) == 0x57);
    }

    @Test
    public void testWriteToROM() {
        for (int i = 0; i < 0x8000; ++i) {
            mmu.write(i, 0x20);
        }
        for (int i = 0; i < 0x8000; ++i) {
            assertEquals(mmu.read(i), 0x00);
        }
    }
}
