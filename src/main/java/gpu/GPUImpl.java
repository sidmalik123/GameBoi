package gpu;

import com.google.inject.Inject;
import core.BitUtils;
import cpu.clock.AbstractClockObserver;
import cpu.clock.Clock;
import interrupts.Interrupt;
import interrupts.InterruptManager;
import mmu.memoryspaces.ContinuousMemorySpace;
import mmu.memoryspaces.MemorySpace;

/**
 * Concrete class implementing CPU
 * */
public class GPUImpl extends AbstractClockObserver implements GPU {
    private MemorySpace vram;
    private MemorySpace spriteMemory;
    private MemorySpace gpuControls;

    private int numCyclesInCurrMode;

    private Display display;
    private InterruptManager interruptManager;

    @Inject
    public GPUImpl(Display display, InterruptManager interruptManager, Clock clock) {
        super(clock);
        this.display = display;
        this.interruptManager = interruptManager;
        vram = new ContinuousMemorySpace(VRAM_START_ADDRESS, VRAM_END_ADDRESS);
        spriteMemory = new ContinuousMemorySpace(SPRITE_START_ADDRESS, SPRITE_END_ADDRESS);
        gpuControls = new ContinuousMemorySpace(LCD_CONTROL_REGISTER_ADDRESS, WINDOW_SCROLL_Y_ADDRESS);

        // initial settings
        setCurrMode(GPUMode.ACCESSING_OAM);
    }

    @Override
    public void handleClockIncrement(int increment) {
        if (!isLCDEnabled()) { // TODO - one more thing here
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
                    if (isModeInterruptEnabled(GPUMode.HBLANK)) interruptManager.requestInterrupt(Interrupt.LCD);
                }
                break;
            case HBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    setCurrLineNum(getCurrLineNum() + 1); // move to the next line after HBlank

                    if (getCurrLineNum() == 144) {
                        setCurrMode(GPUMode.VBLANK);
                        if (isModeInterruptEnabled(GPUMode.VBLANK)) interruptManager.requestInterrupt(Interrupt.LCD);
                        interruptManager.requestInterrupt(Interrupt.VBLANK);
                    } else { // back to OAM for the next line
                        setCurrMode(GPUMode.ACCESSING_OAM);
                        if (isModeInterruptEnabled(GPUMode.ACCESSING_OAM)) interruptManager.requestInterrupt(Interrupt.LCD);
                    }
                }
                break;
            case VBLANK:
                if (numCyclesInCurrMode >= currMode.getNumCyclesToSpend()) {
                    setCurrLineNum(getCurrLineNum() + 1);
                    if (getCurrLineNum() == 154) { // end of VBlank, back to the top
                        setCurrLineNum(0);
                        setCurrMode(GPUMode.ACCESSING_OAM);
                        if (isModeInterruptEnabled(GPUMode.ACCESSING_OAM)) interruptManager.requestInterrupt(Interrupt.LCD);
                    } else {
                        setCurrMode(GPUMode.VBLANK); // stay in VBLank
                        if (isModeInterruptEnabled(GPUMode.VBLANK)) interruptManager.requestInterrupt(Interrupt.LCD);
                    }
                }
        }
    }

    @Override
    public boolean accepts(int address) {
        return vram.accepts(address) || spriteMemory.accepts(address) || gpuControls.accepts(address);
    }

    @Override
    public int read(int address) {
        return getMemorySpace(address).read(address);
    }

    @Override
    public void write(int address, int data) {
        if (address == CURR_LINE_NUM_ADDRESS) {
            data = 0; // set data to 0 on any writes to currline
        }
        getMemorySpace(address).write(address, data);
    }

    private MemorySpace getMemorySpace(int address) {
        if (vram.accepts(address)) return vram;
        if (spriteMemory.accepts(address)) return spriteMemory;
        if (gpuControls.accepts(address)) return gpuControls;
        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in any memory space");
    }

    private void setCurrMode(GPUMode mode) {
        int lcdStatus = read(LCD_STATUS_REGISTER_ADDRESS);
        switch (mode.getModeNum()) {
            case 0: // 00
                lcdStatus = BitUtils.resetBit(lcdStatus, 0);
                lcdStatus = BitUtils.resetBit(lcdStatus, 1);
                break;
            case 1: // 01
                lcdStatus = BitUtils.setBit(lcdStatus, 0);
                lcdStatus = BitUtils.resetBit(lcdStatus, 1);
                break;
            case 2: // 10
                lcdStatus = BitUtils.resetBit(lcdStatus, 0);
                lcdStatus = BitUtils.setBit(lcdStatus, 1);
                break;
            case 3: // 11
                lcdStatus = BitUtils.setBit(lcdStatus, 0);
                lcdStatus = BitUtils.setBit(lcdStatus, 1);
                break;
        }
        gpuControls.write(LCD_STATUS_REGISTER_ADDRESS, lcdStatus);
        numCyclesInCurrMode = 0;
    }

    private GPUMode getCurrMode() {
        int lcdStatus = read(LCD_STATUS_REGISTER_ADDRESS);
        int modeNum = lcdStatus & 0b11;
        for (GPUMode mode : GPUMode.values()) {
            if (mode.getModeNum() == modeNum) return mode;
        }
        throw new RuntimeException("No mode with num: " + modeNum + " present");
    }

    private boolean isModeInterruptEnabled(GPUMode mode) {
        return BitUtils.isBitSet(read(LCD_STATUS_REGISTER_ADDRESS), mode.getInterruptBitNum());
    }

    private int getCurrLineNum() {
        return gpuControls.read(CURR_LINE_NUM_ADDRESS);
    }

    private void setCurrLineNum(int lineNum) {
        gpuControls.write(CURR_LINE_NUM_ADDRESS, lineNum);
    }

    private boolean isLCDEnabled() {
        return BitUtils.isBitSet(read(LCD_CONTROL_REGISTER_ADDRESS), 7);
    }
}
