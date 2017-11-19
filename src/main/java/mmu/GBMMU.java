package mmu;

import core.BitUtils;
import core.TimingObserver;

import java.util.HashMap;
import java.util.Map;

public class GBMMU implements MMU, TimingObserver {

    private Map<MemoryType, MemorySpace> memorySpaceMap;

    private Timer timer;

    private static final int RESTRCITED_AREA_START_ADDRESS = 0xFEA0;
    private static final int RESTRICTED_AREA_END_ADDRESS = 0xFEFF;

    private static final int DIVIDER_REGISTER_ADDRESS = 0xFF04;
    private static final int TIMER_VALUE_ADDRESS = 0xFF06;
    private static final int TIMER_RESET_VALUE_ADDRESS = 0xFF05;
    private static final int TIMER_FREQUENCY_ADDRESS = 0xFF07;
    private static final int TIMER_OVERFLOW_VALUE = 255;

    private boolean timerSpeedChanged;

    private ROMBankMode romBankMode; // set on game load -> read from 0x147


    public GBMMU() {
        memorySpaceMap = new HashMap<MemoryType, MemorySpace>();
        memorySpaceMap.put(MemoryType.ROM0, new GBMemorySpace(MemoryType.ROM0));
        memorySpaceMap.put(MemoryType.ROM1, new GBMemorySpace(MemoryType.ROM1));
        memorySpaceMap.put(MemoryType.VRAM, new GBMemorySpace(MemoryType.VRAM));
        memorySpaceMap.put(MemoryType.EXTERNAL_RAM, new GBMemorySpace(MemoryType.EXTERNAL_RAM));
        memorySpaceMap.put(MemoryType.WORKING_RAM, new GBMemorySpace(MemoryType.WORKING_RAM));
        memorySpaceMap.put(MemoryType.WORKING_RAM_SHADOW, new GBMemorySpace(MemoryType.WORKING_RAM_SHADOW));
        memorySpaceMap.put(MemoryType.SPRITE_MEMORY, new GBMemorySpace(MemoryType.SPRITE_MEMORY));
        memorySpaceMap.put(MemoryType.IO_MEMORY, new GBMemorySpace(MemoryType.IO_MEMORY));
        memorySpaceMap.put(MemoryType.ZERO_PAGE_RAM, new GBMemorySpace(MemoryType.ZERO_PAGE_RAM));
    }

    private MemorySpace getMemorySpace(int address) {
        for (MemorySpace memorySpace : memorySpaceMap.values()) {
            if (isBetween(address, memorySpace.getStartAddress(), memorySpace.getEndAddress()))
                return memorySpace;
        }
        throw new IllegalArgumentException("Illegal Address " + address + " Supplied");
    }

    public int readData(int address) {
        MemorySpace memorySpace = getMemorySpace(address);
        return memorySpace.read(address - memorySpace.getStartAddress());
    }

    // Todo - handle ROM and RAM banking
    public void writeData(int address, int data) {
        MemorySpace memorySpace = getMemorySpace(address);
        if (memorySpace.isReadOnly())
            throw new IllegalArgumentException("Cannot write to read only memory address " + address);

        if (isBetween(address, RESTRCITED_AREA_START_ADDRESS, RESTRICTED_AREA_END_ADDRESS))
            throw new IllegalArgumentException("Cannot write to restricted area [" +
                    RESTRCITED_AREA_START_ADDRESS + ", " + RESTRICTED_AREA_END_ADDRESS + "]");


        // @Todo write to shadow ram here if working ram is being written to

        memorySpace.write(address - memorySpace.getStartAddress(), data);
    }

    public int loadProgram() {
        return 0;
    }

    private boolean isBetween(int x, int start, int end) { // [start, end]
        return start <= x && x <= end;
    }

    public void notifyNumCycles(int numCycles) {
        // update the divider register


        // update the timer
        timer.notifyNumCycles(numCycles);
    }

    private void notifyTimerUpdate() {
        int timerValue = readData(TIMER_VALUE_ADDRESS);

        writeData(TIMER_VALUE_ADDRESS, ++timerValue);

        timer.setEnabled(isTimerEnabled());
        timer.setClockSpeed(getTimerSpeed());

        if (readData(TIMER_VALUE_ADDRESS) == TIMER_OVERFLOW_VALUE) {
            writeData(TIMER_VALUE_ADDRESS, readData(TIMER_RESET_VALUE_ADDRESS));

            // Todo - request interupt
        }
    }

    private void notifyDividerUpdate() {
        int dividerVal = readData(DIVIDER_REGISTER_ADDRESS);
        /*
        * NOTE: Divider cannot be written to with writeData as that sets divider to 0
        * on all writes to DIVIDER_REGISTER_ADDRESS
        * */
        MemorySpace dividerMemorySpace = getMemorySpace(DIVIDER_REGISTER_ADDRESS);
        if (dividerVal == 255) {
            dividerMemorySpace.write(DIVIDER_REGISTER_ADDRESS, 0); // on overflow set to 0
        } else {
            // increment
            dividerMemorySpace.write(DIVIDER_REGISTER_ADDRESS, ++dividerVal);
        }
    }


    /*
    * First 2 bits at TIMER_FREQUENCY_ADDRESS, specify the frequency
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


    /*
    * If bit 2 at TIMER_FREQUENCY_ADDRESS is set then timer is enabled, else not
    * */
    private boolean isTimerEnabled() {
        int frequencyValue = readData(TIMER_FREQUENCY_ADDRESS);
        return BitUtils.isBitSet(frequencyValue, 2);
    }


    /*
    * Private class Timer to handle timers in the memory
    * */
    private class Timer implements TimingObserver {
        private int cycleCounter;

        private TimerSpeed timerSpeed;

        private final int CPU_FREQUENCY;

        private boolean isEnabled;

        private GBMMU mmu;

        public Timer(int cycleCounter, int CPU_FREQUENCY, GBMMU mmu) {
            this.cycleCounter = cycleCounter;
            this.CPU_FREQUENCY = CPU_FREQUENCY;
            this.mmu = mmu;
        }

        public void notifyNumCycles(int numCycles) {
            if (isEnabled()) return;

            cycleCounter -= numCycles;

            if (cycleCounter <= 0)
                mmu.notifyTimerUpdate();
        }

        public void setClockSpeed(TimerSpeed timerSpeed) { /* only set if new TimerSpeed is different*/
            if (this.timerSpeed == timerSpeed) return;
            this.timerSpeed = timerSpeed;
            this.cycleCounter = CPU_FREQUENCY/timerSpeed.getFrequency();
        }

        public int getCycleCounter() {
            return cycleCounter;
        }

        public int getCPU_FREQUENCY() {
            return CPU_FREQUENCY;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }
    }

    private class DividerRegister implements TimingObserver {

        private final int CPU_FREQUENCY;

        private final int SELF_FREQUENCY = 16382;

        private final int CYCLES_TO_COUNT_TO;

        private GBMMU mmu;

        private int cycleCount;

        public DividerRegister(int CPU_FREQUENCY, GBMMU mmu) {
            this.CPU_FREQUENCY = CPU_FREQUENCY;
            CYCLES_TO_COUNT_TO = CPU_FREQUENCY/SELF_FREQUENCY;
            cycleCount = 0;
            this.mmu = mmu;
        }

        public void notifyNumCycles(int numCycles) {
            cycleCount += numCycles;
            if (cycleCount >= CYCLES_TO_COUNT_TO) {
                cycleCount = 0;
                mmu.notifyDividerUpdate();
            }
        }
    }
}
