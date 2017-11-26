package mmu;

import core.AbstractTimingSubject;
import core.BitUtils;
import cpu.interrupts.GBInterruptManager;
import cpu.interrupts.InterruptType;
import gpu.GBGPU;
import gpu.GPUModeType;
import gpu.palette.GBPalette;
import mmu.tiles.GBTile;
import mmu.tiles.GBTileImpl;
import mmu.tiles.GBTileLine;
import mmu.tiles.GBTileLineImpl;

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

    private static final int BACKGROUND_PALETTE_ADDRESS = 0xFF47;

    private static final int GPU_CURR_LINE_NUM_ADDRESS = 0xFF44;

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

        if (address == GPU_CURR_LINE_NUM_ADDRESS)
            return gpu.getCurrLineNum();

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
            case BACKGROUND_PALETTE_ADDRESS:
                setBackgroundPalette();
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

    /**
     * Returns array of 1024(32x32) tileId nums for the background
     * */
    private int[] getBackgroundTileIdentificationData() {
        return getDataInArray(getBackgroundTileIdentificationStartAddress(), 1024);
    }

    /**
     * Returns array of 1024(32x32) tileId nums for the window
     * */
    private int[] getWindowTileIdentificationData() {
        return getDataInArray(getWindowTileIdentificationStartAddress(), 1024);
    }

    /**
     * Returns the background tile map
     * */
    public Map<Integer, GBTile> getBackgroundTileMap() {
        return getTileDataMap(getBackgroundTileIdentificationData());
    }

    /**
     * Returns a map of length tileIdNums.length,
     * an entry is a mapping from the tileNum to the Tile it points to
     * */
    private Map<Integer, GBTile> getTileDataMap(int[] tileIdNums) {
        Map<Integer, GBTile> result = new HashMap<Integer, GBTile>();
        List<GBTile> tiles = getTiles();
        for (int i = 1; i <= tileIdNums.length; ++i) {
            int tileNum;
            if (isTileNumbersSigned()) {
                byte signedTileNum = (byte) tileIdNums[i-1];
                tileNum = signedTileNum + 128;
            } else {
                tileNum = tileIdNums[i-1];
            }
            result.put(tileNum, tiles.get(tileNum));
        }
        return result;
    }

    /**
     * Returns the window tile map
     * */
    public Map<Integer, GBTile> getWindowTileMap() {
        return getTileDataMap(getWindowTileIdentificationData());
    }

    /**
     * Returns 256 initialized tiles
     * */
    private List<GBTile> getTiles() {
        int startAddress = getTileDataStartAddress();
        List<GBTile> results = new ArrayList<GBTile>(256);
        for (int i = 0; i < results.size(); ++i) {
            results.add(createTile(getDataInArray(startAddress, 16)));
            startAddress += 16;
        }
        return results;
    }

    /**
     * Creates a tile initializing all its lines
     * */
    private GBTile createTile(int[] tileData) {
        GBTile tile = new GBTileImpl();
        int j = 0;
        for (int i = 0; i < 8; ++i) {
            tile.setLine(i, createLine(tileData[j], tileData[j+1]));
            j += 2;
        }
        return tile;
    }

    /**
     * Creates a tile line using the an upper and a lower byte
     * corresponding bits of each tell us the color of a pixel
     * */
    private GBTileLine createLine(int upperByte, int lowerByte) {
        GBTileLine line = new GBTileLineImpl();
        int numPixelsInALine = 8;
        for (int i = 0; i < numPixelsInALine; ++i) {
            line.setPixelColorNum(i,
                    getColorNum(BitUtils.isBitSet(upperByte, 7-i), BitUtils.isBitSet(lowerByte, 7-i)));
        }
        return line;
    }

    /**
     * if Tile data starts at 0x8800,
     * then we must treat till ids as signed values ranging from -128 to 127
     * */
    private boolean isTileNumbersSigned() {
        return getTileDataStartAddress() == 0x8800;
    }

    /**
     * Copies arrSize bytes of data from startAddress
     * and returns it in an array
     * */
    private int[] getDataInArray(int startAddress, int arrSize) {
        int[] titleIdentificationData = new int[arrSize];
        for (int i = 0; i < arrSize; ++i) {
            titleIdentificationData[i] = readData(startAddress + i);
        }
        return titleIdentificationData;
    }

    /**
     * Checks bit 3 LCD_CONTROL_REGISTER
     * if set returns 0x9C00, else 0x9800
     * */
    private int getBackgroundTileIdentificationStartAddress() {
        boolean isBit3Set = BitUtils.isBitSet(readData(LCD_CONTROL_REGISTER_ADDRESS), 3);
        return isBit3Set ? 0x9C00 : 0x9800;
    }

    /**
     * Checks bit 6 LCD_CONTROL_REGISTER
     * if set returns 0x9C00, else 0x9800
     * */
    private int getWindowTileIdentificationStartAddress() {
        boolean isBit6Set = BitUtils.isBitSet(readData(LCD_CONTROL_REGISTER_ADDRESS), 6);
        return isBit6Set ? 0x9C00 : 0x9800;
    }

    /**
     * Checks bit 4 LCD_CONTROL_REGISTER
     * if set returns 0x8000, else 0x8800
     * */
    private int getTileDataStartAddress() {
        boolean isBit4Set = BitUtils.isBitSet(readData(LCD_CONTROL_REGISTER_ADDRESS), 4);
        return isBit4Set ? 0x8000 : 0x8800;
    }

    /**
     * Sets the background palette for the GPU,
     * bit 1,0 -> color 1
     * bit 3,2 -> color 2
     * bit 5,4 -> color 3
     * bit 7,6 -> color 4
     * */
    private void setBackgroundPalette() {
        int paletteData = readData(BACKGROUND_PALETTE_ADDRESS);
        gpu.setBackgroundPaletteColor(1,
                getColor(BitUtils.isBitSet(paletteData, 1), BitUtils.isBitSet(paletteData, 0)));
        gpu.setBackgroundPaletteColor(2,
                getColor(BitUtils.isBitSet(paletteData, 3), BitUtils.isBitSet(paletteData, 2)));
        gpu.setBackgroundPaletteColor(3,
                getColor(BitUtils.isBitSet(paletteData, 5), BitUtils.isBitSet(paletteData, 4)));
        gpu.setBackgroundPaletteColor(4,
                getColor(BitUtils.isBitSet(paletteData, 7), BitUtils.isBitSet(paletteData, 6)));
    }

    /**
     * Returns the color the 2 bits passed in map to
     * 1 -> white
     * 2 -> light grey
     * 3 -> dark grey
     * 4 -> black
     * */
    private GBPalette.Color getColor(boolean isBit2Set, boolean isBit1Set) {
        int colorNum = getColorNum(isBit2Set, isBit1Set);
        if (colorNum == 4) return GBPalette.Color.BLACK;

        if (colorNum == 3) return GBPalette.Color.DARK_GREY;

        if (colorNum == 2) return GBPalette.Color.LIGHT_GREY;

        return GBPalette.Color.WHITE;
    }

    /**
     * Returns the colorNum for the 2 bits passed in
     * 00 -> 0
     * 01 -> 1
     * 10 -> 3
     * 11 -> 4
     * */
    private int getColorNum(boolean isBit2Set, boolean isBit1Set) {
        if (isBit2Set && isBit1Set) return 4;

        if (isBit2Set && !isBit1Set) return 3;

        if (!isBit2Set && isBit1Set) return 2;

        return 1;
    }
}
