package mmu;

import mmu.memoryspaces.ROM;
import mmu.memoryspaces.ROMImpl;
import org.junit.Test;

public class TestROM {

    private ROM rom;

    public TestROM() {
        this.rom = new ROMImpl();
    }

//    @Test
//    public void testNoWriteAllowedToROM() {
//        try {
//            rom.write(0x100, 2);
//            assert (false); // should throw above
//        } catch (ReadOnlyMemoryException ex) {}
//    }
}
