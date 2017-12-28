package gpu;

import com.google.inject.Inject;
import core.BitUtils;
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

    private Display display;

    @Inject
    public GPUImpl(Display display) {
        this.display = display;
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
                    setCurrLineNum(getCurrLineNum() + 1);
                    if (getCurrLineNum() == 154) { // end of vblank
                        setCurrLineNum(0);

                        currMode = GPUMode.ACCESSING_OAM;
                    }
                }
        }
    }

    private void renderCurrLine() {
        final int backgroundScrollY = gpuControls.read(BACKGROUND_SCROLL_Y_ADDRESS);
        final int backgroundScrollX = gpuControls.read(BACKGROUND_SCROLL_X_ADDRESS);
        final int currLineNum = getCurrLineNum();

        final int tileLine = (backgroundScrollY + currLineNum)/8; // out of the 32 tile lines which one is this
        int currTileNum = (tileLine * 32) + backgroundScrollX/8; // topLeft tile is 0, next one to the right is 1, and so on
        final int lineInTile = (backgroundScrollY + currLineNum) % 8; // the line in tiles we are rendering
        int startPixelNum = backgroundScrollX % 8;

        int[] tile = getTile(getTileId(currTileNum));
        for (int i = 0; i < 160; ++i) {
            int lineHigherByte = tile[(2 * lineInTile) + 1];
            int lineLowerByte = tile[2 * lineInTile];
            if (lineHigherByte != 0 || lineLowerByte != 0) System.out.println("non-zero tile data");
            for (int j = startPixelNum; j < 8; ++j) {
                int colorNum = getPixelColorNum(BitUtils.isBitSet(lineHigherByte, j), BitUtils.isBitSet(lineLowerByte, j));
                Color color = null;
                switch (colorNum) {
                    case 0: color = Color.WHITE; break;
                    case 1: color = Color.LIGHT_GRAY; break;
                    case 2: color = Color.DARK_GRAY; break;
                    case 3: color = Color.BLACK; break;
                }
                display.setPixel(startPixelNum + i, getCurrLineNum(), color);
            }
            startPixelNum = 0;
            tile = getTile(getTileId(++currTileNum)); // get next tile
        }

        display.refresh();
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

    private int getTileId(int tileNum) {
        int tileOffset = getTileIdOffset();
        return vram.read(tileOffset + tileNum);
    }

    private int getTileIdOffset() {
        int controls = gpuControls.read(LCD_CONTROL_REGISTER_ADDRESS);
        if (BitUtils.isBitSet(controls, 3)) return 0x9C00;
        return 0x9800;
    }

    private int[] getTile(int tileId) {
        int[] data = new int[16];
        int tileDataOffset = getTileDataOffset();
        int address;
        if (tileDataOffset == 0x8800) { // signed
            address = tileDataOffset + 128 + (byte) tileId;
        } else { // unsigned
            address = tileDataOffset + (16 * tileId);
        }
        for (int i = 0; i < 16; ++i) {
            data[i] = vram.read(address + i);
        }

        return data;
    }

    private int getTileDataOffset() {
        int controls = gpuControls.read(LCD_CONTROL_REGISTER_ADDRESS);
        if (BitUtils.isBitSet(controls, 4)) return 0x8000;
        return 0x8800;
    }

    private int getPixelColorNum(boolean higherBit, boolean lowerBit) {
        if (higherBit && lowerBit) return 3;
        if (higherBit && !lowerBit) return 2;
        if (!higherBit & lowerBit) return 1;
        return 0;
    }
}
