package mmu;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.MainModule;
import core.TestWithTestModule;
import cpu.CPU;
import gpu.GPU;
import mmu.cartridge.CartridgeImpl;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


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

    @Test
    public void testRendering() throws IOException {
        Injector mainInjector = Guice.createInjector(new MainModule());
        MMU mmu = mainInjector.getInstance(MMU.class);
        mmu.load(new CartridgeImpl("roms/cpu_instrs/individual/06-ld r,r.gb"));
        CPU cpu = mainInjector.getInstance(CPU.class);
//        GPU gpu = mainInjector.getInstance(GPU.class);
        cpu.run();
    }
}
