package mmu;

import core.Word;

public class GBMemorySpaceManagerImpl implements GBMemorySpaceManager {

    // private memory space fields
    private GBMemorySpace rom0;
    private GBMemorySpace rom1;
    private GBMemorySpace vram;
    private GBMemorySpace externalRam;
    private GBMemorySpace workingRam;
    private GBMemorySpace workingRamShadow;

    // BIOS, memory-mapped io, zero page, sprite info not added yet

    public GBMemorySpaceManagerImpl() {
        // set the size of all memory spaces
    }

    public GBMemorySpace getMemorySpace(Word address) {
        // based on address return the right GbMemorySpace
        switch (address.getValue() & 0xF000) {
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
                throw new IllegalArgumentException("Illegal address " + address.getValue() + " passed");
        }
    }
}
