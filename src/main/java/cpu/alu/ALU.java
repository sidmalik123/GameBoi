package cpu.alu;

/**
 * Arithmetic logic unit of GameBoy
 * */
public interface ALU {

    /**
     * Increments word by 1
     * */
    int incWord(int word);

    /**
     * Increments bytee by 1
     * */
    int incByte(int bytee);

    /**
     * Decrements bytee by 1
     * */
    int decByte(int bytee);

    /**
     * Rotates bytee left by 1 bit
     * */
    int rotateByteLeft(int bytee, boolean throughCarry);

    /**
     * Adds word1 and word2
     * */
    int addWords(int word1, int word2);

    /**
     * Decrements word by 1
     * */
    int decWord(int word);

    /**
     * Rotates bytee right by 1 bit
     * */
    int rotateByteRight(int bytee, boolean throughCarry);

    /**
     * Complements bytee
     * */
    int complementByte(int bytee);

    /**
     * returns the result of byte1 + byte2
     * */
    int addBytes(int byte1, int byte2, boolean addCarry);

    /**
     * Returns the result of byte1 - byte2
     * */
    int subBytes(int byte1, int byte2, boolean subCarry);

    /**
     * Returns the result byte1 & byte2
     * */
    int andBytes(int byte1, int byte2);

    /**
     * XORs byte1 and byte2 and returns the result
     * */
    int xorBytes(int byte1, int byte2);

    /**
     * ORs byte1 and byte2 and returns the result
     * */
    int orBytes(int byte1, int byte2);

    /**
     * Shifts bytee left by 1 bit
     * */
    int shiftByteLeft(int bytee);

    /**
     * Shifts bytee right by 1 bit
     * */
    int shiftByteRight(int bytee, boolean resetBit7);

    /**
     * Swaps upper and lower nibbles if bytee
     * */
    int swapNibbles(int bytee);

    /**
     * Tests bitnum of bytee
     * */
    void testBit(int bytee, int bitNum);

    /**
     * Resets bitNum in bytee
     * */
    int resetBit(int bytee, int bitNum);

    /**
     * Sets bitNum in bytee
     * */
    int setBit(int bytee, int bitNum);

    /**
     * This is for the DAA instruction
     * */
    int decimalAdjust(int bytee);

    /**
     * Add signedByte to word
     * */
    int addSignedByteToWord(int word, int signedByte);
}
