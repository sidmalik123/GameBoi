package cpu;

import mmu.MMU;

public class GBCPU extends AbstractCPU {

    private enum ValueType {IMMEDIATE, ADDRESS};

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
            case 0x06:
                load(SingleRegister.B, ValueType.IMMEDIATE);
            case 0x0E:
                load(SingleRegister.C, ValueType.IMMEDIATE);
            case 0x16:
                load(SingleRegister.E, ValueType.IMMEDIATE);
            case 0x1E:
                load(SingleRegister.E, ValueType.IMMEDIATE);
            case 0x26:
                load(SingleRegister.H, ValueType.IMMEDIATE);
            case 0x2E:
                load(SingleRegister.L, ValueType.IMMEDIATE);
            case 0x7F:

        }

    }

    /*
    *
    * LOAD METHODS
    *
    * */

    // loads immediate/address value into register
    private void load(SingleRegister r1, ValueType valueType) {
        switch (valueType) {
            case IMMEDIATE:
                registerManager.set(r1, mmu.readData(++PC));
                numCyclesPassed += 8;
            case ADDRESS:
                registerManager.set(r1, getImmediateAddressValue());
                numCyclesPassed += 16;
        }

    }

    // loads the value in r1 to immediate address val
    private void load(SingleRegister r1) {
        mmu.writeData(getImmediateAddressValue(), registerManager.get(r1));
        numCyclesPassed += 16;
    }

    // r2 = r1
    private void load(SingleRegister r1, SingleRegister r2) {
        registerManager.set(r1, registerManager.get(r2));
        numCyclesPassed += 4;
    }

    // r1 = high bits of r2
    private void load(SingleRegister r1, DoubleRegister r2) {
        registerManager.set(r1, registerManager.getHigh(r2));
        numCyclesPassed += 8;
    }

    // r1.high = r2
    private void load(DoubleRegister r1, SingleRegister r2) {
        registerManager.set(r1, registerManager.get(r2));
        numCyclesPassed += 8;
    }

    // r1.high = immediate value
    private void load(DoubleRegister r1) {
        registerManager.set(r1, mmu.readData(++PC));
        numCyclesPassed += 12;
    }

    // special loads

    private int getImmediateAddressValue() {
        int add1 = mmu.readData(++PC);
        int add2 = mmu.readData(++PC);
        return add2 << 8 | add1;
    }
}
