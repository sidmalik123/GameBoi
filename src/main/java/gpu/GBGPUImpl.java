package gpu;


import cpu.interrupts.GBInterruptManager;
import cpu.interrupts.InterruptType;
import gpu.palette.GBPalette;
import mmu.GBMMU;

import java.util.Map;

public class GBGPUImpl implements GBGPU {

    private int modeCycleCount; // cycles spent in a mode

    private int currLineNum;

    private boolean isLCDEnabled;

    private int coincidenceLineNum;

    private boolean isCoincidenceLCDInterruptEnabled;

    private int numLinesInVblank;

    private static final int NUM_LINES_IN_VBLANK = 9;

    private GBInterruptManager interruptManager;

    private GPUModeType currMode;

    private Map<GPUModeType, GBGPUMode> modesMap;

    private boolean isBackgroundEnabled;

    private boolean isSpritesEnabled;

    private boolean isWindowEnabled;

    private int backgroundScrollX;

    private int backgroundScrollY;

    private int windowScrollX;

    private int windowScrollY;

    private GBMMU mmu;

    private GBPalette backgroundPalette;

    public GBGPUImpl(GBInterruptManager interruptManager, GBMMU mmu) {
        currLineNum = 1;
        modeCycleCount = 0;
        numLinesInVblank = 0;
        currMode = GPUModeType.ACCESSING_OAM; // start mode
        this.interruptManager = interruptManager;
        this.mmu = mmu;
    }

    /**
    * Change mode based on numCyclesElapsed
    * set modeCycleCount back to 0, after exiting a mode
    * */
    public void notifyNumCycles(int numCycles) {

        if (!isLCDEnabled()) return;

        if (isCoincidence() && isCoincidenceLCDInterruptEnabled())
            interruptManager.requestInterrupt(InterruptType.LCD);

        modeCycleCount += numCycles;

        switch (currMode) {
            case ACCESSING_OAM:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    currMode = GPUModeType.ACCESSING_VRAM;
                    modeCycleCount = 0;
                }
                break;
            case ACCESSING_VRAM:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    currMode = GPUModeType.HBLANK;
                    modeCycleCount = 0;

                    // @Todo - write a line to screen

                    if (modesMap.get(currMode).isLCDInterruptEnabled())
                        interruptManager.requestInterrupt(InterruptType.LCD);
                }
                break;
            case HBLANK:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    modeCycleCount = 0;
                    ++currLineNum; // move to the next line after hblank

                    if (currLineNum == 144) { // @Todo - have a Screen object read this value from it, render screen
                        currMode = GPUModeType.VBLANK;
                        interruptManager.requestInterrupt(InterruptType.VBLANK);

                        if (modesMap.get(currMode).isLCDInterruptEnabled())
                            interruptManager.requestInterrupt(InterruptType.LCD);
                    } else { // back to OAM for the next line
                        currMode = GPUModeType.ACCESSING_OAM;
                    }
                }
                break;
            case VBLANK:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    modeCycleCount = 0;
                    ++numLinesInVblank;

                    if (numLinesInVblank > NUM_LINES_IN_VBLANK) { // end of vblank
                        numLinesInVblank = currLineNum = 0;

                        currMode = GPUModeType.ACCESSING_OAM;
                        if (modesMap.get(currMode).isLCDInterruptEnabled())
                            interruptManager.requestInterrupt(InterruptType.LCD);
                    }
                }
        }
    }

    // Todo
    private boolean isLCDEnabled() {
        return true;
    }

    /**
     * If value passed in is not the value set,
     * sets the currLineNum to 0
     * if we enable a disabled screen we set the mode to ACCESSING_OAM
     * if we disable an enabled screen we set the mode to VBLANK
     * */
    public void setLCDEnabled(boolean isEnabled) {
        if (this.isLCDEnabled == isEnabled) return;

        this.isLCDEnabled = isEnabled;
        currLineNum = 0;

        if (this.isLCDEnabled) {
            currMode = GPUModeType.ACCESSING_OAM;
        } else {
            currMode = GPUModeType.VBLANK;
        }
    }

    public GPUModeType getCurrentMode() {
        return currMode;
    }

    public void setLCDInterrupt(GPUModeType modeType, boolean isEnabled) {
        modesMap.get(modeType).setLCDInterruptEnabled(isEnabled);
    }

    public void setCoincidenceLineNum(int coincidenceLineNum) {
        this.coincidenceLineNum = coincidenceLineNum;
    }

    public void setCoincidenceLCDInterruptEnabled(boolean isCoincidenceLCDInterruptEnabled) {
        this.isCoincidenceLCDInterruptEnabled = isCoincidenceLCDInterruptEnabled;
    }

    private boolean isBackgroundEnabled() {
        return isBackgroundEnabled;
    }

    private boolean isSpritesEnabled() {
        return isSpritesEnabled;
    }


    private boolean isWindowEnabled() {
        return isWindowEnabled;
    }

    public void setBackgroundEnabled(boolean isEnabled) {
        isBackgroundEnabled = isEnabled;
    }

    public void setSpritesEnabled(boolean isEnabled) {
        isSpritesEnabled = isEnabled;
    }

    public void setWindowEnabled(boolean isEnabled) {
        isWindowEnabled = isEnabled;
    }

    public void setBackgroundScrollX(int scrollX) {
        this.backgroundScrollX = scrollX;
    }

    public void setBackgroundScrollY(int scrollY) {
        this.backgroundScrollY = scrollY;
    }

    public void setWindowScrollX(int scrollX) {
        this.windowScrollX = scrollX;
    }

    public void setWindowScrollY(int scrollY) {
        this.windowScrollY = scrollY;
    }

    public void setBackgroundPaletteColor(int colorNum, GBPalette.Color color) {
        backgroundPalette.setColor(colorNum, color);
    }

    public int getCurrLineNum() {
        return currLineNum;
    }

    private boolean isCoincidence(){
        return currLineNum == coincidenceLineNum;
    }

    private boolean isCoincidenceLCDInterruptEnabled() {
        return isCoincidenceLCDInterruptEnabled;
    }
}
