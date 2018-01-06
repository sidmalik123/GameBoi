package mmu.cartridge;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Concrete class for Cartridge
 * */
public class CartridgeImpl implements Cartridge {

    private int[] data;

    public CartridgeImpl(String fileLocation) {
        // init data
        File file = new File(fileLocation);
        try (InputStream inputStream = FileUtils.openInputStream(file)) {
            byte[] byteArr = IOUtils.toByteArray(inputStream);
            data = new int[byteArr.length];
            for (int i = 0; i < byteArr.length; ++i) {
                data[i] = byteArr[i] & 0xFF;
            }
        } catch (IOException e) {
            throw new CartridgeReadException("Error reading cartridge from location: " + fileLocation, e);
        }
    }

    @Override
    public int[] getData() {
        return data;
    }
}
