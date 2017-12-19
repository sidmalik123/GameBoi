package cpu.alu;

import core.BitUtils;
import cpu.registers.Registers;

/**
 * Concrete class implementing ALU
 * */
public class ALUImpl implements ALU {

    private Registers registers;

    public ALUImpl(Registers registers) {
        this.registers = registers;
    }

    @Override
    public int joinBytes(int highByte, int lowByte) {
        return BitUtils.joinBytes(highByte, lowByte);
    }

    @Override
    public void cmpBytes(int byte1, int byte2) {
        registers.setFlag(Registers.Flag.ZERO, ((byte1 - byte1) & 0xFF) == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, true);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarrySub8Bit(byte1, byte2));
        registers.setFlag(Registers.Flag.CARRY, BitUtils.isCarrySub8Bit(byte1, byte2));
    }

    @Override
    public int xor(int val1, int val2) {
        int result = val1 ^ val2;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, false);

        return result;
    }

    @Override
    public int subBytes(int val1, int val2, boolean withCarry) {
        int toSub = withCarry && registers.getFlag(Registers.Flag.CARRY) ? val2 + 1: val2;
        int result = val1 - toSub;
        registers.setFlag(Registers.Flag.ZERO, (result & 0xFF) == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, true);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarrySub8Bit(val1, toSub));
        registers.setFlag(Registers.Flag.CARRY, BitUtils.isCarrySub8Bit(val1, toSub));
        return result;
    }

    @Override
    public int dec(int val) {
        return --val;
    }

    @Override
    public int incByte(int byteToInc) {
        int result = (++byteToInc) & 0xFF;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarryAdd8Bit(byteToInc, 1));
        return result;
    }

    @Override
    public void testBit(int val, int bitNum) {
        registers.setFlag(Registers.Flag.ZERO, !BitUtils.isBitSet(val, bitNum));
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, true);
    }

    @Override
    public int addBytes(int byte1, int byte2) {
        int result = (byte1 + byte2) & 0xFF;
        registers.setFlag(Registers.Flag.ZERO, result == 0);
        registers.setFlag(Registers.Flag.SUBTRACTION, false);
        registers.setFlag(Registers.Flag.HALF_CARRY, BitUtils.isHalfCarryAdd8Bit(byte1, byte2));
        registers.setFlag(Registers.Flag.CARRY, BitUtils.isCarryAdd8Bit(byte1, byte2));
        return result;
    }
}
