package mmu;

import mmu.memoryspaces.RAM;
import org.junit.Test;

public class TestRAM {

    private RAM ram;

    public TestRAM() {
        ram = new RAM();
    }

    @Test
    public void testShadowWritesToMainRam() {
        ram.write(0xE005, 0x57);
        assert (ram.read(0xE005) == 0x57);
        assert (ram.read(0xC005) == 0x57);
    }
}
