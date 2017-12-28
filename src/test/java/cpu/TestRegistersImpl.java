package cpu;

import core.BitUtils;
import cpu.clock.ClockImpl;
import cpu.registers.Flag;
import cpu.registers.Register;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import org.junit.Test;

public class TestRegistersImpl {

    private Registers registers = new RegistersImpl(new ClockImpl());

    @Test
    public void testWriteAndRead() {
        int val = 0;
        for (Register register : Register.values()) {
            registers.write(register, val);
            assert (registers.read(register) == val);
            ++val;
        }

        // test single registers join to become double registers
        registers.write(Register.A, 0x20);
        registers.write(Register.F, 0x03);
        assert (registers.read(Register.AF) == 0x2003);

        // test write to double register writes to single registers too
        registers.write(Register.AF, 0xFF30);
        assert (registers.read(Register.A) == 0xFF);
        assert (registers.read(Register.F) == 0x30);
    }

    @Test
    public void testOnlyByteIsWrittenToSingleRegisters() {
        int val = 0x789;
        registers.write(Register.A, val);
        assert (registers.read(Register.A) == (val & 0xFF));
    }

    @Test
    public void testOnlyWordIsWrittenToDoubleRegisters() {
        int val = 0x3030F;
        registers.write(Register.AF, val);
        assert (registers.read(Register.AF) == (val & 0xFFFF));

        registers.write(Register.SP, 0xFF200);
        assert (registers.read(Register.SP) == 0xF200);
    }

    @Test
    public void testFlags() {
        for (Flag flag : Flag.values()) {
            registers.setFlag(flag, true);
            assert (registers.getFlag(flag));
            assert (BitUtils.isBitSet(registers.read(Register.F), flag.getBitNum()));
        }
    }

    @Test
    public void testIncrementPC() {
        int pcBeforeIncrement = registers.read(Register.PC);
        registers.incrementPC();
        assert (registers.read(Register.PC) == pcBeforeIncrement + 1);
    }

    @Test
    public void testSignedByteAdditionToPC() {
        registers.write(Register.PC, 0);
        registers.addSignedByteToPC(0xFF);
        assert (registers.read(Register.PC) == 0xFFFF);

        registers.write(Register.PC, 0x3456);
        registers.addSignedByteToPC(0xFF);
        assert (registers.read(Register.PC) == 0x3455);

        registers.addSignedByteToPC(0x01);
        assert (registers.read(Register.PC) == 0x3456);
    }

    @Test
    public void testFlagBitNums() {
        assert (Flag.ZERO.getBitNum() == 7);
        assert (Flag.SUBTRACTION.getBitNum() == 6);
        assert (Flag.HALF_CARRY.getBitNum() == 5);
        assert (Flag.CARRY.getBitNum() == 4);
    }

    @Test
    public void testInitialRegisterValues() {
        assert (registers.read(Register.PC) == 0x100);
        assert (registers.read(Register.AF) == 0x01B0);
        assert (registers.read(Register.BC) == 0x0013);
        assert (registers.read(Register.DE) == 0x00D8);
        assert (registers.read(Register.HL) == 0x014D);
        assert (registers.read(Register.SP) == 0xFFFE);
    }
}
