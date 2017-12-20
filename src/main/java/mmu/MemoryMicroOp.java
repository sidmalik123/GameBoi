package mmu;

public enum MemoryMicroOp implements MicroOp {

    READ_PC, READ_ADDRESS, WRITE_ADDRESS;

    private static final int NUM_CYCLES_TO_ACCESS_MEMORY = 4;

    @Override
    public int getNumCycles() {
        return NUM_CYCLES_TO_ACCESS_MEMORY;
    }
}
