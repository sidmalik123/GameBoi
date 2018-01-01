package cpu;

import cpu.alu.ALU;
import cpu.alu.ALUImpl;
import cpu.clock.Clock;
import cpu.clock.ClockImpl;
import cpu.registers.Flag;
import cpu.registers.Registers;
import cpu.registers.RegistersImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests ALU methods
 * */
public class TestALU {

    private ALU alu;
    private Registers registers;
    private Clock clock;

    @Before
    public void reset() {
        clock = new ClockImpl();
        registers = new RegistersImpl(clock);
        alu = new ALUImpl(registers, clock);
    }

    @Test
    public void testRotateLeft() {
        int bytee = 0b10001000;
        assert (alu.rotateByteLeft(bytee, false) == 0b00010001);
        assert (registers.getFlag(Flag.CARRY));
        assertFalse(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));

        bytee = 0b01110110;
        assert (alu.rotateByteLeft(bytee, true) == 0b11101101);
        assert (!registers.getFlag(Flag.CARRY));
        assertFalse(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));

        // through-carry, carry not set before
        registers.setFlag(Flag.CARRY, false);
        assertEquals(alu.rotateByteLeft(0b10101010, true), 0b01010100);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.CARRY));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));

        // through-carry, carry set before
        registers.setFlag(Flag.CARRY, true);
        assertEquals(alu.rotateByteLeft(0b10101010, true), 0b01010101);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.CARRY));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));

        // not through carry, bit 7 set
        assertEquals(alu.rotateByteLeft(0b10101010, false), 0b01010101);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.CARRY));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));

        // not through carry, bit 7 not set
        assertEquals(alu.rotateByteLeft(0b00101010, false), 0b01010100);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.CARRY));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));

        // zero case
        registers.setFlag(Flag.CARRY, false);
        assertEquals(alu.rotateByteLeft(0b10000000, true), 0);
        assertTrue(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.CARRY));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));
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

        // rotate right through carry
        registers.setFlag(Flag.CARRY, false);
        assertEquals(alu.rotateByteRight(0b00000001, true), 0);
        assertTrue(registers.getFlag(Flag.CARRY));
        assertTrue(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));

        // rotate right through carry
        registers.setFlag(Flag.CARRY, true);
        assertEquals(alu.rotateByteRight(0b00000001, true), 0b10000000);
        assertTrue(registers.getFlag(Flag.CARRY));
        assertTrue(!registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));
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

        alu.testBit(0x00, 0);
        assertTrue(registers.getFlag(Flag.ZERO));
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

    @Test
    public void testShiftLeft() {
        int bytee = 0b10110001;
        assertEquals(alu.shiftByteLeft(bytee), 0b01100010);
        assertTrue(registers.getFlag(Flag.CARRY));

        assertEquals(alu.shiftByteLeft(0b10000000), 0);
        assertTrue(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.CARRY));

        assertEquals(alu.shiftByteLeft(0b01010101), 0b10101010);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.CARRY));
    }

    @Test
    public void testIncWord() {
        int oldCycles = clock.getTotalCycles();
        assert (alu.incWord(0xFF32) == 0xFF33);
        assert (clock.getTotalCycles() == oldCycles + 4);

        assert (alu.incWord(0xFFFF) == 0x0000);
    }

    @Test
    public void testIncByte() {
        boolean oldCarryFlagVal = registers.getFlag(Flag.CARRY);
        assertEquals(alu.incByte(0x30), 0x31);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));
        assertEquals(registers.getFlag(Flag.CARRY), oldCarryFlagVal);

        assertEquals(alu.incByte(0x3F), 0x40);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertTrue(registers.getFlag(Flag.HALF_CARRY));
        assertEquals(registers.getFlag(Flag.CARRY), oldCarryFlagVal);

        assertEquals(alu.incByte(0xFF), 0x00);
        assertTrue(registers.getFlag(Flag.ZERO));
        assertFalse(registers.getFlag(Flag.SUBTRACTION));
        assertTrue(registers.getFlag(Flag.HALF_CARRY));
        assertEquals(registers.getFlag(Flag.CARRY), oldCarryFlagVal);
    }

    @Test
    public void testDecByte() {
        boolean oldCarryFlagVal = registers.getFlag(Flag.CARRY);
        assertEquals(alu.decByte(0x43), 0x42);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));
        assertEquals(registers.getFlag(Flag.CARRY), oldCarryFlagVal);

        assertEquals(alu.decByte(0x40), 0x3F);
        assertFalse(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.SUBTRACTION));
        assertTrue(registers.getFlag(Flag.HALF_CARRY));
        assertEquals(registers.getFlag(Flag.CARRY), oldCarryFlagVal);

        assertEquals(alu.decByte(0x01), 0x00);
        assertTrue(registers.getFlag(Flag.ZERO));
        assertTrue(registers.getFlag(Flag.SUBTRACTION));
        assertFalse(registers.getFlag(Flag.HALF_CARRY));
        assertEquals(registers.getFlag(Flag.CARRY), oldCarryFlagVal);
    }

    @Test
    public void testWordDec() {
        int oldClockCycles = clock.getTotalCycles();
        assertEquals(alu.decWord(0xFF32), 0xFF31);
        assertEquals(clock.getTotalCycles(), oldClockCycles + 4);

        assertEquals(alu.decWord(0x0000), 0xFFFF);
        assertEquals(alu.decWord(0x3F30), 0x3F2F);

    }
}
