package mmu;

import mmu.cartridge.Cartridge;
import mmu.cartridge.CartridgeImpl;
import org.junit.Test;

import java.io.IOException;

public class TestCartridge {

    private static final String SAMPLE_FILE_NAME = "sample-rom-file.gb";
    private static final byte[] byteArr = {0x04, 0x05, -1, 0x11, 0x3E, 0x29, 0x59};

    @Test
    public void testCartridgeRead() throws IOException {
        Cartridge cartridge = new CartridgeImpl(SAMPLE_FILE_NAME);
        int[] data = cartridge.getData();
        
        assert (data.length == byteArr.length);
        for (int i = 0; i < byteArr.length; ++i) {
            if (i == 2) {
                assert (data[i] == 0xFF);
            } else {
                assert (data[i] == byteArr[i]);
            }
        }
    }
}
