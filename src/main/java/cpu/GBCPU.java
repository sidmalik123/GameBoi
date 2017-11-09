package cpu;

import mmu.MMU;

public class GBCPU extends AbstractCPU {

    private MMU mmu;

    // registers
    GBRegisterManager registerManager;

    public void run() {

    }

    protected int readInstruction(int address) {
        return mmu.readData(address);
    }

    protected void executeInstruction(int instruction) {

    }
}
