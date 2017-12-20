package cpu.instructionstage;

import core.BitUtils;
import cpu.DataBus;
import mmu.MMU;

/**
 * Memory reads and writes happen in this stage
 * */
public class MemoryStage implements InstructionExecuteStage {

    enum Op {READ_BYTE, WRITE_BYTE, READ_WORD};

    private DataBus dataBus1, dataBus2;

    private Op op;

    private MMU mmu;

    public MemoryStage(MMU mmu, DataBus dataBus1, DataBus dataBus2) {
        this.mmu = mmu;
        this.dataBus1 = dataBus1;
        this.dataBus2 = dataBus2;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    @Override
    public int execute() {
        int numCycles;
        switch (op) {
            case READ_BYTE:
                dataBus1.setData(mmu.read(dataBus1.getData()));
                numCycles = 4; break;
            case WRITE_BYTE:
                mmu.write(dataBus1.getData(), dataBus2.getData());
                numCycles = 4; break;
            case READ_WORD:
                int address = dataBus1.getData();
                dataBus1.setData(BitUtils.joinBytes(mmu.read(address+1), mmu.read(address)));
                numCycles = 8; break;
            default: // ignore, data buses keep the same value
                numCycles = 0;
        }
        op = null; // reset op
        return numCycles;
    }
}
