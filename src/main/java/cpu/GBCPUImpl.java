package cpu;

import core.AbstractTimingSubject;
import core.BitUtils;
import cpu.interrupts.GBInterruptManager;
import mmu.GBMMU;

public class GBCPUImpl extends AbstractTimingSubject implements GBCPU {

    private static final int LOAD_SPECIAL_ADDRESS = 0xFF00;
    private static final int CPU_FREQUENCY = 4194304;

    private GBMMU mmu;

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
        return mmu.readData(programCounter);
    }

    /**
     * Execute instruction
     *
     * @return num of clock cycles taken to execute the instruction
     * */
    private int executeInstruction(int instruction) { // Note - update programCounter at the end

        int opCode = instruction & 0xFF;

        switch (opCode) {
            case 0x06: return loadImmediateByteIntoRegister(SingleRegister.B);
            case 0x0E: return loadImmediateByteIntoRegister(SingleRegister.C);
            case 0x16: return loadImmediateByteIntoRegister(SingleRegister.D);
            case 0x1E: return loadImmediateByteIntoRegister(SingleRegister.E);
            case 0x26: return loadImmediateByteIntoRegister(SingleRegister.H);
            case 0x2E: return loadImmediateByteIntoRegister(SingleRegister.L);

            case 0x7F: return loadRegisterFromRegister(SingleRegister.A, SingleRegister.A);
            case 0x78: return loadRegisterFromRegister(SingleRegister.A, SingleRegister.B);
            case 0x79: return loadRegisterFromRegister(SingleRegister.A, SingleRegister.C);
            case 0x7A: return loadRegisterFromRegister(SingleRegister.A, SingleRegister.D);
            case 0x7B: return loadRegisterFromRegister(SingleRegister.A, SingleRegister.E);
            case 0x7C: return loadRegisterFromRegister(SingleRegister.A, SingleRegister.H);
            case 0x7D: return loadRegisterFromRegister(SingleRegister.A, SingleRegister.L);
            case 0x40: return loadRegisterFromRegister(SingleRegister.B, SingleRegister.B);
            case 0x41: return loadRegisterFromRegister(SingleRegister.B, SingleRegister.C);
            case 0x42: return loadRegisterFromRegister(SingleRegister.B, SingleRegister.D);
            case 0x43: return loadRegisterFromRegister(SingleRegister.B, SingleRegister.E);
            case 0x44: return loadRegisterFromRegister(SingleRegister.B, SingleRegister.H);
            case 0x45: return loadRegisterFromRegister(SingleRegister.B, SingleRegister.L);
            case 0x48: return loadRegisterFromRegister(SingleRegister.C, SingleRegister.B);
            case 0x49: return loadRegisterFromRegister(SingleRegister.C, SingleRegister.C);
            case 0x4A: return loadRegisterFromRegister(SingleRegister.C, SingleRegister.D);
            case 0x4B: return loadRegisterFromRegister(SingleRegister.C, SingleRegister.E);
            case 0x4C: return loadRegisterFromRegister(SingleRegister.C, SingleRegister.H);
            case 0x4D: return loadRegisterFromRegister(SingleRegister.C, SingleRegister.L);
            case 0x50: return loadRegisterFromRegister(SingleRegister.D, SingleRegister.B);
            case 0x51: return loadRegisterFromRegister(SingleRegister.D, SingleRegister.C);
            case 0x52: return loadRegisterFromRegister(SingleRegister.D, SingleRegister.D);
            case 0x53: return loadRegisterFromRegister(SingleRegister.D, SingleRegister.E);
            case 0x54: return loadRegisterFromRegister(SingleRegister.D, SingleRegister.H);
            case 0x55: return loadRegisterFromRegister(SingleRegister.D, SingleRegister.L);
            case 0x58: return loadRegisterFromRegister(SingleRegister.E, SingleRegister.B);
            case 0x59: return loadRegisterFromRegister(SingleRegister.E, SingleRegister.C);
            case 0x5A: return loadRegisterFromRegister(SingleRegister.E, SingleRegister.D);
            case 0x5B: return loadRegisterFromRegister(SingleRegister.E, SingleRegister.E);
            case 0x5C: return loadRegisterFromRegister(SingleRegister.E, SingleRegister.H);
            case 0x5D: return loadRegisterFromRegister(SingleRegister.E, SingleRegister.L);
            case 0x60: return loadRegisterFromRegister(SingleRegister.H, SingleRegister.B);
            case 0x61: return loadRegisterFromRegister(SingleRegister.H, SingleRegister.C);
            case 0x62: return loadRegisterFromRegister(SingleRegister.H, SingleRegister.D);
            case 0x63: return loadRegisterFromRegister(SingleRegister.H, SingleRegister.E);
            case 0x64: return loadRegisterFromRegister(SingleRegister.H, SingleRegister.H);
            case 0x65: return loadRegisterFromRegister(SingleRegister.H, SingleRegister.L);
            case 0x68: return loadRegisterFromRegister(SingleRegister.L, SingleRegister.B);
            case 0x69: return loadRegisterFromRegister(SingleRegister.L, SingleRegister.C);
            case 0x6A: return loadRegisterFromRegister(SingleRegister.L, SingleRegister.D);
            case 0x6B: return loadRegisterFromRegister(SingleRegister.L, SingleRegister.E);
            case 0x6C: return loadRegisterFromRegister(SingleRegister.L, SingleRegister.H);
            case 0x6D: return loadRegisterFromRegister(SingleRegister.L, SingleRegister.L);
            case 0x47: return loadRegisterFromRegister(SingleRegister.B, SingleRegister.A);
            case 0x4F: return loadRegisterFromRegister(SingleRegister.C, SingleRegister.A);
            case 0x57: return loadRegisterFromRegister(SingleRegister.D, SingleRegister.A);
            case 0x5F: return loadRegisterFromRegister(SingleRegister.E, SingleRegister.A);
            case 0x67: return loadRegisterFromRegister(SingleRegister.H, SingleRegister.A);
            case 0x6F: return loadRegisterFromRegister(SingleRegister.L, SingleRegister.A);
            case 0xF9: return loadRegisterFromRegister(DoubleRegister.SP, DoubleRegister.HL);

            case 0x70: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.B);
            case 0x71: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.C);
            case 0x72: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.D);
            case 0x73: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.E);
            case 0x74: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.H);
            case 0x75: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.L);
            case 0x02: return writeRegisterToMemory(DoubleRegister.BC, SingleRegister.A);
            case 0x12: return writeRegisterToMemory(DoubleRegister.DE, SingleRegister.A);
            case 0x77: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.A);
            case 0xE2: return writeRegisterToMemory(SingleRegister.C, SingleRegister.L);

            case 0x7E: return writeMemoryToRegister(SingleRegister.A, DoubleRegister.HL);
            case 0x46: return writeMemoryToRegister(SingleRegister.B, DoubleRegister.HL);
            case 0x4E: return writeMemoryToRegister(SingleRegister.C, DoubleRegister.HL);
            case 0x56: return writeMemoryToRegister(SingleRegister.D, DoubleRegister.HL);
            case 0x5E: return writeMemoryToRegister(SingleRegister.E, DoubleRegister.HL);
            case 0x66: return writeMemoryToRegister(SingleRegister.H, DoubleRegister.HL);
            case 0x6E: return writeMemoryToRegister(SingleRegister.L, DoubleRegister.HL);
            case 0x0A: return writeMemoryToRegister(SingleRegister.A, DoubleRegister.BC);
            case 0x1A: return writeMemoryToRegister(SingleRegister.A, DoubleRegister.DE);
            case 0xF2: return writeMemoryToRegister(SingleRegister.A, SingleRegister.C);

            case 0x3A: return (writeMemoryToRegister(SingleRegister.A, DoubleRegister.HL) + decrementRegister(DoubleRegister.HL));
            case 0x2A: return (writeMemoryToRegister(SingleRegister.A, DoubleRegister.HL) + incrementRegister(DoubleRegister.HL));

            case 0x32: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.A) + decrementRegister(DoubleRegister.HL);
            case 0x22: return writeRegisterToMemory(DoubleRegister.HL, SingleRegister.A) + incrementRegister(DoubleRegister.HL);

            case 0x01: return loadImmediateWordIntoRegister(DoubleRegister.BC);
            case 0x11: return loadImmediateWordIntoRegister(DoubleRegister.DE);
            case 0x21: return loadImmediateWordIntoRegister(DoubleRegister.HL);
            case 0x31: return loadImmediateWordIntoRegister(DoubleRegister.SP);

            case 0xF5: return pushRegisterToStack(DoubleRegister.AF);
            case 0xC5: return pushRegisterToStack(DoubleRegister.BC);
            case 0xD5: return pushRegisterToStack(DoubleRegister.DE);
            case 0xE5: return pushRegisterToStack(DoubleRegister.HL);

            case 0xF1: return popStackIntoRegister(DoubleRegister.AF);
            case 0xC1: return popStackIntoRegister(DoubleRegister.BC);
            case 0xD1: return popStackIntoRegister(DoubleRegister.DE);
            case 0xE1: return popStackIntoRegister(DoubleRegister.HL);

            case 0x87: return addRegisterToRegister(SingleRegister.A, SingleRegister.A, false);
            case 0x80: return addRegisterToRegister(SingleRegister.A, SingleRegister.B, false);
            case 0x81: return addRegisterToRegister(SingleRegister.A, SingleRegister.C, false);
            case 0x82: return addRegisterToRegister(SingleRegister.A, SingleRegister.D, false);
            case 0x83: return addRegisterToRegister(SingleRegister.A, SingleRegister.E, false);
            case 0x84: return addRegisterToRegister(SingleRegister.A, SingleRegister.H, false);
            case 0x85: return addRegisterToRegister(SingleRegister.A, SingleRegister.L, false);
            case 0x86: return addMemoryToRegister(SingleRegister.A, DoubleRegister.HL, false);
            case 0xC6: return addImmediateByteToRegister(SingleRegister.A, false);

            case 0x8F: return addRegisterToRegister(SingleRegister.A, SingleRegister.A, true);
            case 0x88: return addRegisterToRegister(SingleRegister.A, SingleRegister.B, true);
            case 0x89: return addRegisterToRegister(SingleRegister.A, SingleRegister.C, true);
            case 0x8A: return addRegisterToRegister(SingleRegister.A, SingleRegister.D, true);
            case 0x8B: return addRegisterToRegister(SingleRegister.A, SingleRegister.E, true);
            case 0x8C: return addRegisterToRegister(SingleRegister.A, SingleRegister.H, true);
            case 0x8D: return addRegisterToRegister(SingleRegister.A, SingleRegister.L, true);
            case 0x8E: return addMemoryToRegister(SingleRegister.A, DoubleRegister.HL, true);
            case 0xCE: return addImmediateByteToRegister(SingleRegister.A, true);

            case 0x97: return subRegisterFromRegister(SingleRegister.A, SingleRegister.A, false);
            case 0x90: return subRegisterFromRegister(SingleRegister.A, SingleRegister.B, false);
            case 0x91: return subRegisterFromRegister(SingleRegister.A, SingleRegister.C, false);
            case 0x92: return subRegisterFromRegister(SingleRegister.A, SingleRegister.D, false);
            case 0x93: return subRegisterFromRegister(SingleRegister.A, SingleRegister.E, false);
            case 0x94: return subRegisterFromRegister(SingleRegister.A, SingleRegister.H, false);
            case 0x95: return subRegisterFromRegister(SingleRegister.A, SingleRegister.L, false);
            case 0x96: return subMemoryFromRegister(SingleRegister.A, DoubleRegister.HL, false);
            case 0xD6: return subImmediateByteFromRegister(SingleRegister.A, false);

            case 0x9F: return subRegisterFromRegister(SingleRegister.A, SingleRegister.A, true);
            case 0x98: return subRegisterFromRegister(SingleRegister.A, SingleRegister.B, true);
            case 0x99: return subRegisterFromRegister(SingleRegister.A, SingleRegister.C, true);
            case 0x9A: return subRegisterFromRegister(SingleRegister.A, SingleRegister.D, true);
            case 0x9B: return subRegisterFromRegister(SingleRegister.A, SingleRegister.E, true);
            case 0x9C: return subRegisterFromRegister(SingleRegister.A, SingleRegister.H, true);
            case 0x9D: return subRegisterFromRegister(SingleRegister.A, SingleRegister.L, true);
            case 0x9E: return subMemoryFromRegister(SingleRegister.A, DoubleRegister.HL, true);
            case 0xDE: return subImmediateByteFromRegister(SingleRegister.A, true);

            default:
                throw new IllegalArgumentException("Unknow opcode: " + opCode);
        }

    }


    /**
     * Loads immediate byte into register r,
     * increments the programCounter by 1
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int loadImmediateByteIntoRegister(SingleRegister r) {
        registerManager.set(r, getImmediateByte());
        return 8;
    }

    /**
     * Loads r1 with the value in r2
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int loadRegisterFromRegister(SingleRegister r1, SingleRegister r2) {
        registerManager.set(r1, registerManager.get(r2));
        return 4;
    }

    /**
     * Sets the value of d1 to be the same as the value of d2
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int loadRegisterFromRegister(DoubleRegister d1, DoubleRegister d2) {
        registerManager.set(d1, registerManager.get(d2));
        return 8;
    }

    /**
     * Writes the value of r to the address value in d
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int writeRegisterToMemory(DoubleRegister d, SingleRegister r) {
        mmu.writeData(registerManager.get(d), registerManager.get(r));
        return 8;
    }

    /**
     * Writes the value of r2 to address (0xFF00 + r2.val)
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int writeRegisterToMemory(SingleRegister r1, SingleRegister r2) {
        mmu.writeData(LOAD_SPECIAL_ADDRESS + registerManager.get(r1), registerManager.get(r2));
        return 8;
    }

    /**
     * Writes the value at address d to r
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int writeMemoryToRegister(SingleRegister r, DoubleRegister d) {
        registerManager.set(r, mmu.readData(registerManager.get(d)));
        return 8;
    }

    /**
     * Writes the value at address (0xFF00 + r2.val) to r1
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int writeMemoryToRegister(SingleRegister r1, SingleRegister r2) {
        registerManager.set(r1, mmu.readData(LOAD_SPECIAL_ADDRESS + registerManager.get(r2)));
        return 8;
    }

    /**
     * Decrements the value of d by 1
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int decrementRegister(DoubleRegister d) {
        registerManager.set(d, registerManager.get(d) - 1);
        return 0;
    }

    /**
     * Increments the value of d by 1
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int incrementRegister(DoubleRegister d) {
        registerManager.set(d, registerManager.get(d) + 1);
        return 0;
    }

    /**
     * Loads the immediate word into d
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int loadImmediateWordIntoRegister(DoubleRegister d) {
        registerManager.set(d, getImmediateWord());
        return 12;
    }

    /**
     * Pushes d's values onto the stack
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int pushRegisterToStack(DoubleRegister d) {
        pushToStack(registerManager.getHigh(d));
        pushToStack(registerManager.getLow(d));

        return 16;
    }

    /**
     * Pops stack word into d
     *
     * @return num of cpu cycles taken to perform op
     * */
    private int popStackIntoRegister(DoubleRegister d) {
        registerManager.setLow(d, popStack());
        registerManager.setHigh(d, popStack());

        return 12;
    }

    /**
     * Adds r1 and r2, puts the value in r1
     * Sets the flags carry, half-carry and zero
     *
     * @param addCarry if carry flag is set add result is incremented by 1
     * @return num of cpu cycles taken to perform op
     * */
    private int addRegisterToRegister(SingleRegister r1, SingleRegister r2, boolean addCarry) {
        registerManager.set(r1,
                performAdd(registerManager.get(r1), registerManager.get(r2), addCarry));
        return 4;
    }

    /**
     * Adds r and value at memory address d, puts the value in r
     * Sets the flags carry, half-carry and zero
     *
     * @param addCarry if carry flag is set add result is incremented by 1
     * @return num of cpu cycles taken to perform op
     * */
    private int addMemoryToRegister(SingleRegister r, DoubleRegister d, boolean addCarry) {
        registerManager.set(r,
                performAdd(registerManager.get(r), mmu.readData(registerManager.get(d)), addCarry));
        return 8;
    }

    /**
     * Adds r and the immediate byte, puts the result in r
     * Sets the flags carry, half-carry and zero
     *
     * @param addCarry if carry flag is set add result is incremented by 1
     * @return num of cpu cycles taken to perform op
     * */
    private int addImmediateByteToRegister(SingleRegister r, boolean addCarry) {
        registerManager.set(r,
                performAdd(registerManager.get(r), getImmediateByte(), addCarry));
        return 8;
    }

    /**
     * Subtracts r1 and r2, puts the result in r1
     * Sets the flags carry, half-carry, zero and operation flag
     *
     * @param subCarry if carry flag is set add result is decremented by 1
     * @return num of cpu cycles taken to perform op
     * */
    private int subRegisterFromRegister(SingleRegister r1, SingleRegister r2, boolean subCarry) {
        registerManager.set(r1,
                performSub(registerManager.get(r1), registerManager.get(r2), subCarry));

        return 4;
    }

    /**
     * Subs value at memory address d from r, puts the value in r
     * Sets the flags carry, half-carry, zero and operation flag
     *
     * @param addCarry if carry flag is set add result is incremented by 1
     * @return num of cpu cycles taken to perform op
     * */
    private int subMemoryFromRegister(SingleRegister r, DoubleRegister d, boolean addCarry) {
        registerManager.set(r,
                performSub(registerManager.get(r), mmu.readData(registerManager.get(d)), addCarry));
        return 8;
    }

    /**
     * Subs immediate byte from r, puts the result in r
     * Sets the flags carry, half-carry, zero and operation flag
     *
     * @param addCarry if carry flag is set add result is incremented by 1
     * @return num of cpu cycles taken to perform op
     * */
    private int subImmediateByteFromRegister(SingleRegister r, boolean addCarry) {
        registerManager.set(r,
                performSub(registerManager.get(r), getImmediateByte(), addCarry));
        return 8;
    }

    /**
     * Reads and returns the next 8 immediate bits from memory
     * */
    private int getImmediateByte() {
        return mmu.readData(++programCounter);
    }

    /**
     * Reads and returns the next 16 immediate bits from memory
     * */
    private int getImmediateWord() {
        int val1 = mmu.readData(++programCounter);
        int val2 = mmu.readData(++programCounter);
        return val2 << 8 | val1;
    }

    /**
     * Pushes data on to the stack, decrements the stack pointer
     * */
    private void pushToStack(int data) {
        int stackPointer = registerManager.get(DoubleRegister.SP);
        mmu.writeData(stackPointer - 1, data);
        registerManager.set(DoubleRegister.SP, stackPointer - 1);
    }

    /**
     * Performs addition on of val1 and val2,
     * if addCarry is true and the carry flag is set, result is incremented by 1
     *
     * Sets the carry, half-carry and zero flag
     * Note: This is used in ALU CPU methods as a helper
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
     * Note: This is used in ALU CPU methods as a helper
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
     * Pops an element of the stack, increments the stack pointer
     *
     * @return element popped
     * */
    private int popStack() {
        int stackPointer = registerManager.get(DoubleRegister.SP);
        registerManager.set(DoubleRegister.SP, stackPointer + 1);
        return mmu.readData(stackPointer);
    }
}
