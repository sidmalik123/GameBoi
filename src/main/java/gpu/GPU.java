package gpu;

import cpu.clock.ClockObserver;
import mmu.memoryspaces.MemorySpace;

/**
 * GameBoy's Graphic Processing Unit,
 *
 * For the purpose of making code cleaner, VRAM and OAM (Sprite memory),
 * which are separate parts of memory in reality (that GPU reads from)
 * is modelled to be contained in GPU
 * */
public interface GPU extends MemorySpace, ClockObserver {
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
}
