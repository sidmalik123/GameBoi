package mmu;

public class GBMemorySpaceManager implements MemorySpaceManager {

    // private memory space fields
    private MemorySpace rom0; // 16K
    private MemorySpace rom1; // 16K
    private MemorySpace vram; // 8K
    private MemorySpace externalRam; // 8K
    private MemorySpace workingRam; // 8K
    private MemorySpace workingRamShadow; // slightly less than 8K
    private MemorySpace spriteMemory;
    private MemorySpace IOMemory;
    private MemorySpace zeroPageRam;

    // start and end addresses for memory spaces
    private static final int ROM0_START_ADDRESS = 0x0000;
    private static final int ROM0_END_ADDRESS = 0x3FFF;

    private static final int ROM1_START_ADDRESS = 0x4000;
    private static final int ROM1_END_ADDRESS = 0x7FFF;

    private static final int VRAM_START_ADDRESS = 0x4000;
    private static final int VRAM_END_ADDRESS = 0x7FFF;

    private static final int EXTERNAL_RAM_START_ADDRESS = 0x4000;
    private static final int EXTERNAL_RAM_END_ADDRESS = 0x7FFF;

    private static final int WORKING_RAM_START_ADDRESS = 0x4000;
    private static final int WORKING_RAM_END_ADDRESS = 0x7FFF;

    private static final int WORKING_RAM_SHADOW_START_ADDRESS = 0x4000;
    private static final int WORKING_RAM_SHADOW_END_ADDRESS = 0x7FFF;

    private static final int SPRITE_START_ADDRESS = 0x4000;
    private static final int SPRITE_END_ADDRESS = 0x7FFF;

    private static final int IO_MEMORY_START_ADDRESS = 0x4000;
    private static final int IO_MEMORY_END_ADDRESS = 0x7FFF;

    private static final int ZERO_PAGE_START_ADDRESS = 0x4000;
    private static final int ZERO_PAGE_END_ADDRESS = 0x7FFF;

    public GBMemorySpaceManager() {
        rom0 = new GBMemorySpace(ROM0_START_ADDRESS, ROM0_END_ADDRESS);
        rom1 = new GBMemorySpace(ROM1_START_ADDRESS, ROM1_END_ADDRESS);
        vram = new GBMemorySpace(VRAM_START_ADDRESS, VRAM_END_ADDRESS);
        externalRam = new GBMemorySpace(EXTERNAL_RAM_START_ADDRESS, EXTERNAL_RAM_END_ADDRESS);
        workingRam = new GBMemorySpace(WORKING_RAM_START_ADDRESS, WORKING_RAM_END_ADDRESS);
        workingRamShadow = new GBMemorySpace(WORKING_RAM_SHADOW_START_ADDRESS, WORKING_RAM_SHADOW_END_ADDRESS);
        spriteMemory = new GBMemorySpace(SPRITE_START_ADDRESS, SPRITE_END_ADDRESS);
        IOMemory = new GBMemorySpace(IO_MEMORY_START_ADDRESS, IO_MEMORY_END_ADDRESS);
        zeroPageRam = new GBMemorySpace(ZERO_PAGE_START_ADDRESS, ZERO_PAGE_END_ADDRESS);
    }

    public MemorySpace getMemorySpace(int address) {
        // based on address return the right GbMemorySpace
        switch (address & 0xF000) {
            case 0x0000:
            case 0x1000:
            case 0x2000:
            case 0x3000:
                return rom0;

            case 0x4000:
            case 0x5000:
            case 0x6000:
            case 0x7000:
                return rom1;

            case 0x8000:
            case 0x9000:
                return vram;

            case 0xA000:
            case 0xB000:
                return externalRam;

            case 0xC000:
            case 0xD000:
                return workingRam;

            case 0xE000:
                return workingRamShadow;

            // @todo need to implement a few more
            default:
                throw new IllegalArgumentException("Illegal address " + address + " passed");
        }
    }
}
