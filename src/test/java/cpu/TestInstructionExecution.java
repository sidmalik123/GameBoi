package cpu;

import cpu.alu.ALUImpl;
import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import cpu.registers.Flag;
import cpu.registers.Register;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import interrupts.Interrupt;
import interrupts.InterruptManager;
import interrupts.InterruptManagerImpl;
import mmu.MMU;
import mmu.MockMMU;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests instruction executions
 *
 * Unfortunately because of time constraints these tests are not comprehensive,
 * they test what the instructions are supposed to do,
 * but they don't test that the instructions don't do anything more than that
 * */
public class TestInstructionExecution {

    private CPU CPU;
    private Registers registers;
    private Clock clock;
    private MMU mmu;
    private InterruptManager interruptManager;
    private int instructionAddress = 0;

    public TestInstructionExecution() {
        clock = new ClockImpl();
        registers = new RegistersImpl(clock);
        mmu = new MockMMU();
        interruptManager = new InterruptManagerImpl(mmu);
        CPU = new CPUImpl(new MemoryAccessorImpl(mmu, clock), registers, clock,
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
        CPU.executeInstruction();

        assert (registers.read(Register.BC) == 0x3355);
        assert (registers.read(Register.SP) == 0x1002);
    }

    @Test
    public void testJP() {
        mmu.write(0, 0xC3);
        mmu.write(1, 0x20);
        mmu.write(2, 0x15);

        CPU.executeInstruction();

        assert (registers.read(Register.PC) == 0x1520);
    }

    @Test
    public void testLoadImmediateWordIntoRegister() {
        mmu.write(0, 0x01);
        mmu.write(1, 0x33);
        mmu.write(2, 0xFF);

        CPU.executeInstruction();

        assert (registers.read(Register.BC) == 0xFF33);
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

        CPU.executeInstruction();

        assert (mmu.read(0x3001) == 0x1A);
        assert (mmu.read(0x3000) == 0x4A);
        assert (registers.read(Register.SP) == 0x3000);
        assert (registers.read(Register.PC) == 0x2135);
    }

    @Test
    public void testRST() {
        registers.write(Register.SP, 0x3002);
        mmu.write(0, 0xC7);

        CPU.executeInstruction();

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

        CPU.executeInstruction();

        assert (registers.read(Register.SP) == 0x2002);
        assert (registers.read(Register.PC) == 0x18B5);
    }

    @Test
    public void testSLA() {
        mmu.write(0, 0xCB);
        mmu.write(0x01, 0x20);
        registers.write(Register.B, 0b10110001);

        CPU.executeInstruction();

        assert (registers.getFlag(Flag.CARRY));
        assert (registers.read(Register.B) == 0b01100010);
    }

    @Test
    public void testSRA() {
        mmu.write(0x00, 0xCB);
        mmu.write(0x01, 0x28);
        registers.write(Register.B, 0b10111000);

        CPU.executeInstruction();

        assert (!registers.getFlag(Flag.CARRY));
        assert (registers.read(Register.B) == 0b11011100);
    }

    @Test
    public void testSRL() {
        mmu.write(0x00, 0xCB);
        mmu.write(0x01, 0x38);
        registers.write(Register.B, 0b10001111);

        CPU.executeInstruction();

        assert (registers.getFlag(Flag.CARRY));
        assert (registers.read(Register.B) == 0b01000111);
    }

    @Test
    public void testPush() {
        mmu.write(0x00, 0xF5);
        registers.write(Register.AF, 0x2230);
        registers.write(Register.SP, 0x1007);

        CPU.executeInstruction();

        assert (mmu.read(0x1006) == 0x22);
        assert (mmu.read(0x1005) == 0x30);
        assert (registers.read(Register.SP) == 0x1005);
    }

    @Test
    public void testServiceInterrupts() {
        registers.write(Register.SP, 0xFFFE);
        mmu.write(0x00, 0x00); // NOP
        mmu.write(MMU.INTERRUPT_ENABLE_REGISTER, 0xFF); // enable all interrupts
        interruptManager.setInterruptsEnabled(true);
        interruptManager.requestInterrupt(Interrupt.VBLANK);

        CPU.executeInstruction();

        assert (registers.read(Register.PC) == Interrupt.VBLANK.getServiceAddress());
        interruptManager.setInterruptsEnabled(true);
        assert (interruptManager.getPendingInterrupt() == null); // interrupt was reset
    }

    @Test
    public void testLoadWordIntoMemory() {
        registers.write(Register.SP, 0xF347);
        mmu.write(0, 0x08);
        mmu.write(1, 0xAB);
        mmu.write(2, 0xAF);

        CPU.executeInstruction();

        assert (mmu.read(0xAFAB) == 0x47);
        assert (mmu.read(0xAFAB + 1) == 0xF3);
        assert (registers.read(Register.PC) == 3);
    }

    @Test
    public void testJR() {
        registers.write(Register.PC, 0x480);
        mmu.write(0x480, 0x18);
        mmu.write(0x481, 0xFA);

        CPU.executeInstruction();

        assertEquals(registers.read(Register.PC), 0x47C);

        registers.write(Register.PC, 0x480);
        mmu.write(0x480, 0x18);
        mmu.write(0x481, 0x03);

        CPU.executeInstruction();

        assertEquals(registers.read(Register.PC), 0x485);

        registers.write(Register.PC, 0);
        registers.setFlag(Flag.ZERO, true);
        mmu.write(0, 0x20);
        mmu.write(1, 0xfB);
        mmu.write(2, 0x14);

        CPU.executeInstruction();
        assertEquals(registers.read(Register.PC), 2);

    }

    @Test
    public void testCompare() {
        registers.write(Register.A, 0x3B);
        mmu.write(0, 0xFE);
        mmu.write(1, 0x3B);

        CPU.executeInstruction();
        assertTrue(registers.getFlag(Flag.ZERO));
    }

    @Test
    public void testHalt() {
        mmu.write(0, 0x76); // halt instruction

        CPU.executeInstruction();
        assertEquals(registers.read(Register.PC), 1);

        int oldCycles = clock.getTotalCycles();
        CPU.executeInstruction();
        assertEquals(registers.read(Register.PC), 1); // pc stays the same
        assertEquals(clock.getTotalCycles(), oldCycles + 4); // Nop executed
    }

    @Test
    public void testStop() {
        mmu.write(0, 0x10); // stop instruction

        CPU.executeInstruction();
        assertEquals(registers.read(Register.PC), 1);

        int oldCycles = clock.getTotalCycles();
        CPU.executeInstruction();
        assertEquals(registers.read(Register.PC), 1); // pc stays the same
        assertEquals(clock.getTotalCycles(), oldCycles + 4); // Nop executed
    }

    private void executeInstructionAndTestPCAndClock(int pcIncrement, int cycleIncrement) {
        int oldPC = registers.read(Register.PC);
        int oldCycles = clock.getTotalCycles();

        CPU.executeInstruction();

        assert (registers.read(Register.PC) - oldPC == pcIncrement);
        assert (clock.getTotalCycles() - oldCycles == cycleIncrement);
    }
}
