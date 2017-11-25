package mmu;

import core.AbstractTimingSubject;
import core.BitUtils;
import cpu.interrupts.GBInterruptManager;
import cpu.interrupts.InterruptType;
import gpu.GBGPU;
import gpu.GPUModeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GBMMUImpl extends AbstractTimingSubject implements GBMMU {

    private Map<MemoryType, GBMemorySpace> memorySpaceMap;

    private GBTimer timer;

    private GBGPU gpu;

    private GBTimer dividerRegister;

    private final int CPU_FREQUENCY;

    private static final int RESTRCITED_AREA_START_ADDRESS = 0xFEA0;
    private static final int RESTRICTED_AREA_END_ADDRESS = 0xFEFF;

    private static final int DIVIDER_REGISTER_ADDRESS = 0xFF04;
    private static final int TIMER_VALUE_ADDRESS = 0xFF06;
    private static final int TIMER_RESET_VALUE_ADDRESS = 0xFF05;
    private static final int TIMER_FREQUENCY_ADDRESS = 0xFF07;

    private static final int INTERRUPT_ENABLE_ADDRESS = 0xFFFF;

    private static final int LCD_STATUS_REGISTER_ADDRESS = 0xFF41;
    private static final int COINCIDENCE_LINE_ADDRESS = 0xFF45;
    private static final int LCD_CONTROL_REGISTER_ADDRESS = 0XFF40;
    private static final int DMA_ADDRESS = 0xFF46;

    private static final int BACKGROUND_SCROLL_X_ADDRESS = 0xFF42;
    private static final int BACKGROUND_SCROLL_Y_ADDRESS = 0xFF43;
    private static final int WINDOW_SCROLL_X_ADDRESS = 0xFF4A;
    private static final int WINDOW_SCROLL_Y_ADDRESS = 0xFF4B;

    private ROMBankMode romBankMode; // set on game load -> read from 0x147

    private GBInterruptManager interruptManager;


    public GBMMUImpl(int CPU_FREQUENCY, GBTimer timer, GBTimer dividerRegister) {
        this.CPU_FREQUENCY = CPU_FREQUENCY;

        memorySpaceMap = new HashMap<MemoryType, GBMemorySpace>();
        memorySpaceMap.put(MemoryType.ROM0, new GBMemorySpaceImpl(MemoryType.ROM0));
        memorySpaceMap.put(MemoryType.ROM1, new GBMemorySpaceImpl(MemoryType.ROM1));
        memorySpaceMap.put(MemoryType.VRAM, new GBMemorySpaceImpl(MemoryType.VRAM));
        memorySpaceMap.put(MemoryType.EXTERNAL_RAM, new GBMemorySpaceImpl(MemoryType.EXTERNAL_RAM));
        memorySpaceMap.put(MemoryType.WORKING_RAM, new GBMemorySpaceImpl(MemoryType.WORKING_RAM));
        memorySpaceMap.put(MemoryType.WORKING_RAM_SHADOW, new GBMemorySpaceImpl(MemoryType.WORKING_RAM_SHADOW));
        memorySpaceMap.put(MemoryType.SPRITE_MEMORY, new GBMemorySpaceImpl(MemoryType.SPRITE_MEMORY));
        memorySpaceMap.put(MemoryType.IO_MEMORY, new GBMemorySpaceImpl(MemoryType.IO_MEMORY));
        memorySpaceMap.put(MemoryType.ZERO_PAGE_RAM, new GBMemorySpaceImpl(MemoryType.ZERO_PAGE_RAM));

        this.timer = timer; // Todo - set timerspeed on game load
        this.dividerRegister = dividerRegister;
        attachTimingObserver(timer);
        attachTimingObserver(dividerRegister);
    }

    /**
     * Returns a MemorySpace that manages address
     * */
    private GBMemorySpace getMemorySpace(int address) {
        for (GBMemorySpace memorySpace : memorySpaceMap.values()) {
            if (isBetween(address, memorySpace.getStartAddress(), memorySpace.getEndAddress()))
                return memorySpace;
        }
        throw new IllegalArgumentException("Illegal Address " + address + " Supplied");
    }

    public int readData(int address) {
        if (address == TIMER_VALUE_ADDRESS) {
            return timer.getTimerValue();
        }

        if (address == DIVIDER_REGISTER_ADDRESS) {
            return dividerRegister.getTimerValue();
        }

        GBMemorySpace memorySpace = getMemorySpace(address);
        return memorySpace.read(address - memorySpace.getStartAddress());
    }


    /**
     * Writes the data passed in to the address passed in
     * Note: This method is split into 3 parts:
     *  1. Pre-write: Handle special cases before write
     *  2. Write: writes data to the address
     *  3. Post-write: allow write to happen, but do extra processing for some address
     * */
    public void writeData(int address, int data) { // Todo - handle ROM and RAM banking
        /*
        * PRE WRITE processing
        * */
        GBMemorySpace memorySpace = getMemorySpace(address);
        if (memorySpace.isReadOnly())
            throw new IllegalArgumentException("Cannot write to read only memory address " + address);

        if (isBetween(address, RESTRCITED_AREA_START_ADDRESS, RESTRICTED_AREA_END_ADDRESS)) {
            memorySpace.write(address, 0); // in the restricted area all writes default to 0
            return;
        }

        /*
        * WRITE - Todo write to shadow ram too on working ram write
        * */
        memorySpace.write(address - memorySpace.getStartAddress(), data);

        /*
        * POST WRITE processing
        * */
        switch (address) {
            case TIMER_FREQUENCY_ADDRESS:
                timer.setEnabled(isTimerEnabled());
                timer.setClockSpeed(getTimerSpeed());
            case TIMER_RESET_VALUE_ADDRESS:
                timer.setResetValue(data);
            case INTERRUPT_ENABLE_ADDRESS:
                interruptManager.enableInterrupts(getListOfEnabledInterrupts());
            case LCD_STATUS_REGISTER_ADDRESS:
                setGPUInterruptBits();
            case COINCIDENCE_LINE_ADDRESS:
                gpu.setCoincidenceLineNum(readData(COINCIDENCE_LINE_ADDRESS));
            case LCD_CONTROL_REGISTER_ADDRESS:
                setGPUDisplayBits();
            case DMA_ADDRESS:
                copyDataToSpriteMemory(data * 100);
            case BACKGROUND_SCROLL_X_ADDRESS:
                gpu.setBackgroundScrollX(data);
            case BACKGROUND_SCROLL_Y_ADDRESS:
                gpu.setBackgroundScrollY(data);
            case WINDOW_SCROLL_X_ADDRESS:
                gpu.setWindowScrollX(data);
            case WINDOW_SCROLL_Y_ADDRESS:
                gpu.setWindowScrollY(data);
        }
    }

    public int loadProgram(String programLocation) {
        return 0;
    }

    /**
     * checks if x is between start and end inclusive
     * */
    private boolean isBetween(int x, int start, int end) {
        return start <= x && x <= end;
    }

    /**
     * Notify numCycle update to all the observers
     * */
    public void notifyNumCycles(int numCycles) {
        notifyTimingObservers(numCycles);
    }

    /**
    * First 2 bits at TIMER_FREQUENCY_ADDRESS, specify the speed mode
    * */
    private TimerSpeed getTimerSpeed() {
        int speed =  readData(TIMER_FREQUENCY_ADDRESS) & 3;
        switch (speed) {
            case 0:
                return TimerSpeed.SPEED1;
            case 1:
                return TimerSpeed.SPEED2;
            case 2:
                return TimerSpeed.SPEED3;
            case 3:
                return TimerSpeed.SPEED4;
            default:
                throw new IllegalArgumentException("Incorrect timer frequency spedified at " + TIMER_FREQUENCY_ADDRESS);
        }
    }


    /**
    * If bit 2 at TIMER_FREQUENCY_ADDRESS is set then timer is enabled, else not
    * */
    private boolean isTimerEnabled() {
        int frequencyValue = readData(TIMER_FREQUENCY_ADDRESS);
        return BitUtils.isBitSet(frequencyValue, 2);
    }

    /**
     * Analyses INTERRUPT_ENABLE_ADDRESS value to check which
     * interrupts are enabled, returns a list of the enabled ones
     * */
    private List<InterruptType> getListOfEnabledInterrupts() {
        List<InterruptType> enabledInterrupts = new ArrayList<InterruptType>();

        int interruptEnableAddressVal = readData(INTERRUPT_ENABLE_ADDRESS);

        if (BitUtils.isBitSet(interruptEnableAddressVal, 0))
            enabledInterrupts.add(InterruptType.VBLANK);

        if (BitUtils.isBitSet(interruptEnableAddressVal, 1))
            enabledInterrupts.add(InterruptType.LCD);

        if (BitUtils.isBitSet(interruptEnableAddressVal, 2))
            enabledInterrupts.add(InterruptType.TIMER);

        if (BitUtils.isBitSet(interruptEnableAddressVal, 3))
            enabledInterrupts.add(InterruptType.SERIAL);

        if (BitUtils.isBitSet(interruptEnableAddressVal, 4))
            enabledInterrupts.add(InterruptType.JOYPAD);

        return enabledInterrupts;
    }

    /**
     * Copies block of data from startAddress to Sprite Memory
     * */
    private void copyDataToSpriteMemory(final int startAddress) {
        GBMemorySpace spriteMemory = memorySpaceMap.get(MemoryType.SPRITE_MEMORY);
        for (int i = 0; i < spriteMemory.getMemorySize(); ++i) {
            spriteMemory.write(spriteMemory.getStartAddress() + i, readData(startAddress + i));
        }
    }

    /**
     * Sets GPU Display bits by reading the value at LCD_CONTROL_REGISTER_ADDRESS
     * 7 -> LCD enabled/disabled
     * 0 -> Background enabled/disabled
     * 1 -> Sprites enabled/disabled
     * 5 -> Window enabled/disabled
     * */
    private void setGPUDisplayBits() {
        int lcdControlData = readData(LCD_CONTROL_REGISTER_ADDRESS);
        gpu.setLCDEnabled(BitUtils.isBitSet(lcdControlData, 7));
        gpu.setBackgroundEnabled(BitUtils.isBitSet(lcdControlData, 0));
        gpu.setSpritesEnabled(BitUtils.isBitSet(lcdControlData, 1));
        gpu.setWindowEnabled(BitUtils.isBitSet(lcdControlData, 5));
    }

    /**
     * Sets GPU Interrupt bits by reading the value at LCD_STATUS_REGISTER_ADDRESS
     * 3 -> HBLANK LCD Interrupt
     * 4 -> VBLANK LCD Interrupt
     * 5 -> ACCESSING_OAM LCD Interrupt
     * 6 -> Coincidence LCD Interrupt
     * */
    private void setGPUInterruptBits() {
        int lcdStatusData = readData(LCD_STATUS_REGISTER_ADDRESS);
        gpu.setLCDInterrupt(GPUModeType.HBLANK, BitUtils.isBitSet(lcdStatusData, 3));
        gpu.setLCDInterrupt(GPUModeType.VBLANK, BitUtils.isBitSet(lcdStatusData, 4));
        gpu.setLCDInterrupt(GPUModeType.ACCESSING_OAM, BitUtils.isBitSet(lcdStatusData, 5));
        gpu.setCoincidenceLCDInterruptEnabled(BitUtils.isBitSet(lcdStatusData, 6));
    }

    public int[] getBackgroundTiteIdentificationData() {
        return getTileIdentificationData(getBackgroundTileIdentificationStartAddress());
    }

    public int[] getWindowTilaIdentificationData() {
        return getTileIdentificationData(getWindowTileIdentificationStartAddress());
    }

    /**
     * Copies 1024 bytes of data from startAddress
     * and returns it in an array
     * */
    private int[] getTileIdentificationData(int startAddress) {
        final int ARR_SIZE = 1024;
        int[] titleIdentificationData = new int[ARR_SIZE];
        for (int i = 0; i < ARR_SIZE; ++i) {
            titleIdentificationData[i] = readData(startAddress + i);
        }
        return titleIdentificationData;
    }

    /**
     * Checks bit 3 LCD_CONTROL_REGISTER
     * if set returns 0x9800, else 0x9C00
     * */
    private int getBackgroundTileIdentificationStartAddress() {
        boolean isBit3Set = BitUtils.isBitSet(readData(LCD_CONTROL_REGISTER_ADDRESS), 3);
        return isBit3Set ? 0x9800 : 0x9C00;
    }

    /**
     * Checks bit 6 LCD_CONTROL_REGISTER
     * if set returns 0x9800, else 0x9C00
     * */
    private int getWindowTileIdentificationStartAddress() {
        boolean isBit3Set = BitUtils.isBitSet(readData(LCD_CONTROL_REGISTER_ADDRESS), 6);
        return isBit3Set ? 0x9800 : 0x9C00;
    }
}
