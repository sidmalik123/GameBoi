package cpu;

public interface MicroOpsGenerator {

    /**
     * Breaks down instruction into MicroOps
     * returns them in an Array
     * */
    MicroOp[] generate(int instruction);
}
