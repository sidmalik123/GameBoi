package cpu;

import cpu.microops.MicroOp;

/**
 * Represents microps that are hard to break down among register, alu and memory microops
 * */
public enum MiscellaneousMicroOp implements MicroOp{
    JR_NZ, JR_Z, JR_NC, JR_C,
    LD_CA;

    @Override
    public int getNumCycles() {
        return 4;
    }
}
