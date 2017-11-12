package cpu;

import mmu.MMU;

public class GBCPU extends AbstractCPU {

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
                load(SingleRegister.B);
            case 0x0E:
                load(SingleRegister.C);
            case 0x16:
                load(SingleRegister.E);
            case 0x1E:
                load(SingleRegister.E);
            case 0x26:
                load(SingleRegister.H);
            case 0x2E:
                load(SingleRegister.L);

            // LDrrs
            case 0x7F:

        }

    }

    // Load n(immediate value) into r
//    private void LDrn(SingleRegister singleRegister) {
//        registerManager.set(singleRegister, mmu.readData(++PC));
//        numCyclesPassed += 8;
//    }
//
//    // r1 = r2
//    private void LDr1r2(SingleRegister singleRegister1, SingleRegister singleRegister2) {
//        registerManager.set(singleRegister1, registerManager.get(singleRegister2));
//        numCyclesPassed += 4;
//    }


    // loads immediate value into register
    private void load(SingleRegister singleRegister) {
        registerManager.set(singleRegister, mmu.readData(++PC));
        numCyclesPassed += 8;
    }

    // r2 = r1
    private void load(SingleRegister r1, SingleRegister r2) {
        registerManager.set(r1, registerManager.get(r2));
        numCyclesPassed += 4;
    }

    private void load(SingleRegister r1, DoubleRegister r2) {

    }
}
