package cpu.alu;

import core.BitUtils;

/**
 * Concrete class implementing ALU
 * */
public class ALUImpl implements ALU {

    @Override
    public int joinBytes(int highByte, int lowByte) {
        return BitUtils.joinBytes(highByte, lowByte);
    }
}
