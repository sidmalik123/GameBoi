package gpu;

import com.google.inject.Inject;
import core.BitUtils;
import interrupts.Interrupt;
import interrupts.InterruptManager;
import mmu.MMU;

import static mmu.MMU.*;

/**
 * Concrete class implementing CPU
 * */
public class GPUImpl implements GPU {

    private int numCyclesInCurrMode;

    private Display display;
    private InterruptManager interruptManager;
    private MMU mmu;

    private static final int LAST_VISIBLE_LINE = GPU.HEIGHT - 1;
    private static final int LCD_ENABLED_BIT = 7;
    private static final int BACKGROUND_ENABLED_BIT = 0;
    private static final int BACKGROUND_TILE_ID_BIT = 3;
    private static final int BACKGROUND_TILE_DATA_BIT = 4;

    @Inject
    public GPUImpl(Display display, InterruptManager interruptManager, MMU mmu) {
        this.display = display;
        this.interruptManager = interruptManager;
        this.mmu = mmu;
        // initial settings
        setCurrMode(GPUMode.ACCESSING_OAM, false);
    }

    @Override
    public void handleClockIncrement(int increment) {
        if (!isLCDEnabled()) {
            setCurrMode(GPUMode.VBLANK, false);
            setCurrLineNum(0);
            return;
        }

        GPUMode currMode = getCurrMode();
        numCyclesInCurrMode += increment;

        switch (currMode) {
            case ACCESSING_OAM:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    setCurrMode(GPUMode.ACCESSING_VRAM, false);
                }
                break;
            case ACCESSING_VRAM:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    renderCurrLine();
                    setCurrMode(GPUMode.HBLANK, true);
                }
                break;
            case HBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    setCurrLineNum(getCurrLineNum() + 1); // move to the next line after HBlank

                    if (getCurrLineNum() > LAST_VISIBLE_LINE) {
                        setCurrMode(GPUMode.VBLANK, true);
                        interruptManager.requestInterrupt(Interrupt.VBLANK);
                    } else { // back to OAM for the next line
                        setCurrMode(GPUMode.ACCESSING_OAM, true);
                    }
                }
                break;
            case VBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) { // end of vblank, go to top
                    setCurrLineNum(0);
                    setCurrMode(GPUMode.ACCESSING_OAM, true);
                }
        }
    }

    private void renderCurrLine() {
        if (true || isBackgroundEnabled()) {
            final int scrollY = getScrollY();
            final int scrollX = getScrollX();

            final int yPos = scrollY + getCurrLineNum();

            for (int pixel = 0; pixel < GPU.WIDTH; ++pixel) {
                int xPos = scrollX + pixel;
                int tileNum = ((yPos/8) * 32) + (xPos/8);
                int[] tileData = getTileData(tileNum);
                int lineInTile = yPos % 8;
                int pixelNum = xPos % 8;
                int highBit = (tileData[2*lineInTile + 1] >> (7 - pixelNum)) & 0b1;
                int lowBit = (tileData[2*lineInTile] >> (7 - pixelNum)) & 0b1;
                int colorNum = ((highBit << 1) | lowBit) & 0b11;
                display.setPixel(pixel, getCurrLineNum(), getPaletteColor(colorNum));
            }
            display.refresh();
        }
    }

    /**
     * @param checkForLCDInterrupt check if LCD interrupt is enabled for mode
     * */
    private void setCurrMode(GPUMode mode, boolean checkForLCDInterrupt) {
        int lcdStatus = mmu.read(LCD_STATUS_REGISTER_ADDRESS);
        switch (mode.getModeNum()) {
            case 0: // 00 - HBLANK
                lcdStatus = BitUtils.resetBit(lcdStatus, 0);
                lcdStatus = BitUtils.resetBit(lcdStatus, 1);
                break;
            case 1: // 01 - VBLANK
                lcdStatus = BitUtils.setBit(lcdStatus, 0);
                lcdStatus = BitUtils.resetBit(lcdStatus, 1);
                break;
            case 2: // 10 - OAM
                lcdStatus = BitUtils.resetBit(lcdStatus, 0);
                lcdStatus = BitUtils.setBit(lcdStatus, 1);
                break;
            case 3: // 11 - VRAM
                lcdStatus = BitUtils.setBit(lcdStatus, 0);
                lcdStatus = BitUtils.setBit(lcdStatus, 1);
                break;
        }
        mmu.write(LCD_STATUS_REGISTER_ADDRESS, lcdStatus);
        numCyclesInCurrMode = 0;
        if (checkForLCDInterrupt && isModeInterruptEnabled(mode)) interruptManager.requestInterrupt(Interrupt.LCD);
    }

    private GPUMode getCurrMode() {
        int lcdStatus = mmu.read(LCD_STATUS_REGISTER_ADDRESS);
        int modeNum = lcdStatus & 0b11;
        for (GPUMode mode : GPUMode.values()) {
            if (mode.getModeNum() == modeNum) return mode;
        }
        throw new RuntimeException("No mode with num: " + modeNum + " present");
    }

    private boolean isModeInterruptEnabled(GPUMode mode) {
        return BitUtils.isBitSet(mmu.read(LCD_STATUS_REGISTER_ADDRESS), mode.getInterruptBitNum());
    }

    private int getCurrLineNum() {
        return mmu.read(CURR_LINE_NUM_ADDRESS);
    }

    private void setCurrLineNum(int lineNum) {
        mmu.setCurrLineNum(lineNum);
    }

    private boolean isLCDEnabled() {
        return BitUtils.isBitSet(mmu.read(LCD_CONTROL_REGISTER_ADDRESS), LCD_ENABLED_BIT);
    }

    private boolean isBackgroundEnabled() {
        return BitUtils.isBitSet(mmu.read(LCD_CONTROL_REGISTER_ADDRESS), BACKGROUND_ENABLED_BIT);
    }

    private int getScrollX() {
        return mmu.read(BACKGROUND_SCROLL_X_ADDRESS);
    }

    private int getScrollY() {
        return mmu.read(BACKGROUND_SCROLL_Y_ADDRESS);
    }

    private int getTileDataStartAddress() {
        final int lcdControl = mmu.read(LCD_CONTROL_REGISTER_ADDRESS);
        if (BitUtils.isBitSet(lcdControl, BACKGROUND_TILE_DATA_BIT)) {
            return 0x8000;
        } else {
            return 0x8800;
        }
    }

    private int getTileId(int tileNum) {
//        if (tileNum < 0 || tileNum > 1023)
//            throw new IllegalArgumentException("Invalid Tile Num: " + tileNum);
        int tileIdStartAddress;
        final int lcdControl = mmu.read(LCD_CONTROL_REGISTER_ADDRESS);
        if (BitUtils.isBitSet(lcdControl, BACKGROUND_TILE_ID_BIT)) {
            tileIdStartAddress = 0x9C00;
        } else {
            tileIdStartAddress = 0x9800;
        }
        return mmu.read(tileIdStartAddress + tileNum);
    }

    private int[] getTileData(int tileNum) {
        int[] tileData = new int[16];
        int tileDataStartAddress = getTileDataStartAddress();
        int tileId = getTileId(tileNum);
        int tileStartAddress;
        if (tileDataStartAddress == 0x8000) { // unsigned tileid
            tileStartAddress = tileDataStartAddress + (tileId * 16);
        } else { // signed tileid
            int adjustedTileId = (byte) tileId + 128;
            tileStartAddress = tileDataStartAddress + (adjustedTileId * 16);
        }
        for (int i = 0; i < 16; ++i) {
            tileData[i] = mmu.read(tileStartAddress + i);
        }
        return tileData;
    }

    private Color getPaletteColor(int colorNum) {
        final int palette = mmu.read(BACKGROUND_PALETTE_ADDRESS);
        switch (colorNum) {
            case 0: return getColor(BitUtils.isBitSet(palette, 1), BitUtils.isBitSet(palette, 0));
            case 1: return getColor(BitUtils.isBitSet(palette, 3), BitUtils.isBitSet(palette, 2));
            case 2: return getColor(BitUtils.isBitSet(palette, 5), BitUtils.isBitSet(palette, 4));
            case 3: return getColor(BitUtils.isBitSet(palette, 7), BitUtils.isBitSet(palette, 6));
        }
        throw new IllegalArgumentException("Invalid color num: " + colorNum);
    }

    private Color getColor(boolean highByte, boolean lowByte) {
        if (highByte && lowByte) return Color.BLACK;

        if (highByte && !lowByte) return Color.DARK_GRAY;

        if (!highByte && lowByte) return Color.LIGHT_GRAY;

        return Color.WHITE;
    }
}
