package mmu;

import core.TestWithTestModule;
import mmu.cartridge.Cartridge;
import mmu.cartridge.CartridgeImpl;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCartridge extends TestWithTestModule {

    private static final String SAMPLE_FILE_NAME = "sample-rom-file.gb";
    private static final byte[] byteArr = {0x04, 0x05, -1, 0x11, 0x3E, 0x29, 0x59};

    private MMU mmu;

    public TestCartridge() {
        mmu = testInjector.getInstance(MMU.class);
    }

    @Test
    public void testCartridgeRead() throws IOException {
        Cartridge cartridge = new CartridgeImpl(SAMPLE_FILE_NAME);
        int[] data = cartridge.getData();
        
        assert (data.length == byteArr.length);
        compareDataWithByteArr(data);
    }

    @Test
    public void testCartidgeLoadMMU() throws IOException {
        mmu.load(new CartridgeImpl("sample-rom-file.gb"));
        int[] data = new int[byteArr.length];
        for (int i = 0; i < data.length; ++i) {
            data[i] = mmu.read(i);
        }
        compareDataWithByteArr(data);
    }

    @Test
    public void testBigFileLoad() throws IOException {
        // tests that only 0x8000 bytes are loaded into ROM
        mmu.load(new CartridgeImpl("file-bigger-than-rom.gb"));
        for (int i = 0; i < 0x8000; ++i) {
            if (i < Cartridge.BIOS.length) {
                assertEquals(mmu.read(i), Cartridge.BIOS[i]);
            } else {
                assertEquals(mmu.read(i), 0x01);
            }
        }
        for (int i = 0x8000; i < 0x9000; ++i) {
            assertEquals(mmu.read(i), 0x00);
        }

    }

    private void compareDataWithByteArr(int[] data) {
        for (int i = 0; i < byteArr.length; ++i) {
            if (i < Cartridge.BIOS.length) {
                assertEquals(data[i], Cartridge.BIOS[i]);
            } else if (i == 2 + Cartridge.BIOS.length) {
                assert (data[i] == 0xFF);
            } else {
                assert (data[i] == byteArr[i]);
            }
        }
    }
}
