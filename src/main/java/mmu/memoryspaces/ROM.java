package mmu.memoryspaces;

/**
 * Read only memory
 * */
public interface ROM extends MemorySpace {

    /**
     * loads program into memory
     * */
    void load(int[] program); // Todo - replace int[] with Catridge
}
