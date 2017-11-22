package gpu;


import cpu.interrupts.GBInterruptManager;
import cpu.interrupts.InterruptType;

import java.util.List;
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

    public GBGPUImpl(GBInterruptManager interruptManager) {
        currLineNum = 1;
        modeCycleCount = 0;
        numLinesInVblank = 0;
        currMode = GPUModeType.ACCESSING_OAM; // start mode
        this.interruptManager = interruptManager;
    }

    /**
    * Change mode based on numCyclesElapsed
    * set modeCycleCount back to 0, after exiting a mode
    * */
    public void notifyNumCycles(int numCycles) {

        if (!isLCDEnabled()) return;

        if (isCoincidence())
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

                    if (currLineNum == 144) { // @Todo - have a Screen object read this value from it
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

    public void setLCDEnabled(boolean isEnabled) {
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

    public void enableLCDInterrupts(List<GPUModeType> modeTypes) {
        for (GPUModeType modeType : modeTypes)
            modesMap.get(modeType).setLCDInterruptEnabled(true);
    }

    public void setCoincidenceLineNum(int coincidenceLineNum) {
        this.coincidenceLineNum = coincidenceLineNum;
    }

    public void setCoincidenceLCDInterruptEnabled(boolean isCoincidenceLCDInterruptEnabled) {
        this.isCoincidenceLCDInterruptEnabled = isCoincidenceLCDInterruptEnabled;
    }

    private boolean isCoincidence(){
        return currLineNum == coincidenceLineNum;
    }
}
