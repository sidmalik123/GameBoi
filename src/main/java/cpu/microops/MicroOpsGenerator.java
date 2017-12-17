package cpu.microops;

import java.util.List;

public interface MicroOpsGenerator {

    /**
     * Breaks down instruction into MicroOps
     * returns them in a List
     * */
    List<MicroOp> generate(int instruction);
}
