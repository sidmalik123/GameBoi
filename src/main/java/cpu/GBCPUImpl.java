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

    private int PC; // set by the mmu on program load

    private int numCyclesPassed;

    public void run(String programLocation) {
        // Todo - process interrupts after every instruction
    }

    public int getFrequency() {
        return CPU_FREQUENCY;
    }

    private int readInstruction() {
        return mmu.readData(PC);
    }

    /**
     * Execute instruction
     *
     * @return num of clock cycles taken to execute the instruction
     * */
    private int executeInstruction(int instruction) { // Note - update PC at the end

        switch (instruction & 0xFF) {
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
            case 0xF9: return loadRegisterFromRegister(DoubleRegister.SP, DoubleRegister.HL);












        }

    }


    /**
     * Loads immediate byte into register r,
     * increments the PC by 1
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

    private void loadRegisterFromAddress(SingleRegister r1) {
        registerManager.set(r1, getImmediateAddressValue());
        numCyclesPassed += 16;
    }

    // loads the value in r1 to immediate address val
    private void loadAddressFromRegister(SingleRegister r1) {
        mmu.writeData(getImmediateAddressValue(), registerManager.get(r1));
        numCyclesPassed += 16;
    }

    // r1 = high bits of r2
    private void loadRegisterFromRegister(SingleRegister r1, DoubleRegister r2) {
        registerManager.set(r1, registerManager.getHigh(r2));
        numCyclesPassed += 8;
    }

    // r1.high = r2
    private void loadRegisterFromRegister(DoubleRegister r1, SingleRegister r2) {
        registerManager.set(r1, registerManager.get(r2));
        numCyclesPassed += 8;
    }

    // r1.high = immediate value
    private void loadRegisterFromImmediate8(DoubleRegister r1) {
        registerManager.setHigh(r1, mmu.readData(++PC));
        numCyclesPassed += 12;
    }

    private void loadRegisterFromImmediate16(DoubleRegister r1) {
        registerManager.set(r1, getImmediateWord());
    }

    // special loads
    private void loadAC() {
        registerManager.set(SingleRegister.A,
                mmu.readData(LOAD_SPECIAL_ADDRESS + registerManager.get(SingleRegister.C)));
        numCyclesPassed += 8;
    }

    private void loadCA() {
        mmu.writeData(LOAD_SPECIAL_ADDRESS + registerManager.get(SingleRegister.C),
                registerManager.get(SingleRegister.A));
        numCyclesPassed += 8;
    }

    private void LoadAHLD() {
        int HLAddress = registerManager.get(DoubleRegister.HL);
        registerManager.set(SingleRegister.A, mmu.readData(HLAddress));
        registerManager.set(DoubleRegister.HL, --HLAddress);
        numCyclesPassed += 8;
    }

    private void loadHLDA() {
        int HLAddress = registerManager.get(DoubleRegister.HL);
        mmu.writeData(HLAddress, registerManager.get(SingleRegister.A));
        registerManager.set(DoubleRegister.HL, --HLAddress);
        numCyclesPassed += 8;
    }

    private void loadAHLI() {
        int HLAddress = registerManager.get(DoubleRegister.HL);
        registerManager.set(SingleRegister.A, mmu.readData(HLAddress));
        registerManager.set(DoubleRegister.HL, ++HLAddress);
        numCyclesPassed += 8;
    }

    private void loadHLIA() {
        int HLAddress = registerManager.get(DoubleRegister.HL);
        mmu.writeData(HLAddress, registerManager.get(SingleRegister.A));
        registerManager.set(DoubleRegister.HL, ++HLAddress);
        numCyclesPassed += 8;
    }

    private void loadnA() {
        mmu.writeData(LOAD_SPECIAL_ADDRESS + getImmediateByte(),
                registerManager.get(SingleRegister.A));
        numCyclesPassed += 12;
    }

    private void loadAn() {
        registerManager.set(SingleRegister.A,
                mmu.readData(LOAD_SPECIAL_ADDRESS + getImmediateByte()));
        numCyclesPassed += 12;
    }

    private void loadSPHL() {
        SP = registerManager.get(DoubleRegister.HL);
        numCyclesPassed += 8;
    }

    private void loadHLSPn() {
        int immediateValue8 = getImmediateByte();
        registerManager.setZeroFlag(false);
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarryAdd(SP, immediateValue8));
        registerManager.setCarryFlag(BitUtils.isCarryAdd(SP, immediateValue8));

        registerManager.set(DoubleRegister.HL, (SP + immediateValue8) & 0xFFFF);

        numCyclesPassed += 12;
    }

    private void loadnnSP() {
        mmu.writeData(getImmediateAddressValue(), SP);
        numCyclesPassed += 20;
    }

    private void push(DoubleRegister r1) {
        --SP;
        mmu.writeData(SP, registerManager.getHigh(r1));
        --SP;
        mmu.writeData(SP, registerManager.getLow(r1));
        numCyclesPassed += 16;
    }

    private void pop(DoubleRegister r1) {
        registerManager.setHigh(r1, mmu.readData(SP));
        ++SP;
        registerManager.setLow(r1, mmu.readData(SP));
        ++SP;
        numCyclesPassed += 12;
    }


    /*
    *
    * ALU METHODS
    *
    * */
    private void add(SingleRegister r1, boolean addCarry) {
        int r1Val = registerManager.get(r1);

        doAdd(r1Val, addCarry);

        numCyclesPassed += 4;
    }

    private void addImmediate(boolean addCarry) {
        int immediateVal = getImmediateByte();

        doAdd(immediateVal, addCarry);

        numCyclesPassed += 8;
    }

    private void add(DoubleRegister r1, boolean addCarry) {
        int memoryVal = mmu.readData(registerManager.get(r1));

        doAdd(memoryVal, addCarry);

        numCyclesPassed += 8;
    }

    private void doAdd(int val, boolean addCarry) {
        int regAVal = registerManager.get(SingleRegister.A);

        setAddFlags(regAVal, val);

        int finalVal = regAVal + val;
        if (addCarry) finalVal += registerManager.getCarryFlag() ? 1 : 0;

        registerManager.set(SingleRegister.A, finalVal & 0xFF);
    }

    private void sub(SingleRegister r1, boolean subCarry) {
        int r1Val = registerManager.get(r1);

        doSub(r1Val, subCarry);

        numCyclesPassed += 4;
    }

    private void subImmediate(boolean subCarry) {
        int immediateVal = getImmediateByte();

        doSub(immediateVal, subCarry);

        numCyclesPassed += 8;
    }

    private void sub(DoubleRegister r1, boolean subCarry) {
        int memoryVal = mmu.readData(registerManager.get(r1));

        doSub(memoryVal, subCarry);

        numCyclesPassed += 8;
    }

    private void doSub(int val, boolean subCarry) {
        int regAVal = registerManager.get(SingleRegister.A);

        setSubFlags(regAVal, val);

        int finalVal = regAVal - val;
        if (subCarry) finalVal -= registerManager.getCarryFlag() ? 1 : 0;

        registerManager.set(SingleRegister.A, finalVal & 0xFF);
    }


    // set flags on doing sub op
    private void setSubFlags(int regAVal, int otherVal) {
        registerManager.setZeroFlag(BitUtils.isZeroSub(regAVal, otherVal));
        registerManager.setOperationFlag(true);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarrySub(regAVal, otherVal));
        registerManager.setCarryFlag(BitUtils.isCarrySub(regAVal, otherVal));
    }



    // sets flags on doing an add op
    private void setAddFlags(int regAVal, int otherVal) {
        registerManager.setZeroFlag(BitUtils.isZeroAdd(regAVal, otherVal));
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarryAdd(regAVal, otherVal));
        registerManager.setCarryFlag(BitUtils.isCarryAdd(regAVal, otherVal));
    }


    private void and(SingleRegister r1) {
        int r1Val = registerManager.get(r1);

        doAnd(r1Val);

        numCyclesPassed += 4;
    }

    private void and() {
        int immediateVal = getImmediateByte();

        doAnd(immediateVal);

        numCyclesPassed += 8;
    }

    private void and(DoubleRegister r1) {
        int memoryVal = mmu.readData(registerManager.get(r1));

        doAnd(memoryVal);

        numCyclesPassed += 4;
    }

    private void doAnd(int otherVal) {
        int regAVal = registerManager.get(SingleRegister.A);
        int result = (regAVal & otherVal) & 0xFF;

        registerManager.setZeroFlag(result == 0);
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(true);
        registerManager.setCarryFlag(false);

        registerManager.set(SingleRegister.A, result);
    }

    private void or(SingleRegister r1) {
        int r1Val = registerManager.get(r1);

        doOr(r1Val);

        numCyclesPassed += 4;
    }

    private void or(DoubleRegister r1) {
        int memoryVal = mmu.readData(registerManager.get(r1));

        doOr(memoryVal);

        numCyclesPassed += 8;
    }

    private void or() {
        int immediateVal = getImmediateByte();

        doOr(immediateVal);

        numCyclesPassed += 8;
    }

    private void doOr(int otherVal) {
        int regAVal = registerManager.get(SingleRegister.A);
        int result = (regAVal | otherVal) & 0xFF;

        registerManager.setZeroFlag(result == 0);
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(false);
        registerManager.setCarryFlag(false);

        registerManager.set(SingleRegister.A, result);
    }

    private void xor(SingleRegister r1) {
        int r1Val = registerManager.get(r1);

        doXor(r1Val);

        numCyclesPassed += 4;
    }

    private void xor(DoubleRegister r1) {
        int memoryVal = mmu.readData(registerManager.get(r1));

        doXor(memoryVal);

        numCyclesPassed += 8;
    }

    private void xor() {
        int immediateVal = getImmediateByte();

        doXor(immediateVal);

        numCyclesPassed += 8;
    }

    private void doXor(int otherVal) {
        int regAVal = registerManager.get(SingleRegister.A);
        int result = (regAVal ^ otherVal) & 0xFF;

        registerManager.setZeroFlag(result == 0);
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(false);
        registerManager.setCarryFlag(false);

        registerManager.set(SingleRegister.A, result);
    }

    private void cp(SingleRegister r1) {
        int r1Val = registerManager.get(r1);

        doCp(r1Val);

        numCyclesPassed += 4;
    }

    private void cp(DoubleRegister r1) {
        int memoryVal = mmu.readData(registerManager.get(r1));

        doCp(memoryVal);

        numCyclesPassed += 8;
    }

    private void cp() {
        int immediateVal = getImmediateByte();

        doCp(immediateVal);

        numCyclesPassed += 8;
    }

    private void doCp(int otherVal) {
        int regAVal = registerManager.get(SingleRegister.A);
        int result = (regAVal - otherVal) & 0xFF;

        registerManager.setZeroFlag(result == 0);
        registerManager.setOperationFlag(true);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarrySub(regAVal, otherVal));
        registerManager.setCarryFlag(BitUtils.isCarrySub(regAVal, otherVal));
    }

    private void inc(SingleRegister r1) {
        int r1Val = registerManager.get(r1);
        int result = (r1Val + 1) & 0xFF;

        registerManager.setZeroFlag(result == 0);
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarryAdd(r1Val, 1));

        registerManager.set(r1, result);

        numCyclesPassed += 4;
    }

    private void dec(SingleRegister r1) {
        int r1Val = registerManager.get(r1);
        int result = (r1Val - 1) & 0xFF;

        registerManager.setZeroFlag(result == 0);
        registerManager.setOperationFlag(true);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarrySub(r1Val, 1));

        registerManager.set(r1, result);

        numCyclesPassed += 4;
    }

    private int getImmediateAddressValue() {
        int address = getImmediateWord();
        return mmu.readData(address);
    }

    /**
     * Reads and returns the next 8 immediate bits from memory
     * */
    private int getImmediateByte() {
        return mmu.readData(++PC);
    }

    /**
     * Reads and returns the next 16 immediate bits from memory
     * */
    private int getImmediateWord() {
        int val1 = mmu.readData(++PC);
        int val2 = mmu.readData(++PC);
        return val2 << 8 | val1;
    }
}
