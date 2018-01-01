package mmu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cpu.CPUImpl;
import cpu.instructions.InstructionExecutorImpl;
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
        memory[0xFF10] = 0x80;
        memory[0xFF11] = 0xBF;
        memory[0xFF12] = 0xF3;
        memory[0xFF14] = 0xBF;
        memory[0xFF16] = 0x3F;
        memory[0xFF19] = 0xBF;
        memory[0xFF1A] = 0x7F;
        memory[0xFF1B] = 0xFF;
        memory[0xFF1C] = 0x9F;
        memory[0xFF1E] = 0xBF;
        memory[0xFF20] = 0xFF;
        memory[0xFF23] = 0xBF;
        memory[0xFF24] = 0x77;
        memory[0xFF25] = 0xF3;
        memory[0xFF26] = 0xF1;
        memory[0xFF40] = 0x91;
        memory[0xFF47] = 0xFC;
        memory[0xFF48] = 0xFF;
        memory[0xFF49] = 0xFF;
    }

    @Override
    public int read(int address) {
        return memory[address];
    }

    @Override
    public void write(int address, int data) {
        if (address == 0xFF02 && data == 0x81) {
            char c = (char) read(0xFF01);
            System.out.print(c);
        }

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
