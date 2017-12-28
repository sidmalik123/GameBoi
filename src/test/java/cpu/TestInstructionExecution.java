package cpu;

import cpu.alu.ALUImpl;
import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import cpu.instructions.InstructionExecutor;
import cpu.instructions.InstructionExecutorImpl;
import cpu.registers.Flag;
import cpu.registers.Register;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import interrupts.Interrupt;
import interrupts.InterruptManager;
import interrupts.InterruptManagerImpl;
import mmu.MMU;
import mmu.MockMMU;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests instruction executions
 *
 * Unfortunately because of time constraints these tests are not comprehensive,
 * they test what the instructions are supposed to do,
 * but they don't test that the instructions don't do anything more than that
 * */
public class TestInstructionExecution {

    private InstructionExecutor instructionExecutor;
    private Registers registers;
    private Clock clock;
    private MMU mmu;
    private InterruptManager interruptManager;
    private int instructionAddress = 0;

    public TestInstructionExecution() {
        clock = new ClockImpl();
        registers = new RegistersImpl(clock);
        mmu = new MockMMU(clock);
        interruptManager = new InterruptManagerImpl();
        instructionExecutor = new InstructionExecutorImpl(mmu, registers, clock,
                new ALUImpl(registers, clock), interruptManager);
    }

    @Before
    public void reset() {
        interruptManager.setInterruptsEnabled(false);
        registers.write(Register.PC, 0);
        registers.write(Register.AF, 0);
        registers.write(Register.BC, 0);
        registers.write(Register.DE, 0);
        registers.write(Register.HL, 0);
    }

    @Test
    public void testInstructions() {
        mmu.write(instructionAddress++, 0x00);
        executeInstructionAndTestPCAndClock(1, 4);

        mmu.write(instructionAddress++, 0x18);
        mmu.write(instructionAddress++, 0x03);
        executeInstructionAndTestPCAndClock(5, 12);

        mmu.write(6, 0x18);
        mmu.write(7, 0xFF);
        executeInstructionAndTestPCAndClock(1, 12);
    }

    @Test
    public void testPOP() {
        mmu.write(0x1000, 0x55);
        mmu.write(0x1001, 0x33);

        registers.write(Register.SP, 0x1000);
        mmu.write(0x00, 0xC1);
        instructionExecutor.executeInstruction();

        assert (registers.read(Register.BC) == 0x3355);
        assert (registers.read(Register.SP) == 0x1002);
    }

    @Test
    public void testJP() {
        mmu.write(0, 0xC3);
        mmu.write(1, 0x20);
        mmu.write(2, 0x15);

        instructionExecutor.executeInstruction();

        assert (registers.read(Register.PC) == 0x1520);
    }

    @Test
    public void testCall() {
        // call NZ
        registers.write(Register.PC, 0x1A47);
        registers.write(Register.SP, 0x3002);
        registers.setFlag(Flag.ZERO, false);

        mmu.write(0x1A47, 0xC4);
        mmu.write(0x1A48, 0x35);
        mmu.write(0x1A49, 0x21);

        instructionExecutor.executeInstruction();

        assert (mmu.read(0x3001) == 0x1A);
        assert (mmu.read(0x3000) == 0x4A);
        assert (registers.read(Register.SP) == 0x3000);
        assert (registers.read(Register.PC) == 0x2135);
    }

    @Test
    public void testRST() {
        registers.write(Register.SP, 0x3002);
        mmu.write(0, 0xC7);

        instructionExecutor.executeInstruction();

        assert (registers.read(Register.PC) == 0x00);
        assert (mmu.read(0x3001) == 0x00);
        assert (mmu.read(0x3000) == 0x01);
        assert (registers.read(Register.SP) == 0x3000);
    }

    @Test
    public void testRET() {
        registers.write(Register.PC, 0x3535);
        mmu.write(0x3535, 0xC9);
        registers.write(Register.SP, 0x2000);
        mmu.write(0x2000, 0xB5);
        mmu.write(0x2001, 0x18);

        instructionExecutor.executeInstruction();

        assert (registers.read(Register.SP) == 0x2002);
        assert (registers.read(Register.PC) == 0x18B5);
    }

    @Test
    public void testSLA() {
        mmu.write(0, 0xCB);
        mmu.write(0x01, 0x20);
        registers.write(Register.B, 0b10110001);

        instructionExecutor.executeInstruction();

        assert (registers.getFlag(Flag.CARRY));
        assert (registers.read(Register.B) == 0b01100010);
    }

    @Test
    public void testSRA() {
        mmu.write(0x00, 0xCB);
        mmu.write(0x01, 0x28);
        registers.write(Register.B, 0b10111000);

        instructionExecutor.executeInstruction();

        assert (!registers.getFlag(Flag.CARRY));
        assert (registers.read(Register.B) == 0b11011100);
    }

    @Test
    public void testSRL() {
        mmu.write(0x00, 0xCB);
        mmu.write(0x01, 0x38);
        registers.write(Register.B, 0b10001111);

        instructionExecutor.executeInstruction();

        assert (registers.getFlag(Flag.CARRY));
        assert (registers.read(Register.B) == 0b01000111);
    }

    @Test
    public void testPush() {
        mmu.write(0x00, 0xF5);
        registers.write(Register.AF, 0x2233);
        registers.write(Register.SP, 0x1007);

        instructionExecutor.executeInstruction();

        assert (mmu.read(0x1006) == 0x22);
        assert (mmu.read(0x1005) == 0x33);
        assert (registers.read(Register.SP) == 0x1005);
    }

    @Test
    public void testServiceInterrupts() {
        mmu.write(0x00, 0x00); // NOP
        interruptManager.write(InterruptManager.INTERRUPT_ENABLE_REGISTER, 0xFF); // enable all interrupts
        interruptManager.setInterruptsEnabled(true);
        interruptManager.requestInterrupt(Interrupt.VBLANK);

        instructionExecutor.executeInstruction();

        assert (registers.read(Register.PC) == Interrupt.VBLANK.getServiceAddress());
    }

    private void executeInstructionAndTestPCAndClock(int pcIncrement, int cycleIncrement) {
        int oldPC = registers.read(Register.PC);
        int oldCycles = clock.getTotalCycles();

        instructionExecutor.executeInstruction();

        assert (registers.read(Register.PC) - oldPC == pcIncrement);
        assert (clock.getTotalCycles() - oldCycles == cycleIncrement);
    }
}
