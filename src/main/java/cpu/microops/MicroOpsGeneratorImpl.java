package cpu.microops;

import cpu.alu.ALUMicroOp;
import cpu.registers.RegisterMicroOp;
import mmu.MemoryMicroOp;

import java.util.Arrays;
import java.util.List;

public class MicroOpsGeneratorImpl implements MicroOpsGenerator {

    @Override
    public List<MicroOp> generate(int instruction) {
        switch (instruction & 0xFF) {
            case 0x31:
                Arrays.asList(MemoryMicroOp.READ_PC, MemoryMicroOp.READ_PC, ALUMicroOp.JOIN_BYTES, RegisterMicroOp.WRITE_SP);

            default:
                throw new IllegalArgumentException("Instruction " + Integer.toHexString(instruction) + " has not been implemented yet");

        }
    }
}
