package cpu.instructionstage;

import cpu.DataBus;
import cpu.ProgramCounter;
import mmu.MMU;

/**
 * This stage fetches the instruction
 * */
public class FetchStage implements InstructionExecuteStage {

    private DataBus dataBus1;

    private ProgramCounter programCounter;

    private MMU mmu;

    public FetchStage(MMU mmu, DataBus dataBus1, ProgramCounter programCounter) {
        this.mmu = mmu;
        this.dataBus1 = dataBus1;
        this.programCounter = programCounter;
    }


    @Override
    public int execute() {
        // assumes databus1 contains the program counter
        dataBus1.setData(mmu.read(programCounter.getValue())); // put instruction into databus1
        programCounter.inc();
        return 4;
    }
}
