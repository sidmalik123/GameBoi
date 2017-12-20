package cpu.instructionstage;

import cpu.DataBus;

/**
 * In this stage ALUops are done
 * */
public class ALUStage implements InstructionExecuteStage {

    public enum Op {}; // ALU ops

    private DataBus dataBus1, dataBus2;

    private Op currOp;

    public ALUStage(DataBus dataBus1, DataBus dataBus2) {
        this.dataBus1 = dataBus1;
        this.dataBus2 = dataBus2;
    }

    /**
     *  assumes databus1 has first arg, databus2 has second arg.
     *  Puts the final result in databus1
     * */
    @Override
    public int execute() {
        if (currOp == null) return 0; // skip this stage
        return 0;
    }
}
