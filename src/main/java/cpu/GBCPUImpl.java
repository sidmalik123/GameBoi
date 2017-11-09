package cpu;

import mmu.GBMMU;

public class GBCPUImpl extends AbstractCPU {

    private GBMMU mmu;

    public void run() {

    }

    protected int readInstruction(int address) {
        return mmu.readData(address);
    }

    protected void executeInstruction(int instruction) {

    }
}
