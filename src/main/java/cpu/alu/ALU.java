package cpu.alu;

/**
 * Interface to represent a GameBoy's arithmetic logic unit
 * */
public interface ALU {

    /**
     * Returns word formed by joining highByte and lowByte
     * */
    int joinBytes(int highByte, int lowByte);

    /**
     * Compares byte1 and byte2
     * Z- Set if result is zero. (Set if A=n.)
     * N- Set.
     * H- Set if no borrow from bit 4.
     * C- Set for no borrow. (Set if A<n.)
     * */
    void cmpBytes(int byte1, int byte2);

    /**
     * Logical XOR val1 with val2
     * @return the xored result
     * */
    int xor(int val1, int val2);

    /**
     * Subtract n+CarryflagfromA.
     *
     * Z- Set if result is zero.
     * N- Set.
     * H- Set if no borrow from bit 4.
     * C- Set for no borrow.
     * */
    int subBytes(int val1, int val2, boolean withCarry);

    /**
     * Decrements val by 1
     * No flags affected
     * */
    int dec(int val);

    /**
     * Increment byteToInc by 1
     * Z - Set if result is zero.
     * N- Reset.
     * H- Set if carry from bit 3.
     * C- Not affected.
     * */
    int incByte(int byteToInc);

    /**
     * Test bit bitNum in register val.
     *
     * Z - Set if bit b of register r is 0.
     * N- Reset.
     * H- Set.
     * C- Not affected.
     * */
    void testBit(int val, int bitNum);

    /**
     * Adds byte1 and byte2
     * @return the resulting byte
     * Z 0 H C
     * */
     int addBytes(int byte1, int byte2);
}
