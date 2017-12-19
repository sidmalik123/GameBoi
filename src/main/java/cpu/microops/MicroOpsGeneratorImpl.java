package cpu.microops;

import cpu.MiscellaneousMicroOp;
import cpu.alu.ALUMicroOp;
import cpu.registers.RegisterMicroOp;
import mmu.MemoryMicroOp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MicroOpsGeneratorImpl implements MicroOpsGenerator {

    private boolean lastOpWasCB;

    @Override
    public List<MicroOp> generate(int instruction) {

        if (lastOpWasCB) {
            lastOpWasCB = false;
            return generateExtendedOpcode(instruction);
        }

        switch (instruction & 0xFF) {
            case 0x31:
                return Arrays.asList(MemoryMicroOp.READ_PC, MemoryMicroOp.READ_PC, ALUMicroOp.JOIN_BYTES, RegisterMicroOp.WRITE_SP);
            case 0x21:
                return Arrays.asList(MemoryMicroOp.READ_PC, MemoryMicroOp.READ_PC, ALUMicroOp.JOIN_BYTES, RegisterMicroOp.WRITE_HL);
            case 0x11:
                return Arrays.asList(MemoryMicroOp.READ_PC, MemoryMicroOp.READ_PC, ALUMicroOp.JOIN_BYTES, RegisterMicroOp.WRITE_DE);

            case 0x26:
                return Arrays.asList(MemoryMicroOp.READ_PC, RegisterMicroOp.WRITE_H);
            case 0x0E:
                return Arrays.asList(MemoryMicroOp.READ_PC, RegisterMicroOp.WRITE_C);
            case 0x3E:
                return Arrays.asList(MemoryMicroOp.READ_PC, RegisterMicroOp.WRITE_A);

            case 0x80:
                return Arrays.asList(RegisterMicroOp.READ_A, RegisterMicroOp.READ_B, ALUMicroOp.ADD_BYTES, RegisterMicroOp.WRITE_A);

            case 0xFE:
                return Arrays.asList(RegisterMicroOp.READ_A, MemoryMicroOp.READ_PC, ALUMicroOp.CMP);
            case 0xAF:
                return Arrays.asList(RegisterMicroOp.READ_A, RegisterMicroOp.READ_A, ALUMicroOp.XOR, RegisterMicroOp.WRITE_A);

            case 0xFF: // Todo - Restart instruction
            case 0xFB: // Todo - enable interrupts

            case 0xE2:
                return Arrays.asList(MiscellaneousMicroOp.LD_CA);

            case 0x0C:
                return Arrays.asList(RegisterMicroOp.READ_C, ALUMicroOp.INC_BYTE, RegisterMicroOp.WRITE_A);

            case 0x9F:
                return Arrays.asList(RegisterMicroOp.READ_A, RegisterMicroOp.READ_A, ALUMicroOp.SUB_CARRY_BYTES, RegisterMicroOp.WRITE_A);
            case 0x32:
                return Arrays.asList(RegisterMicroOp.READ_HL, RegisterMicroOp.READ_A, MemoryMicroOp.WRITE_ADDRESS,
                        ALUMicroOp.DEC_WORD, RegisterMicroOp.WRITE_HL);
            case 0xCB:
                lastOpWasCB = true;
                return Arrays.asList();
            case 0x20:
                return Arrays.asList(MiscellaneousMicroOp.JR_NZ);
            default:
                throw new IllegalArgumentException("Instruction " + Integer.toHexString(instruction) + " has not been implemented yet");

        }
    }

    /**
     * Generates microops for extended instructions
     * */
    private List<MicroOp> generateExtendedOpcode(int instruction) {
        switch (instruction & 0xFF) {
            case 0x7C:
                return Arrays.asList(RegisterMicroOp.READ_H, ALUMicroOp.TEST_BIT_7);
            default:
                throw new IllegalArgumentException("Extended Instruction " + Integer.toHexString(instruction)
                        + " has not been implemented yet");
        }
    }
}
