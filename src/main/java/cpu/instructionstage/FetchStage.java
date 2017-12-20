package cpu.instructionstage;

import cpu.DataBus;
import mmu.MMU;

/**
 * This stage fetches the instruction
 * */
public class FetchStage implements InstructionExecuteStage {

    private DataBus dataBus1;

    private MMU mmu;

    public FetchStage(MMU mmu, DataBus dataBus1) {
        this.mmu = mmu;
        this.dataBus1 = dataBus1;
    }


    @Override
    public int execute() {
        // assumes databus1 contains the program counter
        dataBus1.setData(mmu.read(dataBus1.getData())); // put instruction into databus1
        return 4;
    }
}
