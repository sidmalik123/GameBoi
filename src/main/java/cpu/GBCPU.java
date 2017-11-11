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
                LDrn(RegisterType.B);
            case 0x0E:
                LDrn(RegisterType.C);
            case 0x16:
                LDrn(RegisterType.E);
            case 0x1E:
                LDrn(RegisterType.E);
            case 0x26:
                LDrn(RegisterType.H);
            case 0x2E:
                LDrn(RegisterType.L);

            // LDrrs
            case 0x7F:

        }

    }


    private void LDrn(RegisterType registerType) {
        registerManager.set(registerType, mmu.readData(++PC));
        numCyclesPassed += 4;
    }

}
