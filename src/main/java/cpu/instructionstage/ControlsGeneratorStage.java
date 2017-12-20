package cpu.instructionstage;

/**
 * This stage generates sets controls for stages that follow after this
 * */
public class ControlsGeneratorStage implements InstructionExecuteStage {

    private Integer dataBus1; // this contains the instruction

    public ControlsGeneratorStage(Integer dataBus1) {
        this.dataBus1 = dataBus1;
    }

    @Override
    public int execute() {



        return 0;
    }
}
