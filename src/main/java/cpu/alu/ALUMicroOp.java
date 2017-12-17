package cpu.alu;

import cpu.microops.MicroOp;

/**
 * Represents Microps possible in GameBoy's ALU
 * */
public enum ALUMicroOp implements MicroOp {

    JOIN_BYTES {
        @Override
        public int getNumCycles() {
            return 0;
        }
    };

    @Override
    public abstract int getNumCycles();
}
