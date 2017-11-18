package mmu;

public enum MemoryType {
    ROM0, ROM1, VRAM, EXTERNAL_RAM, WORKING_RAM, WORKING_RAM_SHADOW, SPRITE_MEMORY, IO_MEMORY, ZERO_PAGE_RAM;

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

    public int getStartAddress() {
        switch (this) {
            case ROM0:
                return ROM0_START_ADDRESS;
            case ROM1:
                return ROM1_START_ADDRESS;
            case VRAM:
                return VRAM_START_ADDRESS;
            case EXTERNAL_RAM:
                return EXTERNAL_RAM_START_ADDRESS;
            case WORKING_RAM:
                return WORKING_RAM_START_ADDRESS;
            case WORKING_RAM_SHADOW:
                return WORKING_RAM_SHADOW_START_ADDRESS;
            case SPRITE_MEMORY:
                return SPRITE_START_ADDRESS;
            case IO_MEMORY:
                return IO_MEMORY_START_ADDRESS;
            case ZERO_PAGE_RAM:
                return ZERO_PAGE_START_ADDRESS;
        }
        throw new IllegalArgumentException("Unknown MemoryType " + this);
    }

    public int getEndAddress() {
        switch (this) {
            case ROM0:
                return ROM0_END_ADDRESS;
            case ROM1:
                return ROM1_END_ADDRESS;
            case VRAM:
                return VRAM_END_ADDRESS;
            case EXTERNAL_RAM:
                return EXTERNAL_RAM_END_ADDRESS;
            case WORKING_RAM:
                return WORKING_RAM_END_ADDRESS;
            case WORKING_RAM_SHADOW:
                return WORKING_RAM_SHADOW_END_ADDRESS;
            case SPRITE_MEMORY:
                return SPRITE_END_ADDRESS;
            case IO_MEMORY:
                return IO_MEMORY_END_ADDRESS;
            case ZERO_PAGE_RAM:
                return ZERO_PAGE_END_ADDRESS;
        }
        throw new IllegalArgumentException("Unknown MemoryType " + this);
    }
}
