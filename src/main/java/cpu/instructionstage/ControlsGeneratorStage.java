package cpu.instructionstage;

import cpu.DataBus;

/**
 * This stage generates sets controls for stages that follow after this
 * */
public class ControlsGeneratorStage implements InstructionExecuteStage {

    private DataBus dataBus1; // this contains the instruction

    public ControlsGeneratorStage(DataBus dataBus1) {
        this.dataBus1 = dataBus1;
    }

    @Override
    public int execute() {



        return 0;
    }
}
