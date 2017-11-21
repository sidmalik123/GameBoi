package gpu;


import cpu.interrupts.GBInterruptManager;
import cpu.interrupts.InterruptType;

public class GBGPUImpl implements GPU {

    private int modeCycleCount; // cycles spent in a mode

    private int currLineNum;

    private int numLinesInVblank;

    private static final int NUM_LINES_IN_VBLANK = 10;

    private GBInterruptManager interruptManager;

    private GPUMode currMode;

    public GBGPUImpl() {
        modeCycleCount = 0;
        numLinesInVblank = 0;
        currMode = GPUMode.ACCESSING_OAM; // start mode
    }

    /**
    * Change mode based on numCyclesElapsed
    * set count back to 0, after exiting a mode
    * */
    public void notifyNumCycles(int numCycles) {

        if (!isLCDEnabled()) return;

        modeCycleCount += numCycles;

        switch (currMode) {
            case ACCESSING_OAM:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    currMode = GPUMode.ACCESSING_VRAM;
                    modeCycleCount = 0;
                }
                break;
            case ACCESSING_VRAM:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    currMode = GPUMode.HBLANK;
                    modeCycleCount = 0;

                    // @Todo - write a line to screen
                }
                break;
            case HBLANK:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    modeCycleCount = 0;
                    ++currLineNum; // move to the next line after hblank

                    if (currLineNum == 143) { // @Todo make 143 a field
                        currMode = GPUMode.VBLANK;

                        interruptManager.requestInterrupt(InterruptType.VBLANK);
                    } else { // back to OAM for the next line
                        currMode = GPUMode.ACCESSING_OAM;
                    }
                }
                break;
            case VBLANK:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    modeCycleCount = 0;
                    ++numLinesInVblank;

                    if (numLinesInVblank == NUM_LINES_IN_VBLANK) { // end of vblank
                        currMode = GPUMode.ACCESSING_OAM;
                        numLinesInVblank = currLineNum = 0;
                    }
                }

        }
    }

    // Todo
    private boolean isLCDEnabled() {
        return true;
    }
}
