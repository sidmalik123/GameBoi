package cpu;

import core.Word;
import mmu.GBMMU;

public class GBCPUImpl extends GBCPU {

    private GBMMU mmu;

    public void run() {

    }

    protected Byte readInstruction(Word address) {
        return mmu.readData(address);
    }

    protected void executeInstruction(Byte instruction) {

    }
}
