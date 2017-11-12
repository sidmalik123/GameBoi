package cpu;

import mmu.MMU;

public class GBCPU extends AbstractCPU {

    private static final int LOAD_SPECIAL_ADDRESS = 0xFF00;

    private MMU mmu;

    private GBRegisterManager registerManager;

    private int PC; // set by the mmu on program load

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
        registerManager.set(r1, getImmediateValue());
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
    private void loadRegisterFromImmedaite(DoubleRegister r1) {
        registerManager.setHigh(r1, mmu.readData(++PC));
        numCyclesPassed += 12;
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
        mmu.writeData(LOAD_SPECIAL_ADDRESS + getImmediateValue(),
                registerManager.get(SingleRegister.A));
        numCyclesPassed += 12;
    }

    private void loadAn() {
        registerManager.set(SingleRegister.A,
                mmu.readData(LOAD_SPECIAL_ADDRESS + getImmediateValue()));
        numCyclesPassed += 12;
    }

    private int getImmediateAddressValue() {
        int add1 = mmu.readData(++PC);
        int add2 = mmu.readData(++PC);
        return add2 << 8 | add1;
    }

    private int getImmediateValue() {
        return mmu.readData(++PC);
    }
}
