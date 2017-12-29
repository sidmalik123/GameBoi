package mmu.memoryspaces;

import mmu.cartridge.Cartridge;

/**
 * Read only memory
 * */
public interface ROM extends MemorySpace {

    /**
     * loads program into memory
     * */
    void load(Cartridge cartridge);
}
