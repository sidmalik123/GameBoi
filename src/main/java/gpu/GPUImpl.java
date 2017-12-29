package gpu;

import com.google.inject.Inject;
import core.BitUtils;
import cpu.clock.AbstractClockObserver;
import cpu.clock.Clock;
import interrupts.Interrupt;
import interrupts.InterruptManager;
import mmu.MMU;

import static mmu.MMU.CURR_LINE_NUM_ADDRESS;
import static mmu.MMU.LCD_CONTROL_REGISTER_ADDRESS;
import static mmu.MMU.LCD_STATUS_REGISTER_ADDRESS;

/**
 * Concrete class implementing CPU
 * */
public class GPUImpl extends AbstractClockObserver implements GPU {

    private int numCyclesInCurrMode;

    private Display display;
    private InterruptManager interruptManager;
    private MMU mmu;

    @Inject
    public GPUImpl(Display display, InterruptManager interruptManager, Clock clock, MMU mmu) {
        super(clock);
        this.display = display;
        this.interruptManager = interruptManager;
        this.mmu = mmu;
        // initial settings
        setCurrMode(GPUMode.ACCESSING_OAM);
    }

    @Override
    public void handleClockIncrement(int increment) {
        if (!isLCDEnabled()) {
            setCurrMode(GPUMode.VBLANK);
            setCurrLineNum(0);
            return;
        }

        GPUMode currMode = getCurrMode();
        numCyclesInCurrMode += increment;

        switch (currMode) {
            case ACCESSING_OAM:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    setCurrMode(GPUMode.ACCESSING_VRAM);
                }
                break;
            case ACCESSING_VRAM:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
//                    renderCurrLine();
                    setCurrMode(GPUMode.HBLANK);
                }
                break;
            case HBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    setCurrLineNum(getCurrLineNum() + 1); // move to the next line after HBlank

                    if (getCurrLineNum() == 144) {
                        setCurrMode(GPUMode.VBLANK);
                        interruptManager.requestInterrupt(Interrupt.VBLANK);
                    } else { // back to OAM for the next line
                        setCurrMode(GPUMode.ACCESSING_OAM);
                    }
                }
                break;
            case VBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    setCurrLineNum(getCurrLineNum() + 1);
                    if (getCurrLineNum() == 154) { // end of VBlank, back to the top
                        setCurrLineNum(0);
                        setCurrMode(GPUMode.ACCESSING_OAM);
                    } else {
                        setCurrMode(GPUMode.VBLANK); // stay in VBLank
                    }
                }
        }
    }

    private void setCurrMode(GPUMode mode) {
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
        if (mode == GPUMode.ACCESSING_OAM || mode == GPUMode.VBLANK || mode == GPUMode.HBLANK) { // request lcd interrupt for these
            if (isModeInterruptEnabled(mode)) interruptManager.requestInterrupt(Interrupt.LCD);
        }
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
        return BitUtils.isBitSet(mmu.read(LCD_CONTROL_REGISTER_ADDRESS), 7);
    }
}
