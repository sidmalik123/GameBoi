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
            // LDrns
            case 0x06:
                LDrn(SingleRegister.B);
            case 0x0E:
                LDrn(SingleRegister.C);
            case 0x16:
                LDrn(SingleRegister.E);
            case 0x1E:
                LDrn(SingleRegister.E);
            case 0x26:
                LDrn(SingleRegister.H);
            case 0x2E:
                LDrn(SingleRegister.L);

            // LDrrs
            case 0x7F:

        }

    }

    // Load n(immediate value) into r
    private void LDrn(SingleRegister singleRegister) {
        registerManager.set(singleRegister, mmu.readData(++PC));
        numCyclesPassed += 8;
    }

    // r1 = r2
    private void LDr1r2(SingleRegister singleRegister1, SingleRegister singleRegister2) {
        registerManager.set(singleRegister1, registerManager.get(singleRegister2));
        numCyclesPassed += 4;
    }
}
