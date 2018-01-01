package mmu;

import mmu.cartridge.Cartridge;

/**
 * Represents GameBoy's Memory Management Unit
 * */
public interface MMU {

    int EXTERNAL_RAM_START_ADDRESS = 0xA000;
    int EXTERNAL_RAM_END_ADDRESS = 0xBFFF;

    int WORKING_RAM_START_ADDRESS = 0xC000;
    int WORKING_RAM_END_ADDRESS = 0xDFFF;

    int WORKING_RAM_SHADOW_START_ADDRESS = 0xE000;
    int WORKING_RAM_SHADOW_END_ADDRESS = 0xFDFF;

    int ROM0_START_ADDRESS = 0x0000;
    int ROM0_END_ADDRESS = 0x3FFF;

    int ROM1_START_ADDRESS = 0x4000;
    int ROM1_END_ADDRESS = 0x7FFF;

    int RESTRICTED_MEMORY_START_ADDRESS = 0xFEA0;
    int RESTRICTED_MEMORY_END_ADDRESS = 0xFEFF;

    int INTERRUPT_REQUEST_REGISTER = 0xFF0F;
    int INTERRUPT_ENABLE_REGISTER = 0xFFFF;

    int VRAM_START_ADDRESS = 0x8000;
    int VRAM_END_ADDRESS = 0x9FFF;

    int SPRITE_START_ADDRESS = 0xFE00;
    int SPRITE_END_ADDRESS = 0xFE9F;

    int LCD_CONTROL_REGISTER_ADDRESS = 0XFF40;
    int LCD_STATUS_REGISTER_ADDRESS = 0xFF41;
    int BACKGROUND_SCROLL_X_ADDRESS = 0xFF42;
    int BACKGROUND_SCROLL_Y_ADDRESS = 0xFF43;
    int CURR_LINE_NUM_ADDRESS = 0xFF44;
    int COINCIDENCE_LINE_ADDRESS = 0xFF45;
    int DMA_ADDRESS = 0xFF46;
    int BACKGROUND_PALETTE_ADDRESS = 0xFF47;
    int SPRITE_PALETTE1_ADDRESS = 0xFF48;
    int SPRITE_PALETTE2_ADDRESS = 0xFF49;
    int WINDOW_SCROLL_X_ADDRESS = 0xFF4A;
    int WINDOW_SCROLL_Y_ADDRESS = 0xFF4B;

    int FINAL_MEMORY_ADDRESS = 0xFFFF;

    /**
     * @return the value in memory at address
     **/
    int read(int address);

    /**
     * Writes data to memory at address
     * */
    void write(int address, int data);

    /**
     * loads program into memory
     * */
    void load(Cartridge cartridge);

    /**
     * Any writes to CURR_LINE_NUM_ADDRESS reset the curr line to 0,
     * this is a way for the emulator to set the curr line
     * */
    void setCurrLineNum(int lineNum);
}
