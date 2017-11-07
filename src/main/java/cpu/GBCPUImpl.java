package cpu;

import core.Address;
import core.GBAddress;
import core.GBInstruction;
import core.Instruction;
import mmu.GBMMU;

public class GBCPUImpl extends GBCPU {

    private GBMMU mmu;

    public void run() {

    }

    protected GBInstruction readInstruction(GBAddress address) {
        return new GBInstruction(mmu.readData(address));
    }

    protected void executeInstruction(GBInstruction instruction) {

    }
}
