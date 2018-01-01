package mmu.cartridge;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Concrete class for Cartridge
 * */
public class CartridgeImpl implements Cartridge {

    private int[] data;

    public CartridgeImpl(String fileLocation) throws IOException {
        // init data
        File file = new File(fileLocation);
        try (InputStream inputStream = FileUtils.openInputStream(file)) {
            byte[] byteArr = IOUtils.toByteArray(inputStream);
            data = new int[byteArr.length];
            for (int i = 0; i < byteArr.length; ++i) {
                if (i < BIOS.length) {
                    data[i] = BIOS[i];
                } else {
                    data[i] = byteArr[i] & 0xFF;
                }
            }
        }
    }

    @Override
    public int[] getData() {
        return data;
    }
}
