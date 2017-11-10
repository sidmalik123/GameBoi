package cpu;

import mmu.MMU;

public class GBCPU extends AbstractCPU {

    private MMU mmu;

    private GBRegisterManager registerManager;

    private int PC; // set by the mmu on program load

    private int numCyclesPassed;

    public void run(String programLocation) {

    }

    protected int readInstruction(int address) {
        return mmu.readData(address);
    }

    protected void executeInstruction(int instruction) {

    }

    // private methods for each instruction type
}
