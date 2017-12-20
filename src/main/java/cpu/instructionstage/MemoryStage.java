package cpu.instructionstage;

import core.BitUtils;
import mmu.MMU;

/**
 * Memory reads and writes happen in this stage
 * */
public class MemoryStage implements InstructionExecuteStage {

    enum Op {READ_BYTE, WRITE_BYTE, READ_IMMEDIATE_BYTE, READ_IMMEDIATE_WORD};

    private Integer dataBus1, dataBus2;

    private Integer programCounter;

    private Op op;

    private MMU mmu;

    public MemoryStage(MMU mmu, Integer dataBus1, Integer dataBus2, Integer programCounter) {
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
                dataBus1 = mmu.read(dataBus1);
                numCycles = 4; break;
            case WRITE_BYTE:
                mmu.write(dataBus1, dataBus2);
                numCycles = 4; break;
            case READ_IMMEDIATE_BYTE:
                dataBus1 = mmu.read(programCounter);
                ++programCounter;
                numCycles = 4; break;
            case READ_IMMEDIATE_WORD:
                int lowerByte = mmu.read(programCounter++);
                int higherByte = mmu.read(programCounter++);
                dataBus1 = BitUtils.joinBytes(higherByte, lowerByte);
                numCycles = 8; break;
            default: // ignore, data buses keep the same value
                numCycles = 0;
        }
        op = null; // reset op
        return numCycles;
    }
}
