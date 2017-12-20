package cpu;

import cpu.alu.ALU;
import cpu.clock.ClockObserver;
import cpu.clock.ClockSubject;
import cpu.registers.Registers;
import mmu.MMU;
import mmu.MemoryMicroOp;


public class CPUImpl extends ClockSubject implements CPU {

    private int PC;

    private DataBus dataBus1, dataBus2;

    private MMU mmu;

    private Registers registers;

    private ALU alu;

    public CPUImpl(MMU mmu, Registers registers, ALU alu) {
        this.mmu = mmu;
        this.registers = registers;
        this.alu = alu;
    }

    @Override
    public void run() {
        // instruction fetch
        int instruction = fetchInstruction();

        // instruction decode - set control bits

        // register reads

        // memory read/write

        // alu ops

        // register writes


    }

    /**
     * Returns the next instruction to be executed
     * */
    private int fetchInstruction() {
        int instruction = mmu.read(PC);
        ++PC;
        notifyClockIncrement(MemoryMicroOp.READ_PC.getNumCycles());
        return instruction;
    }

    /**
     * Notifies all of its observers to handle clock increment
     * */
    @Override
    protected void notifyClockIncrement(int increment) {
        for (ClockObserver observer : this.observers) {
            observer.handleClockIncrement(increment);
        }
    }
}
