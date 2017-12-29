package mmu.memoryspaces;

import mmu.ReadOnlyMemoryException;
import mmu.cartridge.Cartridge;

public class ROMImpl implements ROM {

    private static final int ROM0_START_ADDRESS = 0x0000;
    private static final int ROM0_END_ADDRESS = 0x3FFF;

    private static final int ROM1_START_ADDRESS = 0x4000;
    private static final int ROM1_END_ADDRESS = 0x7FFF;

    private MemorySpace rom0;
    private MemorySpace rom1;

    public ROMImpl() {
        rom0 = new ContinuousMemorySpace(ROM0_START_ADDRESS, ROM0_END_ADDRESS);
        rom1 = new ContinuousMemorySpace(ROM1_START_ADDRESS, ROM1_END_ADDRESS);
    }

    @Override
    public boolean accepts(int address) {
        return rom0.accepts(address) || rom1.accepts(address);
    }

    @Override
    public int read(int address) {
        if (rom0.accepts(address)) return rom0.read(address);
        if (rom1.accepts(address)) return rom1.read(address);
        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
    }

    @Override
    public void write(int address, int data) {
//        throw new ReadOnlyMemoryException("Cannot write to read only memory: " + Integer.toHexString(address));
    }

    @Override
    public void load(Cartridge cartridge) {
        int[] data = cartridge.getData();
        for (int i = 0; i <= ROM1_END_ADDRESS && i < data.length; ++i) {
            if (i <= ROM0_END_ADDRESS) {
                rom0.write(i, data[i]);
            } else {
                rom1.write(i, data[i]);
            }
        }
    }
}
