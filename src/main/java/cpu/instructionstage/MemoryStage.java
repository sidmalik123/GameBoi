package cpu.instructionstage;

import core.BitUtils;
import cpu.DataBus;
import cpu.ProgramCounter;
import mmu.MMU;

/**
 * Memory reads and writes happen in this stage
 * */
public class MemoryStage implements InstructionExecuteStage {

    enum Op {READ_BYTE, WRITE_BYTE, READ_IMMEDIATE_BYTE, READ_IMMEDIATE_WORD};

    private DataBus dataBus1, dataBus2;

    private ProgramCounter programCounter;

    private Op op;

    private MMU mmu;

    public MemoryStage(MMU mmu, DataBus dataBus1, DataBus dataBus2, ProgramCounter programCounter) {
        this.mmu = mmu;
        this.dataBus1 = dataBus1;
        this.dataBus2 = dataBus2;
        this.programCounter = programCounter;
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
            case READ_IMMEDIATE_BYTE:
                dataBus1.setData(mmu.read(programCounter.getValue()));
                programCounter.inc();
                numCycles = 4; break;
            case READ_IMMEDIATE_WORD:
                int lowerByte = mmu.read(programCounter.getValue());
                programCounter.inc();
                int higherByte = mmu.read(programCounter.getValue());
                programCounter.inc();
                dataBus1.setData(BitUtils.joinBytes(higherByte, lowerByte));
                numCycles = 8; break;
            default: // ignore, data buses keep the same value
                numCycles = 0;
        }
        op = null; // reset op
        return numCycles;
    }
}
