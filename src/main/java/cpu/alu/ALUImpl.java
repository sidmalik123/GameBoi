package cpu.alu;

import com.google.inject.Inject;
import core.BitUtils;
import cpu.clock.Clock;
import cpu.registers.Flag;
import cpu.registers.Registers;

/**
 * Concrete class for ALU
 * */
public class ALUImpl implements ALU {

    private Registers registers;
    private Clock clock;

    @Inject
    public ALUImpl(Registers registers, Clock clock) {
        this.clock = clock;
        this.registers = registers;
    }

    @Override
    /**
     * - - - -
     * */
    public int incWord(int word) {
        clock.addCycles(4);
        return ++word & 0xFFFF;
    }

    @Override
    /**
     * Z 0 H -
     * */
    public int incByte(int bytee) {
        int result = (bytee + 1) & 0xFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryByteAddition(bytee, 1, 0));

        return result;
    }

    @Override
    /**
     * Z 1 H -
     * */
    public int decByte(int bytee) {
        int result = (bytee-1) & 0xFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, true);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryByteSubtraction(bytee, 1));

        return result;
    }

    /**
     * Z - - C
     * */
    public int rotateByteLeft(int bytee, boolean throughCarry) {
        int result = (bytee << 1) & 0xFF;

        if (throughCarry) { // old carry value to bit 0
            if (registers.getFlag(Flag.CARRY)) result = BitUtils.setBit(result, 0);
        } else { // old bit 7 to bit 0
            if (BitUtils.isBitSet(bytee, 7)) result = BitUtils.setBit(result, 0);
        }

        registers.setFlag(Flag.CARRY, BitUtils.isBitSet(bytee, 7)); // old bit 7 to carry flag
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
        return result;
    }

    @Override
    public int addWords(int word1, int word2) {
        int result = (word1 + word2) & 0xFFFF;
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryWordAddition(word1, word2));
        registers.setFlag(Flag.CARRY, BitUtils.isCarryWordAddition(word1, word2));

        clock.addCycles(4);
        return result;
    }

    @Override
    public int decWord(int word) {
        clock.addCycles(4);
        return --word & 0xFFFF;
    }

    @Override
    public int rotateByteRight(int bytee, boolean throughCarry) {
        int result = (bytee >> 1) & 0xFF;

        if (throughCarry) { // old carry value to bit 7
            if (registers.getFlag(Flag.CARRY)) result = BitUtils.setBit(result,7);
        } else { // old bit 0 to bit 7
            if (BitUtils.isBitSet(bytee, 0)) result = BitUtils.setBit(result, 7);
        }

        registers.setFlag(Flag.CARRY, BitUtils.isBitSet(bytee, 0)); // old bit 0 to carry flag
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
        return result;
    }

    @Override
    public int complementByte(int bytee) {
        int result = (~bytee & 0xFF);
        registers.setFlag(Flag.SUBTRACTION, true);
        registers.setFlag(Flag.HALF_CARRY, true);

        return result;
    }

    @Override
    public int addBytes(int byte1, int byte2, boolean addCarry) {
        int byte3 = 0;
        if (addCarry && registers.getFlag(Flag.CARRY)) byte3 = 1;

        int result = (byte1 + byte2 + byte3) & 0xFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryByteAddition(byte1, byte2, byte3));
        registers.setFlag(Flag.CARRY, BitUtils.isCarryByteAddition(byte1, byte2, byte3));

        return result;
    }

    @Override
    public int subBytes(int byte1, int byte2, boolean subCarry) {
        int toSub = byte2;
        if (subCarry && registers.getFlag(Flag.CARRY)) ++toSub;

        int result = (byte1 - toSub) & 0XFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, true);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryByteSubtraction(byte1, toSub));
        registers.setFlag(Flag.CARRY, BitUtils.isCarryByteSubtraction(byte1, toSub));

        return result;
    }

    @Override
    public int andBytes(int byte1, int byte2) {
        int result = byte1 & byte2;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, true);
        registers.setFlag(Flag.CARRY, false);

        return result;
    }

    @Override
    public int xorBytes(int byte1, int byte2) {
        int result = byte1 ^ byte2;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
        registers.setFlag(Flag.CARRY, false);

        return result;
    }

    @Override
    public int orBytes(int byte1, int byte2) {
        int result = byte1 | byte2;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
        registers.setFlag(Flag.CARRY, false);

        return result;
    }

    @Override
    public int shiftByteLeft(int bytee) {
        int result = (bytee << 1) & 0xFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
        registers.setFlag(Flag.CARRY, BitUtils.isBitSet(bytee, 7));

        return result;
    }

    @Override
    public int shiftByteRight(int bytee, boolean resetBit7) {
        int result = (bytee >> 1) & 0xFF;
        if (!resetBit7 && BitUtils.isBitSet(bytee, 7)) {
            result = BitUtils.setBit(result, 7);
        }
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
        registers.setFlag(Flag.CARRY, BitUtils.isBitSet(bytee, 0));

        return result;
    }

    @Override
    public int swapNibbles(int bytee) {
        int upperNibble = (bytee >> 4) & 0xF;
        int lowerNibble = (bytee << 4) & 0xF0;
        int result = lowerNibble | upperNibble;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, false);
        registers.setFlag(Flag.CARRY, false);

        return result;
    }

    @Override
    public void testBit(int bytee, int bitNum) {
        registers.setFlag(Flag.ZERO, !BitUtils.isBitSet(bytee, bitNum));
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, true);
    }

    @Override
    public int resetBit(int bytee, int bitNum) {
        return BitUtils.resetBit(bytee, bitNum);
    }

    @Override
    public int setBit(int bytee, int bitNum) {
        return BitUtils.setBit(bytee, bitNum);
    }

    @Override
    public int decimalAdjust(int bytee) {
        if (!registers.getFlag(Flag.SUBTRACTION)) {  // after an addition, adjust if (half-)carry occurred or if result is out of bounds
            if (registers.getFlag(Flag.CARRY) || bytee > 0x99) { bytee += 0x60; bytee &= 0xff; registers.setFlag(Flag.CARRY, true); }
            if (registers.getFlag(Flag.HALF_CARRY) || (bytee & 0x0f) > 0x09) { bytee += 0x6; bytee &= 0xff; }
        } else {  // after a subtraction, only adjust if (half-)carry occurred
            if (registers.getFlag(Flag.CARRY)) { bytee -= 0x60; bytee &= 0xff; }
            if (registers.getFlag(Flag.HALF_CARRY)) { bytee -= 0x6; bytee &= 0xff; }
        }
        // these flags are always updated
        registers.setFlag(Flag.ZERO, bytee == 0);
        registers.setFlag(Flag.HALF_CARRY, false);
        return bytee;
    }

    @Override
    public int addSignedByteToWord(int word, int signedByte) {
        registers.setFlag(Flag.ZERO, false);
        registers.setFlag(Flag.SUBTRACTION, false);

        int result = word + (byte) signedByte;
        registers.setFlag(Flag.CARRY, (((word & 0xff) + (signedByte & 0xff)) & 0x100) != 0);
        registers.setFlag(Flag.HALF_CARRY, (((word & 0x0f) + (signedByte & 0x0f)) & 0x10) != 0);
        clock.addCycles(4);
        return result & 0xffff;
    }
}
