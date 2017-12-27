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
        int result = ++bytee & 0xFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryByteAddition(bytee, 1));

        return result;
    }

    @Override
    /**
     * Z 1 H -
     * */
    public int decByte(int bytee) {
        int result = --bytee & 0xFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, true);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryByteSubtraction(bytee, 1));

        return result;
    }

    /**
     * - - - C
     * */
    public int rotateByteLeft(int bytee, boolean throughCarry) {
        int result = (bytee << 1) & 0xFF;

        if (throughCarry) { // old carry value to bit 0
            result = registers.getFlag(Flag.CARRY) ? BitUtils.setBit(result, 0) : BitUtils.resetBit(result, 0);
        } else { // old bit 7 to bit 0
            result = BitUtils.isBitSet(bytee, 7) ? BitUtils.setBit(result, 0) : BitUtils.resetBit(bytee, 0);
        }

        registers.setFlag(Flag.CARRY, BitUtils.isBitSet(bytee, 7)); // old bit 7 to carry flag
        registers.setFlag(Flag.ZERO, result == 0);
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
            result = registers.getFlag(Flag.CARRY) ? BitUtils.setBit(result, 7) : BitUtils.resetBit(result, 7);
        } else { // old bit 0 to bit 7
            result = BitUtils.isBitSet(bytee, 0) ? BitUtils.setBit(result, 7) : BitUtils.resetBit(bytee, 7);
        }

        registers.setFlag(Flag.CARRY, BitUtils.isBitSet(bytee, 0)); // old bit 0 to carry flag
        registers.setFlag(Flag.ZERO, result == 0);
        return result;
    }

    @Override
    public int complementByte(int bytee) {
        int result = (bytee ^= 0xFF);
        registers.setFlag(Flag.SUBTRACTION, true);
        registers.setFlag(Flag.HALF_CARRY, true);

        return result;
    }

    @Override
    public int addBytes(int byte1, int byte2, boolean addCarry) {
        int toAdd = byte2;
        if (addCarry && registers.getFlag(Flag.CARRY)) ++toAdd;

        int result = (byte1 + toAdd) & 0XFF;
        registers.setFlag(Flag.ZERO, result == 0);
        registers.setFlag(Flag.SUBTRACTION, false);
        registers.setFlag(Flag.HALF_CARRY, BitUtils.isHalfCarryByteAddition(byte1, toAdd));
        registers.setFlag(Flag.CARRY, BitUtils.isCarryByteAddition(byte1, toAdd));

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


}
