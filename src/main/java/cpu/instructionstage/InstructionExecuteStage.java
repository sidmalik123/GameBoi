package cpu.instructionstage;

/**
 *  Represents a stage in instruction execution
 * */
public interface InstructionExecuteStage {

    /**
     * Performs the work needed in this stage
     *
     * @return num cycles spent in this stage
     * */
    int execute();
}
