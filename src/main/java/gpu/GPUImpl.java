package gpu;

import mmu.ContinuousMemorySpace;
import mmu.MemorySpace;

/**
 * Concrete class implementing CPU
 * */
public class GPUImpl implements GPU {

    // memory regions in GPU
    private static final int VRAM_START_ADDRESS = 0x8000;
    private static final int VRAM_END_ADDRESS = 0x9FFF;

    private static final int OAM_START_ADDRESS = 0xFE00;
    private static final int OAM_END_ADDRESS = 0xFE9F;

    private static final int LCD_CONTROL_REGISTER_ADDRESS = 0XFF40;
    private static final int LCD_STATUS_REGISTER_ADDRESS = 0xFF41;
    private static final int BACKGROUND_SCROLL_X_ADDRESS = 0xFF42;
    private static final int BACKGROUND_SCROLL_Y_ADDRESS = 0xFF43;
    private static final int GPU_CURR_LINE_NUM_ADDRESS = 0xFF44;
    private static final int COINCIDENCE_LINE_ADDRESS = 0xFF45;
    private static final int DMA_ADDRESS = 0xFF46;
    private static final int BACKGROUND_PALETTE_ADDRESS = 0xFF47;
    private static final int SPRITE_PALETTE1_ADDRESS = 0xFF48;
    private static final int SPRITE_PALETTE2_ADDRESS = 0xFF49;
    private static final int WINDOW_SCROLL_X_ADDRESS = 0xFF4A;
    private static final int WINDOW_SCROLL_Y_ADDRESS = 0xFF4B;

    private MemorySpace vram;
    private MemorySpace oam;
    private MemorySpace gpuControls;

    public GPUImpl() {
        vram = new ContinuousMemorySpace(VRAM_START_ADDRESS, VRAM_END_ADDRESS);
        oam = new ContinuousMemorySpace(OAM_START_ADDRESS, OAM_END_ADDRESS);
        gpuControls = new ContinuousMemorySpace(WINDOW_SCROLL_Y_ADDRESS, LCD_CONTROL_REGISTER_ADDRESS);
    }

    @Override
    public void handleClockIncrement(int increment) {

    }

    @Override
    public boolean accepts(int address) {
        return vram.accepts(address) || oam.accepts(address) || gpuControls.accepts(address);
    }

    @Override
    public int read(int address) {
        return getMemorySpace(address).read(address);
    }

    @Override
    public void write(int address, int data) {
        getMemorySpace(address).write(address, data);
    }

    /**
     * Returns MemorySpace that accepts address
     * */
    private MemorySpace getMemorySpace(int address) {
        if (vram.accepts(address)) return vram;
        if (oam.accepts(address)) return oam;
        if (gpuControls.accepts(address)) return gpuControls;

        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " not accepted by any memory space");
    }
}
