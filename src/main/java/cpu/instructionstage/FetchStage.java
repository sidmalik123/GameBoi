package cpu.instructionstage;

import mmu.MMU;

/**
 * This stage fetches the instruction
 * */
public class FetchStage implements InstructionExecuteStage {

    private Integer dataBus1;

    private Integer programCounter;

    private MMU mmu;

    public FetchStage(MMU mmu, Integer dataBus1, Integer programCounter) {
        this.mmu = mmu;
        this.dataBus1 = dataBus1;
        this.programCounter = programCounter;
    }


    @Override
    public int execute() {
        // assumes databus1 contains the program counter
        dataBus1 = mmu.read(programCounter); // put instruction into databus1
        ++programCounter;
        return 4;
    }
}
