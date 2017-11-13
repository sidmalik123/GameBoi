package cpu;

import core.BitUtils;
import mmu.MMU;

public class GBCPU extends AbstractCPU {

    private static final int LOAD_SPECIAL_ADDRESS = 0xFF00;

    private MMU mmu;

    private GBRegisterManager registerManager;

    private int PC; // set by the mmu on program load
    private int SP;

    private int numCyclesPassed;

    public void run(String programLocation) {

    }

    protected int readInstruction() {
        return mmu.readData(PC);
    }

    protected void executeInstruction(int instruction) {

        switch (instruction & 0xFF) {
            // Loads


        }

    }

    /*
    *
    * LOAD METHODS
    *
    * */

    // loads immediate/address value into register
    private void loadRegisterFromImmediate(SingleRegister r1) {
        registerManager.set(r1, getImmediateValue8());
        numCyclesPassed += 8;
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

    // r2 = r1
    private void loadRegisterFromRegister(SingleRegister r1, SingleRegister r2) {
        registerManager.set(r1, registerManager.get(r2));
        numCyclesPassed += 4;
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
        registerManager.set(r1, getImmediateValue16());
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
        mmu.writeData(LOAD_SPECIAL_ADDRESS + getImmediateValue8(),
                registerManager.get(SingleRegister.A));
        numCyclesPassed += 12;
    }

    private void loadAn() {
        registerManager.set(SingleRegister.A,
                mmu.readData(LOAD_SPECIAL_ADDRESS + getImmediateValue8()));
        numCyclesPassed += 12;
    }

    private void loadSPHL() {
        SP = registerManager.get(DoubleRegister.HL);
        numCyclesPassed += 8;
    }

    private void loadHLSPn() {
        int immediateValue8 = getImmediateValue8();
        registerManager.setZeroFlag(false);
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarry(SP, immediateValue8));
        registerManager.setCarryFlag(BitUtils.isCarry(SP, immediateValue8));

        registerManager.set(DoubleRegister.HL, SP + immediateValue8);

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
        int immediateVal = getImmediateValue8();

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

        registerManager.set(SingleRegister.A, finalVal);
    }



    // sets flags on doing an add op
    private void setAddFlags(int regAVal, int otherVal) {
        registerManager.setZeroFlag(BitUtils.isZero(regAVal, otherVal));
        registerManager.setOperationFlag(false);
        registerManager.setHalfCarryFlag(BitUtils.isHalfCarry(regAVal, otherVal));
        registerManager.setCarryFlag(BitUtils.isCarry(regAVal, otherVal));
    }

    private int getImmediateAddressValue() {
        int address = getImmediateValue16();
        return mmu.readData(address);
    }

    private int getImmediateValue8() {
        return mmu.readData(++PC);
    }

    private int getImmediateValue16() {
        int val1 = mmu.readData(++PC);
        int val2 = mmu.readData(++PC);
        return val2 << 8 | val1;
    }
}
