package gpu;

import mmu.memoryspaces.ContinuousMemorySpace;
import mmu.memoryspaces.MemorySpace;

/**
 * Concrete class implementing CPU
 * */
public class GPUImpl implements GPU {

    private static final int VRAM_START_ADDRESS = 0x8000;
    private static final int VRAM_END_ADDRESS = 0x9FFF;

    private static final int SPRITE_START_ADDRESS = 0xFE00;
    private static final int SPRITE_END_ADDRESS = 0xFE9F;

    private static final int LCD_CONTROL_REGISTER_ADDRESS = 0XFF40;
    private static final int LCD_STATUS_REGISTER_ADDRESS = 0xFF41;
    private static final int BACKGROUND_SCROLL_X_ADDRESS = 0xFF42;
    private static final int BACKGROUND_SCROLL_Y_ADDRESS = 0xFF43;
    private static final int CURR_LINE_NUM_ADDRESS = 0xFF44;
    private static final int COINCIDENCE_LINE_ADDRESS = 0xFF45;
    private static final int DMA_ADDRESS = 0xFF46;
    private static final int BACKGROUND_PALETTE_ADDRESS = 0xFF47;
    private static final int SPRITE_PALETTE1_ADDRESS = 0xFF48;
    private static final int SPRITE_PALETTE2_ADDRESS = 0xFF49;
    private static final int WINDOW_SCROLL_X_ADDRESS = 0xFF4A;
    private static final int WINDOW_SCROLL_Y_ADDRESS = 0xFF4B;

    private MemorySpace vram;
    private MemorySpace spriteMemory;
    private MemorySpace gpuControls;

    private GPUMode currMode;
    private int numCyclesInCurrMode;

    public GPUImpl() {
        vram = new ContinuousMemorySpace(VRAM_START_ADDRESS, VRAM_END_ADDRESS);
        spriteMemory = new ContinuousMemorySpace(SPRITE_START_ADDRESS, SPRITE_END_ADDRESS);
        gpuControls = new ContinuousMemorySpace(LCD_CONTROL_REGISTER_ADDRESS, WINDOW_SCROLL_Y_ADDRESS);

        // initial settings
        currMode = GPUMode.ACCESSING_OAM;
    }

    @Override
    public void handleClockIncrement(int increment) {
        numCyclesInCurrMode += increment;

        switch (currMode) {
            case ACCESSING_OAM:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    currMode = GPUMode.ACCESSING_VRAM;
                    numCyclesInCurrMode = 0;
                }
                break;
            case ACCESSING_VRAM:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    currMode = GPUMode.HBLANK;
                    numCyclesInCurrMode = 0;

                    renderCurrLine();
                }
                break;
            case HBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    numCyclesInCurrMode = 0;
                    gpuControls.write(CURR_LINE_NUM_ADDRESS, getCurrLineNum() + 1); // move to the next line after HBlank

                    if (getCurrLineNum() == 144) {
                        currMode = GPUMode.VBLANK;

                    } else { // back to OAM for the next line
                        currMode = GPUMode.ACCESSING_OAM;
                    }
                }
                break;
            case VBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    numCyclesInCurrMode = 0;

                    if (getCurrLineNum() == 154) { // end of vblank
                        setCurrLineNum(0);

                        currMode = GPUMode.ACCESSING_OAM;
                    }
                }
        }
    }

    private void renderCurrLine() {

    }

    @Override
    public boolean accepts(int address) {
        return vram.accepts(address) || spriteMemory.accepts(address) || gpuControls.accepts(address);
    }

    @Override
    public int read(int address) {
        if (vram.accepts(address)) return vram.read(address);
        if (spriteMemory.accepts(address)) return spriteMemory.read(address);
        if (gpuControls.accepts(address)) return gpuControls.read(address);
        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
    }

    @Override
    public void write(int address, int data) {
        if (vram.accepts(address)) {vram.write(address, data);}
        else if (spriteMemory.accepts(address)) {spriteMemory.write(address, data);}
        else if (gpuControls.accepts(address)) {gpuControls.write(address, data);}
        else {throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");}
    }

    private int getCurrLineNum() {
        return gpuControls.read(CURR_LINE_NUM_ADDRESS);
    }

    private void setCurrLineNum(int lineNum) {
        gpuControls.write(CURR_LINE_NUM_ADDRESS, lineNum);
    }
}
