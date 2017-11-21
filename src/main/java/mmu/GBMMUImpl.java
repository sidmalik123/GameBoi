package mmu;

import core.AbstractTimingSubject;
import core.BitUtils;
import cpu.interrupts.GBInterruptManager;
import cpu.interrupts.InterruptType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GBMMUImpl extends AbstractTimingSubject implements GBMMU {

    private Map<MemoryType, MemorySpace> memorySpaceMap;

    private GBTimer timer;

    private GBTimer dividerRegister;

    private final int CPU_FREQUENCY;

    private static final int RESTRCITED_AREA_START_ADDRESS = 0xFEA0;
    private static final int RESTRICTED_AREA_END_ADDRESS = 0xFEFF;

    private static final int DIVIDER_REGISTER_ADDRESS = 0xFF04;
    private static final int TIMER_VALUE_ADDRESS = 0xFF06;
    private static final int TIMER_RESET_VALUE_ADDRESS = 0xFF05;
    private static final int TIMER_FREQUENCY_ADDRESS = 0xFF07;

    private static final int INTERRUPT_ENABLE_ADDRESS = 0xFFFF;

    private ROMBankMode romBankMode; // set on game load -> read from 0x147

    private GBInterruptManager interruptManager;


    public GBMMUImpl(int CPU_FREQUENCY, GBTimer timer, GBTimer dividerRegister) {
        this.CPU_FREQUENCY = CPU_FREQUENCY;

        memorySpaceMap = new HashMap<MemoryType, MemorySpace>();
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
    private MemorySpace getMemorySpace(int address) {
        for (MemorySpace memorySpace : memorySpaceMap.values()) {
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

        MemorySpace memorySpace = getMemorySpace(address);
        return memorySpace.read(address - memorySpace.getStartAddress());
    }


    // Todo - handle ROM and RAM banking
    public void writeData(int address, int data) {
        /*
        * PRE WRITE processing
        * */
        MemorySpace memorySpace = getMemorySpace(address);
        if (memorySpace.isReadOnly())
            throw new IllegalArgumentException("Cannot write to read only memory address " + address);

        if (isBetween(address, RESTRCITED_AREA_START_ADDRESS, RESTRICTED_AREA_END_ADDRESS))
            memorySpace.write(address, 0); // in the restricted area all writes default to 0

        /*
        * WRITE - Todo write to shadow ram too on working ram write
        * */
        memorySpace.write(address - memorySpace.getStartAddress(), data);

        /*
        * POST WRITE processing
        * */
        if (address == TIMER_FREQUENCY_ADDRESS) {
            timer.setEnabled(isTimerEnabled());
            timer.setClockSpeed(getTimerSpeed());
        }

        if (address == TIMER_RESET_VALUE_ADDRESS) {
            timer.setResetValue(data);
        }

        if (address == INTERRUPT_ENABLE_ADDRESS) {
            interruptManager.enableInterrupts(getListOfEnabledInterrupts());
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
     * Analyses INTERRUPT_ENABLE_ADDRESS value to checks which
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
}
