package cpu;

import cpu.alu.ALU;
import cpu.alu.ALUImpl;
import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import cpu.registers.Flag;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import org.junit.Test;

/**
 * Tests ALU methods
 * */
public class TestALU {

    private ALU alu;
    private Registers registers;

    public TestALU() {
        Clock clock = new ClockImpl();
        registers = new RegistersImpl(clock);
        alu = new ALUImpl(registers, clock);
    }

    @Test
    public void testRotateLeft() {
        int bytee = 0b10001000;
        assert (alu.rotateByteLeft(bytee, false) == 0b00010001);
        assert (registers.getFlag(Flag.CARRY));

        bytee = 0b01110110;
        assert (alu.rotateByteLeft(bytee, true) == 0b11101101);
        assert (!registers.getFlag(Flag.CARRY));
    }

    @Test
    public void testWordAddition() {
        int word1 = 0x4242;
        int word2 = 0x1111;

        assert (alu.addWords(word1, word2) == 0x5353);
        assert (!registers.getFlag(Flag.SUBTRACTION));
        assert (!registers.getFlag(Flag.HALF_CARRY));
        assert (!registers.getFlag(Flag.CARRY));

        word2 = 0x0FFF;
        alu.addWords(word1, word2);
        assert (!registers.getFlag(Flag.CARRY));
        assert (registers.getFlag(Flag.HALF_CARRY));
    }

    @Test
    public void testRotateRight() {
        int bytee = 0b00010001;
        assert (alu.rotateByteRight(bytee, false) == 0b10001000);
        assert (registers.getFlag(Flag.CARRY));

        bytee = 0b11100001;
        registers.setFlag(Flag.CARRY, false);
        assert (alu.rotateByteRight(bytee, true) == 0b01110000);
        assert (registers.getFlag(Flag.CARRY));
    }

    @Test
    public void testComplementByte() {
        int bytee = 0b10110100;
        assert (alu.complementByte(bytee) == 0b01001011);
    }

    @Test
    public void testByteAddition() {
        int byte1 = 0x44;
        int byte2 = 0x11;

        assert (alu.addBytes(byte1, byte2, false) == 0x55);

        registers.setFlag(Flag.CARRY, true);
        assert (alu.addBytes(byte1, byte2, false) == 0x55);

        registers.setFlag(Flag.CARRY, true);
        assert (alu.addBytes(byte1, byte2, true) == 0x56);

        registers.setFlag(Flag.CARRY, false);

        assert (alu.addBytes(byte1, byte2, true) == 0x55);

        assert (alu.addBytes(250, 7, false) == 1);
        assert (registers.getFlag(Flag.CARRY));
    }

    @Test
    public void testByteSubtraction() {
        assert (alu.subBytes(0x29, 0x11, false) == 0x18);

        registers.setFlag(Flag.CARRY, true);
        assert (alu.subBytes(0x29, 0x11, true) == 0x17);
    }

    @Test
    public void testAnd() {
        assert (alu.andBytes(0b01111011, 0b11000011) == 0b01000011);
    }

    @Test
    public void testXOR() {
        assert (alu.xorBytes(0b10010110, 0b01011101) == 0b11001011);
    }

    @Test
    public void testOR() {
        assert (alu.orBytes(0b01000100, 0b00010010) == 0b01010110);
    }

    @Test
    public void testSwapNibbles() {
        assert (alu.swapNibbles(0x2F) == 0xF2);
    }

    @Test
    public void testTestBit() {
        registers.setFlag(Flag.ZERO, false);
        alu.testBit(0b10101010, 2);
        assert (registers.getFlag(Flag.ZERO));
    }

    @Test
    public void testResetBit() {
        assert (alu.resetBit(0b10110111, 1) == 0b10110101);
    }

    @Test
    public void testSetBit() {
        assert (alu.setBit(0b10110111, 6) == 0b11110111);
    }

    @Test
    public void testAddSignedByteToWord() {
        assert (alu.addSignedByteToWord(0x3502, 0xFF) == 0x3501);
    }
}
