package gpu;

import core.TimingObserver;

public class GBGPU implements GPU, TimingObserver {

    private int modeCycleCount; // cycles spent in a mode

    private int totalCycleCount; // total cycle count since start

    private int currLineNum;

    private int numLinesInVblank;

    private GPUMode currMode;

    public GBGPU() {
        modeCycleCount = 0;
        numLinesInVblank = 0;
        totalCycleCount = 0;
        currMode = GPUMode.ACCESSING_OAM; // start mode
    }

    /*
    * Change mode based on numCyclesElapsed
    * set count back to 0, after exiting a mode
    * */
    public void notifyNumCycles(int numCycles) {
        totalCycleCount += numCycles;
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

                    // @Todo - write a line to buffer
                }
                break;
            case HBLANK:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    modeCycleCount = 0;
                    ++currLineNum; // move to the next line after hblank

                    if (currLineNum == 143) { // @Todo make 143 a field
                        currMode = GPUMode.VBLANK;
                        // @Todo - refresh screen
                    } else { // back to OAM for the next line
                        currMode = GPUMode.ACCESSING_OAM;
                    }
                }
                break;
            case VBLANK:
                if (modeCycleCount >= currMode.getNumCyclesToSpend()) {
                    modeCycleCount = 0;
                    ++numLinesInVblank;

                    if (numLinesInVblank == 10) { // end of vblank
                        currMode = GPUMode.ACCESSING_OAM;
                        numLinesInVblank = currLineNum = 0;
                    }
                }

        }
    }
}
