package cpu;

import core.AbstractTimingSubject;
import core.BitUtils;
import cpu.interrupts.GBInterruptManager;
import mmu.GBMMU;

public class GBCPUImpl extends AbstractGBCPUImpl {

    private static final int LOAD_SPECIAL_ADDRESS = 0xFF00;
    private static final int CPU_FREQUENCY = 4194304;

    private GBRegisterManager registerManager;

    private GBInterruptManager interruptManager;

    private int programCounter; // set by the mmu on program load

    public void run(String programLocation) {
        // Todo - process interrupts after every instruction
    }

    public int getFrequency() {
        return CPU_FREQUENCY;
    }

    private int readInstruction() {
        return readMemory(programCounter);
    }

    /**
     * Execute instruction
     * */
    private void executeInstruction(int instruction) { // todo - update programCounter at the end

        int opCode = instruction & 0xFF;

        switch (opCode) {
            // load register with immediate byte
            case 0x06: registerManager.set(SingleRegister.B, getImmediateByte());
            case 0x0E: registerManager.set(SingleRegister.C, getImmediateByte());
            case 0x16: registerManager.set(SingleRegister.D, getImmediateByte());
            case 0x1E: registerManager.set(SingleRegister.E, getImmediateByte());
            case 0x26: registerManager.set(SingleRegister.H, getImmediateByte());
            case 0x2E: registerManager.set(SingleRegister.L, getImmediateByte());

            // set register from register
            case 0x7F: registerManager.set(SingleRegister.A, SingleRegister.A);
            case 0x78: registerManager.set(SingleRegister.A, SingleRegister.B);
            case 0x79: registerManager.set(SingleRegister.A, SingleRegister.C);
            case 0x7A: registerManager.set(SingleRegister.A, SingleRegister.D);
            case 0x7B: registerManager.set(SingleRegister.A, SingleRegister.E);
            case 0x7C: registerManager.set(SingleRegister.A, SingleRegister.H);
            case 0x7D: registerManager.set(SingleRegister.A, SingleRegister.L);
            case 0x40: registerManager.set(SingleRegister.B, SingleRegister.B);
            case 0x41: registerManager.set(SingleRegister.B, SingleRegister.C);
            case 0x42: registerManager.set(SingleRegister.B, SingleRegister.D);
            case 0x43: registerManager.set(SingleRegister.B, SingleRegister.E);
            case 0x44: registerManager.set(SingleRegister.B, SingleRegister.H);
            case 0x45: registerManager.set(SingleRegister.B, SingleRegister.L);
            case 0x48: registerManager.set(SingleRegister.C, SingleRegister.B);
            case 0x49: registerManager.set(SingleRegister.C, SingleRegister.C);
            case 0x4A: registerManager.set(SingleRegister.C, SingleRegister.D);
            case 0x4B: registerManager.set(SingleRegister.C, SingleRegister.E);
            case 0x4C: registerManager.set(SingleRegister.C, SingleRegister.H);
            case 0x4D: registerManager.set(SingleRegister.C, SingleRegister.L);
            case 0x50: registerManager.set(SingleRegister.D, SingleRegister.B);
            case 0x51: registerManager.set(SingleRegister.D, SingleRegister.C);
            case 0x52: registerManager.set(SingleRegister.D, SingleRegister.D);
            case 0x53: registerManager.set(SingleRegister.D, SingleRegister.E);
            case 0x54: registerManager.set(SingleRegister.D, SingleRegister.H);
            case 0x55: registerManager.set(SingleRegister.D, SingleRegister.L);
            case 0x58: registerManager.set(SingleRegister.E, SingleRegister.B);
            case 0x59: registerManager.set(SingleRegister.E, SingleRegister.C);
            case 0x5A: registerManager.set(SingleRegister.E, SingleRegister.D);
            case 0x5B: registerManager.set(SingleRegister.E, SingleRegister.E);
            case 0x5C: registerManager.set(SingleRegister.E, SingleRegister.H);
            case 0x5D: registerManager.set(SingleRegister.E, SingleRegister.L);
            case 0x60: registerManager.set(SingleRegister.H, SingleRegister.B);
            case 0x61: registerManager.set(SingleRegister.H, SingleRegister.C);
            case 0x62: registerManager.set(SingleRegister.H, SingleRegister.D);
            case 0x63: registerManager.set(SingleRegister.H, SingleRegister.E);
            case 0x64: registerManager.set(SingleRegister.H, SingleRegister.H);
            case 0x65: registerManager.set(SingleRegister.H, SingleRegister.L);
            case 0x68: registerManager.set(SingleRegister.L, SingleRegister.B);
            case 0x69: registerManager.set(SingleRegister.L, SingleRegister.C);
            case 0x6A: registerManager.set(SingleRegister.L, SingleRegister.D);
            case 0x6B: registerManager.set(SingleRegister.L, SingleRegister.E);
            case 0x6C: registerManager.set(SingleRegister.L, SingleRegister.H);
            case 0x6D: registerManager.set(SingleRegister.L, SingleRegister.L);
            case 0x47: registerManager.set(SingleRegister.B, SingleRegister.A);
            case 0x4F: registerManager.set(SingleRegister.C, SingleRegister.A);
            case 0x57: registerManager.set(SingleRegister.D, SingleRegister.A);
            case 0x5F: registerManager.set(SingleRegister.E, SingleRegister.A);
            case 0x67: registerManager.set(SingleRegister.H, SingleRegister.A);
            case 0x6F: registerManager.set(SingleRegister.L, SingleRegister.A);
            case 0xF9: registerManager.set(DoubleRegister.SP, DoubleRegister.HL);

            // write register value to memory
            case 0x70: writeToMemory(registerManager.get(DoubleRegister.HL), registerManager.get(SingleRegister.B));
            case 0x71: writeToMemory(registerManager.get(DoubleRegister.HL), registerManager.get(SingleRegister.C));
            case 0x72: writeToMemory(registerManager.get(DoubleRegister.HL), registerManager.get(SingleRegister.D));
            case 0x73: writeToMemory(registerManager.get(DoubleRegister.HL), registerManager.get(SingleRegister.E));
            case 0x74: writeToMemory(registerManager.get(DoubleRegister.HL), registerManager.get(SingleRegister.H));
            case 0x75: writeToMemory(registerManager.get(DoubleRegister.HL), registerManager.get(SingleRegister.L));
            case 0x02: writeToMemory(registerManager.get(DoubleRegister.BC), registerManager.get(SingleRegister.A));
            case 0x12: writeToMemory(registerManager.get(DoubleRegister.DE), registerManager.get(SingleRegister.A));
            case 0x77: writeToMemory(registerManager.get(DoubleRegister.HL), registerManager.get(SingleRegister.A));
            case 0xE2: writeToMemory(registerManager.get(SingleRegister.C) + LOAD_SPECIAL_ADDRESS,
                    registerManager.get(SingleRegister.A));

            // set register with value from memory
            case 0x7E: registerManager.set(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0x46: registerManager.set(SingleRegister.B, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0x4E: registerManager.set(SingleRegister.C, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0x56: registerManager.set(SingleRegister.D, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0x5E: registerManager.set(SingleRegister.E, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0x66: registerManager.set(SingleRegister.H, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0x6E: registerManager.set(SingleRegister.L, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0x0A: registerManager.set(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.BC)));
            case 0x1A: registerManager.set(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.DE)));
            case 0xF2: registerManager.set(SingleRegister.A,
                    readMemory(registerManager.get(SingleRegister.C) + LOAD_SPECIAL_ADDRESS));

            case 0x3A: {
                int address = registerManager.get(DoubleRegister.HL);
                registerManager.set(SingleRegister.A, readMemory(address));
                registerManager.set(DoubleRegister.HL, address - 1);
            }
            case 0x2A: {
                int address = registerManager.get(DoubleRegister.HL);
                registerManager.set(SingleRegister.A, readMemory(address));
                registerManager.set(DoubleRegister.HL, address + 1);
            }

            case 0x32: {
                int address = registerManager.get(DoubleRegister.HL);
                writeToMemory(address, registerManager.get(SingleRegister.A));
                registerManager.set(DoubleRegister.HL, address - 1);
            }
            case 0x22: {
                int address = registerManager.get(DoubleRegister.HL);
                writeToMemory(address, registerManager.get(SingleRegister.A));
                registerManager.set(DoubleRegister.HL, address + 1);
            }

            // set double register with immediate word
            case 0x01: registerManager.set(DoubleRegister.BC, getImmediateWord());
            case 0x11: registerManager.set(DoubleRegister.DE, getImmediateWord());
            case 0x21: registerManager.set(DoubleRegister.HL, getImmediateWord());
            case 0x31: registerManager.set(DoubleRegister.SP, getImmediateWord());

            // push register to stack
            case 0xF5: pushToStack(registerManager.get(DoubleRegister.AF));
            case 0xC5: pushToStack(registerManager.get(DoubleRegister.BC));
            case 0xD5: pushToStack(registerManager.get(DoubleRegister.DE));
            case 0xE5: pushToStack(registerManager.get(DoubleRegister.HL));

            // pop stack into register
            case 0xF1: registerManager.set(DoubleRegister.AF, popStack());
            case 0xC1: registerManager.set(DoubleRegister.BC, popStack());
            case 0xD1: registerManager.set(DoubleRegister.DE, popStack());
            case 0xE1: registerManager.set(DoubleRegister.HL, popStack());

            case 0x87: add(SingleRegister.A, registerManager.get(SingleRegister.A), false);
            case 0x80: add(SingleRegister.A, registerManager.get(SingleRegister.B), false);
            case 0x81: add(SingleRegister.A, registerManager.get(SingleRegister.C), false);
            case 0x82: add(SingleRegister.A, registerManager.get(SingleRegister.D), false);
            case 0x83: add(SingleRegister.A, registerManager.get(SingleRegister.E), false);
            case 0x84: add(SingleRegister.A, registerManager.get(SingleRegister.H), false);
            case 0x85: add(SingleRegister.A, registerManager.get(SingleRegister.L), false);
            case 0x86: add(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)), false);
            case 0xC6: add(SingleRegister.A, getImmediateByte(), false);

            case 0x8F: add(SingleRegister.A, registerManager.get(SingleRegister.A), true);
            case 0x88: add(SingleRegister.A, registerManager.get(SingleRegister.B), true);
            case 0x89: add(SingleRegister.A, registerManager.get(SingleRegister.C), true);
            case 0x8A: add(SingleRegister.A, registerManager.get(SingleRegister.D), true);
            case 0x8B: add(SingleRegister.A, registerManager.get(SingleRegister.E), true);
            case 0x8C: add(SingleRegister.A, registerManager.get(SingleRegister.H), true);
            case 0x8D: add(SingleRegister.A, registerManager.get(SingleRegister.L), true);
            case 0x8E: add(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)), true);
            case 0xCE: add(SingleRegister.A, getImmediateByte(), true);

            case 0x97: sub(SingleRegister.A, registerManager.get(SingleRegister.A), false);
            case 0x90: sub(SingleRegister.A, registerManager.get(SingleRegister.B), false);
            case 0x91: sub(SingleRegister.A, registerManager.get(SingleRegister.C), false);
            case 0x92: sub(SingleRegister.A, registerManager.get(SingleRegister.D), false);
            case 0x93: sub(SingleRegister.A, registerManager.get(SingleRegister.E), false);
            case 0x94: sub(SingleRegister.A, registerManager.get(SingleRegister.H), false);
            case 0x95: sub(SingleRegister.A, registerManager.get(SingleRegister.L), false);
            case 0x96: sub(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)), false);
            case 0xD6: sub(SingleRegister.A, getImmediateByte(),false);

            case 0x9F: sub(SingleRegister.A, registerManager.get(SingleRegister.A), true);
            case 0x98: sub(SingleRegister.A, registerManager.get(SingleRegister.B), true);
            case 0x99: sub(SingleRegister.A, registerManager.get(SingleRegister.C), true);
            case 0x9A: sub(SingleRegister.A, registerManager.get(SingleRegister.D), true);
            case 0x9B: sub(SingleRegister.A, registerManager.get(SingleRegister.E), true);
            case 0x9C: sub(SingleRegister.A, registerManager.get(SingleRegister.H), true);
            case 0x9D: sub(SingleRegister.A, registerManager.get(SingleRegister.L), true);
            case 0x9E: sub(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)), true);
            case 0xDE: sub(SingleRegister.A, getImmediateByte(),true);

            case 0xA7: and(SingleRegister.A, registerManager.get(SingleRegister.A));
            case 0xA0: and(SingleRegister.A, registerManager.get(SingleRegister.B));
            case 0xA1: and(SingleRegister.A, registerManager.get(SingleRegister.C));
            case 0xA2: and(SingleRegister.A, registerManager.get(SingleRegister.D));
            case 0xA3: and(SingleRegister.A, registerManager.get(SingleRegister.E));
            case 0xA4: and(SingleRegister.A, registerManager.get(SingleRegister.H));
            case 0xA5: and(SingleRegister.A, registerManager.get(SingleRegister.L));
            case 0xA6: and(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0xE6: and(SingleRegister.A, getImmediateByte());

            case 0xB7: or(SingleRegister.A, registerManager.get(SingleRegister.A));
            case 0xB0: or(SingleRegister.A, registerManager.get(SingleRegister.B));
            case 0xB1: or(SingleRegister.A, registerManager.get(SingleRegister.C));
            case 0xB2: or(SingleRegister.A, registerManager.get(SingleRegister.D));
            case 0xB3: or(SingleRegister.A, registerManager.get(SingleRegister.E));
            case 0xB4: or(SingleRegister.A, registerManager.get(SingleRegister.H));
            case 0xB5: or(SingleRegister.A, registerManager.get(SingleRegister.L));
            case 0xB6: or(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0xF6: or(SingleRegister.A, getImmediateByte());

            case 0xAF: xor(SingleRegister.A, registerManager.get(SingleRegister.A));
            case 0xA8: xor(SingleRegister.A, registerManager.get(SingleRegister.B));
            case 0xA9: xor(SingleRegister.A, registerManager.get(SingleRegister.C));
            case 0xAA: xor(SingleRegister.A, registerManager.get(SingleRegister.D));
            case 0xAB: xor(SingleRegister.A, registerManager.get(SingleRegister.E));
            case 0xAC: xor(SingleRegister.A, registerManager.get(SingleRegister.H));
            case 0xAD: xor(SingleRegister.A, registerManager.get(SingleRegister.L));
            case 0xAE: xor(SingleRegister.A, readMemory(registerManager.get(DoubleRegister.HL)));
            case 0xEE: xor(SingleRegister.A, getImmediateByte());

            default:
                throw new IllegalArgumentException("Unknow opcode: " + opCode);
        }

    }

    /**
     * Reads and returns the next 8 immediate bits from memory
     * */
    private int getImmediateByte() {
        return readMemory(++programCounter);
    }

    /**
     * Reads and returns the next 16 immediate bits from memory
     * */
    private int getImmediateWord() {
        int val1 = readMemory(++programCounter);
        int val2 = readMemory(++programCounter);
        return val2 << 8 | val1;
    }

    /**
     * Pushes data on to the stack, decrements the stack pointer
     * */
    private void pushToStack(int data) {
        int stackPointer = registerManager.get(DoubleRegister.SP);
        writeToMemory(stackPointer - 1, data);
        registerManager.set(DoubleRegister.SP, stackPointer - 1);
    }

    /**
     * Performs addition on of val1 and val2,
     * if addCarry is true and the carry flag is set, result is incremented by 1
     *
     * Sets the carry, half-carry and zero flag
     * */
    private int performAdd(int val1, int val2, boolean addCarry) {
        int result;
        int toAdd = val2;
        if (addCarry && registerManager.getCarryFlag()) ++toAdd;
        result = val1 + toAdd;

        registerManager.setZeroFlag((result & 0xFF) == 0);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarryAdd(val1,toAdd));
        registerManager.setCarryFlag(BitUtils.isCarryAdd(val1, toAdd));

        return result;
    }

    /**
     * Performs subtraction on of val1 and val2,
     * if addCarry is true and the carry flag is set, result is decremented by 1
     *
     * Sets the carry, half-carry and zero flag and operation flag
     * */
    private int performSub(int val1, int val2, boolean subCarry) {
        int result;
        int toSub = val2;
        if (subCarry && registerManager.getCarryFlag()) ++toSub;
        result = val1 - toSub;

        registerManager.setZeroFlag((result & 0xFF) == 0);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarrySub(val1, toSub));
        registerManager.setCarryFlag(BitUtils.isCarrySub(val1, toSub));
        registerManager.setOperationFlag(true);

        return result;
    }

    /**
     * Performs bitwise AND on val1 and val2,
     * Sets zero flag if result is zero, sets half-carry flag,
     * resets carry and operation flag
     *
     * @return result of the AND
     * */
    private int performAnd(int val1, int val2) {
        int result = val1 & val2;

        registerManager.setZeroFlag((result & 0xFF) == 0);
        registerManager.setHalfCarryFlag(true);
        registerManager.setCarryFlag(false);
        registerManager.setOperationFlag(false);

        return result;
    }

    /**
     * Performs bitwise OR on val1 and val2,
     * Sets zero flag if result is zero
     * resets carry, operation flag and half-carry flag
     *
     * @return result of the AND
     * */
    private int performOr(int val1, int val2) {
        int result = val1 | val2;

        registerManager.setZeroFlag((result & 0xFF) == 0);
        registerManager.setHalfCarryFlag(false);
        registerManager.setCarryFlag(false);
        registerManager.setOperationFlag(false);

        return result;
    }

    /**
     * Performs bitwise XOR on val1 and val2,
     * Sets zero flag if result is zero
     *
     * @return result of the AND
     * */
    private int performXor(int val1, int val2) {
        int result = val1 ^ val2;

        registerManager.setZeroFlag((result & 0xFF) == 0);

        return result;
    }

    /**
     * Pops top element of the stack increments the stack pointer
     *
     * @return element popped
     * */
    private int popStack() {
        int stackPointer = registerManager.get(DoubleRegister.SP);
        registerManager.set(DoubleRegister.SP, stackPointer + 1);
        return readMemory(stackPointer);
    }

    /**
     * Performs addition on r.val and toAdd, stores the result in r
     * */
    private void add(SingleRegister r, int toAdd, boolean addCarry) {
        registerManager.set(r, performAdd(registerManager.get(r), toAdd, addCarry));
    }

    /**
     * Performs subtraction on r.val and toSub, stores the result in r
     * */
    private void sub(SingleRegister r, int toSub, boolean addCarry) {
        registerManager.set(r, performSub(registerManager.get(r), toSub, addCarry));
    }

    /**
     * Performs and on r.val and toAnd, stores the result in r
     * */
    private void and(SingleRegister r, int toAnd) {
        registerManager.set(r, performAnd(registerManager.get(r), toAnd));
    }

    /**
     * Performs or on r.val and toAnd, stores the result in r
     * */
    private void or(SingleRegister r, int toAnd) {
        registerManager.set(r, performOr(registerManager.get(r), toAnd));
    }

    /**
     * Performs xor on r.val and toAnd, stores the result in r
     * */
    private void xor(SingleRegister r, int toAnd) {
        registerManager.set(r, performXor(registerManager.get(r), toAnd));
    }


}
