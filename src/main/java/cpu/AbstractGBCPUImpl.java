package cpu;


import core.AbstractTimingSubject;
import mmu.GBMMU;

public abstract class AbstractGBCPUImpl extends AbstractTimingSubject implements GBCPU {

    private GBMMU mmu;
    private static final int NUM_CYCLES_TO_READ_MEMORY = 4;
    private static final int NUM_CYLES_TO_WRTIE_TO_MEMORY = 4;

    /**
     *  Reads the memory at address,
     *  NOTE: this must be used to read memory always in this class,
     *  as this notifies the observers of the num cycles taken to do this
     * */
    protected int readMemory(int address) {
        notifyTimingObservers(NUM_CYCLES_TO_READ_MEMORY);
        return mmu.readData(address);
    }

    /**
     *  Writes data to memory at address,
     *  NOTE: this must be used to write to memory always in this class,
     *  as this notifies the observers of the num cycles taken to do this
     * */
    protected void writeToMemory(int address, int data) {
        notifyTimingObservers(NUM_CYLES_TO_WRTIE_TO_MEMORY);
        mmu.writeData(address, data);
    }

}
