package mmu.memoryspaces;

/**
 * Gameboy RAM
 * */
public class RAM implements MemorySpace {

    private static final int EXTERNAL_RAM_START_ADDRESS = 0xA000;
    private static final int EXTERNAL_RAM_END_ADDRESS = 0xBFFF;

    private static final int WORKING_RAM_START_ADDRESS = 0xC000;
    private static final int WORKING_RAM_END_ADDRESS = 0xDFFF;

    private static final int WORKING_RAM_SHADOW_START_ADDRESS = 0xE000;
    private static final int WORKING_RAM_SHADOW_END_ADDRESS = 0xFDFF;

    private MemorySpace externalRam;
    private MemorySpace workingRam;
    private MemorySpace workingRamShadow;

    public RAM() {
        externalRam = new ContinuousMemorySpace(EXTERNAL_RAM_START_ADDRESS, EXTERNAL_RAM_END_ADDRESS);
        workingRam = new ContinuousMemorySpace(WORKING_RAM_START_ADDRESS, WORKING_RAM_END_ADDRESS);
        workingRamShadow = new ContinuousMemorySpace(WORKING_RAM_SHADOW_START_ADDRESS, WORKING_RAM_SHADOW_END_ADDRESS);
    }
    @Override
    public boolean accepts(int address) {
        return externalRam.accepts(address) || workingRam.accepts(address) || workingRamShadow.accepts(address);
    }

    @Override
    public int read(int address) {
        if (externalRam.accepts(address)) return externalRam.read(address);
        if (workingRam.accepts(address)) return workingRam.read(address);
        if (workingRamShadow.accepts(address)) return workingRamShadow.read(address);
        throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
    }

    @Override
    public void write(int address, int data) {
        if (externalRam.accepts(address)) {
            externalRam.write(address, data);
        } else if (workingRam.accepts(address)) {
            workingRam.write(address, data);
        } else if (workingRamShadow.accepts(address)) {
            workingRamShadow.write(address, data);
        } else {
            throw new IllegalArgumentException("Address " + Integer.toHexString(address) + " is not in this memory space");
        }
    }
}
