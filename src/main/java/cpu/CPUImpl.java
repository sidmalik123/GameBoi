package cpu;

import afu.org.checkerframework.checker.units.qual.C;
import core.BitUtils;
import core.exceptions.UnknownMicroOpException;
import cpu.alu.ALU;
import cpu.alu.ALUMicroOp;
import cpu.microops.MicroOp;
import cpu.microops.MicroOpsGenerator;
import cpu.registers.RegisterMicroOp;
import cpu.registers.Registers;
import mmu.MMU;
import mmu.MemoryMicroOp;


public class CPUImpl extends ClockSubject implements CPU {

    private int PC;

    private int secondLastOpResult;

    private int lastOpResult;

    private MMU mmu;

    private Registers registers;

    private ALU alu;

    private MicroOpsGenerator microOpsGenerator;

    public CPUImpl(MMU mmu, MicroOpsGenerator microOpsGenerator, Registers registers, ALU alu) {
        this.mmu = mmu;
        this.microOpsGenerator = microOpsGenerator;
        this.registers = registers;
        this.alu = alu;
    }

    @Override
    public void run() {
        int instruction = getNextInstruction();

        for (MicroOp microOp : microOpsGenerator.generate(instruction)) {
            if (microOp instanceof MemoryMicroOp) {
                handleMemoryMicroOp((MemoryMicroOp) microOp);
            } else if (microOp instanceof ALUMicroOp) {
                handleALUMicroOp((ALUMicroOp) microOp);
            } else if (microOp instanceof RegisterMicroOp) {
                handleRegisterMicroOp((RegisterMicroOp) microOp);
            } else {
                throw new UnknownMicroOpException("MicroOp " + microOp.getClass().getName() + " not handled");
            }

            // increment clock by number of CCs it took to execute microOp
            notifyClockIncrement(microOp.getNumCycles());
        }

    }

    /**
     * Handles Register MicroOps
     * */
    private void handleRegisterMicroOp(RegisterMicroOp microOp) {
        switch (microOp) {
            case READ_A:
                setLastOpResult(registers.read(Registers.Register.A));
            case READ_B:
                setLastOpResult(registers.read(Registers.Register.B));
            case READ_C:
                setLastOpResult(registers.read(Registers.Register.C));
            case READ_D:
                setLastOpResult(registers.read(Registers.Register.D));
            case READ_E:
                setLastOpResult(registers.read(Registers.Register.E));
            case READ_F:
                setLastOpResult(registers.read(Registers.Register.F));
            case READ_H:
                setLastOpResult(registers.read(Registers.Register.H));
            case READ_L:
                setLastOpResult(registers.read(Registers.Register.L));
            case READ_AF:
                setLastOpResult(registers.read(Registers.Register.AF));
            case READ_BC:
                setLastOpResult(registers.read(Registers.Register.BC));
            case WRITE_DE:
                setLastOpResult(registers.read(Registers.Register.DE));
            case READ_HL:
                setLastOpResult(registers.read(Registers.Register.HL));
            case READ_SP:
                setLastOpResult(registers.read(Registers.Register.SP));

            // Register writes - assumes data to write is lastOpResult
            case WRITE_A:
                registers.write(Registers.Register.A, lastOpResult);
            case WRITE_B:
                registers.write(Registers.Register.B, lastOpResult);
            case WRITE_C:
                registers.write(Registers.Register.C, lastOpResult);
            case WRITE_D:
                registers.write(Registers.Register.D, lastOpResult);
            case WRITE_E:
                registers.write(Registers.Register.E, lastOpResult);
            case WRITE_F:
                registers.write(Registers.Register.F, lastOpResult);
            case WRITE_H:
                registers.write(Registers.Register.H, lastOpResult);
            case WRITE_L:
                registers.write(Registers.Register.L, lastOpResult);
            case WRITE_AF:
                registers.write(Registers.Register.AF, lastOpResult);
            case WRITE_BC:
                registers.write(Registers.Register.BC, lastOpResult);
            case READ_DE:
                registers.write(Registers.Register.DE, lastOpResult);
            case WRITE_HL:
                registers.write(Registers.Register.HL, lastOpResult);
            case WRITE_SP:
                registers.write(Registers.Register.SP, lastOpResult);
        }
    }

    /**
     * Handles ALU MicroOps
     * */
    private void handleALUMicroOp(ALUMicroOp microOp) {
        switch (microOp) {
            case JOIN_BYTES:
                setLastOpResult(alu.joinBytes(lastOpResult & 0xFF, secondLastOpResult & 0xFF));
        }
    }

    /**
     * Handles Memory MicroOps
     * */
    private void handleMemoryMicroOp(MemoryMicroOp microOp) {
        switch (microOp) {
            case READ_PC:
                setLastOpResult(mmu.read(PC));
                ++PC;
            case READ_ADDRESS:
                // assumes lastOpResult has the address to read from
                setLastOpResult(mmu.read(lastOpResult));
            case WRITE_ADDRESS:
                /* assumes secondLastOpResult holds the address,
                lastOpResult holds the data to write */
                mmu.write(secondLastOpResult, lastOpResult);

        }
    }

    /**
     * Returns the next instruction to be executed
     * */
    private int getNextInstruction() {
        int instruction = mmu.read(PC);
        ++PC;
        notifyClockIncrement(MemoryMicroOp.READ_PC.getNumCycles());
        return instruction;
    }

    /**
     * Sets result to be the result of the last operation
     * */
    private void setLastOpResult(int result) {
        secondLastOpResult = lastOpResult;
        lastOpResult = result;
    }

    /**
     * Notifies all of its observers to handle clock increment
     * */
    @Override
    void notifyClockIncrement(int increment) {
        for (ClockObserver observer : this.observers) {
            observer.handleClockIncrement(increment);
        }
    }
}
