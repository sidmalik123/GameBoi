package mmu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import mmu.cartridge.Cartridge;

/**
 * Concrete implementation of a GameBoy MMU
 * */
@Singleton
public class MMUImpl implements MMU {

    private int[] memory;

    @Inject
    public MMUImpl() {
        this.memory = new int[FINAL_MEMORY_ADDRESS + 1];
        // init memory values
    }

    @Override
    public int read(int address) {
        return memory[address];
    }

    @Override
    public void write(int address, int data) {
        if (isIn(RESTRICTED_MEMORY_START_ADDRESS, RESTRICTED_MEMORY_END_ADDRESS, address)) return;

        if (isReadOnly(address)) return;

        if (isIn(WORKING_RAM_SHADOW_START_ADDRESS, WORKING_RAM_SHADOW_END_ADDRESS, address)) { // write to main ram too
            memory[address - WORKING_RAM_SHADOW_START_ADDRESS + WORKING_RAM_START_ADDRESS] = data;
        }

        if (address == CURR_LINE_NUM_ADDRESS) data = 0;

        if (address == DMA_ADDRESS) {
            copyMemory(data * 0x100, SPRITE_START_ADDRESS,
                    SPRITE_END_ADDRESS - SPRITE_START_ADDRESS + 1);
            return;
        }

        memory[address] = data & 0xFF;
    }

    @Override
    public void load(Cartridge cartridge) {
        int[] data = cartridge.getData();
        for (int i = 0; i <= ROM1_END_ADDRESS && i < data.length; ++i) {
            memory[i] = data[i];
        }
    }

    @Override
    public void setCurrLineNum(int lineNum) {
        memory[CURR_LINE_NUM_ADDRESS] = lineNum;
    }

    private void copyMemory(int sourceAddress, int destinationAddress, int numBytes) {
        for (int i = 0; i < numBytes; ++i) {
            memory[destinationAddress + i] = memory[sourceAddress + i];
        }
    }

    private boolean isIn(int start, int end, int val) {
        return val >= start && val <= end;
    }

    private boolean isReadOnly(int address) {
        return address >= ROM0_START_ADDRESS && address <= ROM1_END_ADDRESS;
    }
}
