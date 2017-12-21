package cpu;

import core.BitUtils;
import cpu.registers.Registers;
import mmu.MMU;

import java.util.logging.Logger;

/**
 * Concrete class implementing InstructionExecutor
 * */
public class InstructionExecutorImpl implements InstructionExecutor {

    private Registers registers;
    private MMU mmu;
    private int programCounter;

    private Logger logger = Logger.getLogger(InstructionExecutorImpl.class.getName());

    private boolean lastOpWasCB;
    private static final int OFF_SET = 0xFF00;

    public InstructionExecutorImpl(Registers registers, MMU mmu) {
        this.registers = registers;
        this.mmu = mmu;
    }

    @Override
    public int executeInstruction() {
//        System.out.println("SP is " + registers.read(Registers.Register.SP));
        int instruction = mmu.read(programCounter++);

        if (lastOpWasCB) {
            lastOpWasCB = false;
            return executeExtendedOpcode(instruction);
        }

        switch (instruction) {
            case 0x00: return 4; // NOP
            // register loads
            case 0x7B: registers.write(Registers.Register.A, registers.read(Registers.Register.E)); return 4;
            case 0x7C: registers.write(Registers.Register.A, registers.read(Registers.Register.H)); return 4;
            case 0x57: registers.write(Registers.Register.D, registers.read(Registers.Register.A)); return 4;
            case 0x67: registers.write(Registers.Register.H, registers.read(Registers.Register.A)); return 4;
            case 0x4F: registers.write(Registers.Register.C, registers.read(Registers.Register.A)); return 4;
            case 0x1A: registers.write(Registers.Register.A, mmu.read(registers.read(Registers.Register.DE))); return 8;
            case 0x66: registers.write(Registers.Register.H, mmu.read(registers.read(Registers.Register.HL))); return 8;
            case 0x11: registers.write(Registers.Register.DE, getImmediateWord()); return 12;
            case 0x21: registers.write(Registers.Register.HL, getImmediateWord()); return 12;
            case 0x31: registers.write(Registers.Register.SP, getImmediateWord()); return 12;
            case 0x1E: registers.write(Registers.Register.E, getImmediateByte()); return 8;
            case 0x3E: registers.write(Registers.Register.A, getImmediateByte()); return 8;
            case 0x0E: registers.write(Registers.Register.C, getImmediateByte()); return 8;
            case 0x06: registers.write(Registers.Register.B, getImmediateByte()); return 8;
            case 0x2E: registers.write(Registers.Register.L, getImmediateByte()); return 8;
            case 0x16: registers.write(Registers.Register.D, getImmediateByte()); return 8;
            case 0xF0: registers.write(Registers.Register.A, mmu.read(OFF_SET + getImmediateByte())); return 12;

            // memory loads
            case 0x22:
                mmu.write(registers.read(Registers.Register.HL), registers.read(Registers.Register.A));
                registers.write(Registers.Register.HL, registers.read(Registers.Register.HL) + 1); return 8;
            case 0x32:
                mmu.write(registers.read(Registers.Register.HL), registers.read(Registers.Register.A));
                registers.write(Registers.Register.HL, registers.read(Registers.Register.HL) - 1); return 8;
            case 0x77:
                mmu.write(registers.read(Registers.Register.HL), registers.read(Registers.Register.A)); return 8;
            case 0xE2:
                mmu.write(OFF_SET + registers.read(Registers.Register.C), registers.read(Registers.Register.A)); return 8;
            case 0xE0:
                mmu.write(OFF_SET + getImmediateByte(), registers.read(Registers.Register.A)); return 12;
            case 0xEA:
                mmu.write(getImmediateWord(), registers.read(Registers.Register.A)); return 16;
            case 0x73:
                mmu.write(registers.read(Registers.Register.HL), registers.read(Registers.Register.E)); return 8;
            case 0x08: {
                int sp = registers.read(Registers.Register.SP);
                int address = getImmediateWord();
                mmu.write(address, sp & 0xFF);
                mmu.write(address + 1, sp >> 8);
                return 20;
            }

            // XORs
            case 0xAF: registers.write(Registers.Register.A,
                    xorBytes(registers.read(Registers.Register.A), registers.read(Registers.Register.A))); return 4;

            // INCs
            case 0x04: registers.write(Registers.Register.B, incByte(registers.read(Registers.Register.B))); return 4;
            case 0x0C: registers.write(Registers.Register.C, incByte(registers.read(Registers.Register.C))); return 4;
            case 0x24: registers.write(Registers.Register.H, incByte(registers.read(Registers.Register.H))); return 4;
            case 0x03: registers.write(Registers.Register.BC, incWord(registers.read(Registers.Register.BC))); return 8;
            case 0x13: registers.write(Registers.Register.DE, incWord(registers.read(Registers.Register.DE))); return 8;
            case 0x23: registers.write(Registers.Register.HL, incWord(registers.read(Registers.Register.HL))); return 8;

            // DECs
            case 0x05: registers.write(Registers.Register.B, decByte(registers.read(Registers.Register.B))); return 4;
            case 0x15: registers.write(Registers.Register.D, decByte(registers.read(Registers.Register.D))); return 4;
            case 0x0D: registers.write(Registers.Register.C, decByte(registers.read(Registers.Register.C))); return 4;
            case 0x1D: registers.write(Registers.Register.E, decByte(registers.read(Registers.Register.E))); return 4;
            case 0x3D: registers.write(Registers.Register.A, decByte(registers.read(Registers.Register.A))); return 4;
            case 0x0B: registers.write(Registers.Register.BC, decWord(registers.read(Registers.Register.BC))); return 8;

            // ADDs
            case 0x83: registers.write(Registers.Register.A,
                    addBytes(registers.read(Registers.Register.A), registers.read(Registers.Register.E), false)); return 4;
            case 0xCE: registers.write(Registers.Register.A,
                    addBytes(registers.read(Registers.Register.A), getImmediateByte(), true)); return 8;
            case 0x88: registers.write(Registers.Register.A,
                    addBytes(registers.read(Registers.Register.A), registers.read(Registers.Register.B), true)); return 4;
            case 0x89: registers.write(Registers.Register.A,
                    addBytes(registers.read(Registers.Register.A), registers.read(Registers.Register.C), true)); return 4;

            // SUBs
            case 0x90:registers.write(Registers.Register.A,
                    subBytes(registers.read(Registers.Register.A), registers.read(Registers.Register.B))); return 4;

            // CMPs
            case 0xFE: compareBytes(registers.read(Registers.Register.A), getImmediateByte()); return 8;

            // branch instructions
            case 0x20: { // NZ
                int jumpOffset = getImmediateByte();
                if (!registers.getFlag(Registers.Flag.ZERO)) {
                    programCounter += (byte) jumpOffset;
                    return 12;
                }
                return 8;
            }
            case 0x28: {// Z
                int jumpOffset = getImmediateByte();
                if (!registers.getFlag(Registers.Flag.ZERO)) {
                    programCounter += (byte) jumpOffset;
                    return 12;
                }
                return 8;
            }

            // stack ops
            case 0xC5: pushWord(registers.read(Registers.Register.BC)); return 16;
            case 0xC1: registers.write(Registers.Register.BC, popWord()); return 12;

            // jumps
            case 0x18: programCounter += (byte) getImmediateByte(); return 12;

            // Miscellaneous
            case 0xCC: logger.info("0xCC called - call with condition"); return 0;
            case 0xCD: callRoutine(true); return 24;
            case 0xC9: returnFromRoutine(); return 16;

            case 0xCB: lastOpWasCB = true; return 4;

            case 0x17: logger.info("0x17 called - RLA"); return 0;

            default:
                throw new IllegalArgumentException("Opcode " + Integer.toHexString(instruction) + " not implementede");

        }
    }

    /*
    * Memory ops start
    * */
    /**
     * @return unsigned immediate byte from memory
     * */
    private int getImmediateByte() {
        return mmu.read(programCounter++);
    }

    /**
     * @return  the immediate word in memory
     * */
    private int getImmediateWord() {
        int lowerByte = mmu.read(programCounter++);
        int higherByte = mmu.read(programCounter++);
        return BitUtils.joinBytes(higherByte, lowerByte);
    }
    /*
    * Memory ops end
    * */

    /*
    * ALU ops start
    * */
    /**
     * XORs byte1 and byte2
     *
     * Z 0 0 0
     * @return XORed result
     * */
    private int xorBytes(int byte1, int byte2) {
        int result = byte1 ^ byte2;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, false);
        registers.setFlag(Registers.Flag.CARRY, false);

        return result;
    }

    /**
     * tests bit bitNum of bytee
     *
     * Z 0 1 -
     * */
    private void testBit(int bytee, int bitNum) {
        registers.setFlag(Registers.Flag.ZERO, !BitUtils.isBitSet(bytee, bitNum));
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, true);
    }

    /**
     * @return bytee incremented by 1
     *
     * Z 0 H -
     * */
    private int incByte(int bytee) {
        int result = ++bytee & 0xFF;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarryAdd8Bit(bytee, 1));
        return result;
    }

    /**
     * @return bytee decremented by 1
     *
     * Z 0 H -
     * */
    private int decByte(int bytee) {
        int result = --bytee & 0xFF;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarrySub8Bit(bytee, 1));
        return result;
    }

    /**
     * @return word decremented by 1
     *
     * - - - -
     * */
    private int decWord(int word) {
        return --word & 0xFF;
    }

    /**
     * @return word incremented by 1
     *
     * - - - - (no flags affected)
     * */
    private int incWord(int word) {
        return ++word;
    }

    /**
     * Compares byte1 & byte2
     *
     * Z 1 H C
     * */
    private void compareBytes(int byte1, int byte2) {
        registers.setFlag(Registers.Flag.ZERO,byte1 == byte2);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarrySub8Bit(byte1, byte2));
        registers.setFlag(Registers.Flag.CARRY, BitUtils.isCarrySub8Bit(byte1, byte2));
    }

    /**
     * @return result after subtracting byte1 from byte2
     *
     * Z 1 H C
     * */
    private int subBytes(int byte1, int byte2) {
        int result = (byte1 - byte2) & 0xFF;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, true);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarrySub8Bit(byte1, byte2));
        registers.setFlag(Registers.Flag.CARRY, BitUtils.isCarrySub8Bit(byte1, byte2));
        return result;
    }

    /**
     * @return add result of byte1 and byte2
     *
     * Z 0 H C
     * */
    private int addBytes(int byte1, int byte2, boolean addCarry) {
        int toAdd = byte2;
        if (addCarry && registers.getFlag(Registers.Flag.CARRY)) ++toAdd;
        int result = (byte1 + toAdd) & 0xFF;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarryAdd8Bit(byte1, toAdd));
        registers.setFlag(Registers.Flag.CARRY, BitUtils.isCarryAdd8Bit(byte1, toAdd));
        return result;
    }
    /*
    * ALU ops end
    * */

    /*
    * Miscellaneous ops start
    * */
    /**
     * Calls routine at address - immediate word
     * pushes address of next instruction on stack
     * */
    private void callRoutine(boolean doCall) {
        int routineAddress = getImmediateWord();
        if (doCall) {
            int sp = registers.read(Registers.Register.SP);
            mmu.write(--sp, programCounter & 0xFF);
            mmu.write(--sp, programCounter >> 8);
            registers.write(Registers.Register.SP, sp);
            programCounter = routineAddress;
        }
    }

    /**
     * Returns from a routine
     * */
    private void returnFromRoutine() {
        int returnAdd = popWord();
        programCounter = returnAdd;
    }

    /**
     * Pushes word to stack
     * */
    private void pushWord(int word) {
        int sp = registers.read(Registers.Register.SP);
        mmu.write(--sp, word & 0xFF);
        mmu.write(--sp, word >> 8);
        registers.write(Registers.Register.SP, sp);
    }

    /**
     * Pop top word from stack
     * */
    private int popWord() {
        int sp = registers.read(Registers.Register.SP);
        int higherByte = mmu.read(sp++);
        int lowerByte = mmu.read(sp++);
        registers.write(Registers.Register.SP, sp);
        return BitUtils.joinBytes(higherByte, lowerByte);
    }
    /*
    * Miscellaneous ops end
    * */

    /**
     * Executes extended opcodes
     *
     * @return num of clock cycles taken to execute instruction
     * */
    private int executeExtendedOpcode(int instruction) {
        switch (instruction) {
            case 0x7C: testBit(registers.read(Registers.Register.H), 7); return 8;
            case 0x11: logger.info("CB 0x11 called - RL C"); return 0;

            default:
                throw new IllegalArgumentException("Extended opcode " + Integer.toHexString(instruction) + " not implementede");
        }
    }
}
