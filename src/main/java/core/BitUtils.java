package core;

public class BitUtils {

    /**
     * Checks if carry occurs from bit 3 to bit 4 in addition of byte1 and byte2
     * */
    public static boolean isHalfCarryByteAddition(int byte1, int byte2) {
        return (byte1 & 0x0F) + (byte2 & 0x0F) > 0x0F;
    }

    /**
     * Checks if carry occurs from bit 7 to bit 8
     * */
    public static boolean isCarryByteAddition(int val1, int val2) {
        return val1 + val2 > 0xFF;
    }

    /**
     * Todo
     * */
    public static boolean isHalfCarryWordAddition(int val1, int val2) {
        return (val1 & 0x0FFF) + (val2 & 0x0FFF) > 0x0FFF;
    }

    /**
     * Todo
     * */
    public static boolean isCarryWordAddition(int val1, int val2) {
        return val1 + val2 > 0xFFFF;
    }

    /**
     * Checks if borrow happens from bit4
     * */
    public static boolean isHalfCarryByteSubtraction(int val1, int val2) {
        return ((val1 & 0xF) - (val2 & 0xF)) < 0;
    }

    /**
     * Checks if borrow occurs
     * */
    public static boolean isCarryByteSubtraction(int val1, int val2) {
        return ((val1 & 0xFF) - (val2 & 0xFF)) < 0;
    }

    /**
    * checks if a particular bit is set or not
    * */
    public static boolean isBitSet(int data, int bitPos) {
        int bitVal = (data >> bitPos) & 1;
        return bitVal == 1;
    }


    /**
    * Sets a particular bit
    * */
    public static int setBit(int data, int bitPos) {
        return data | 1 << bitPos;
    }

    /**
    * resets a particular bit
    * https://stackoverflow.com/questions/1073318/in-java-is-it-possible-to-clear-a-bit
    * */
    public static int resetBit(int data, int bitPos) {
        return data & ~(1 << bitPos);
    }

    /**
     * Joins highByte and lowByte to form a word
     * */
    public static int joinBytes(int highByte, int lowByte) {
        return (highByte << 8) | lowByte;
    }

    /**
     * @return lower byte of word
     * */
    public static int getLowByte(int word) {
        return word & 0xFF;
    }

    /**
     * @return higher byte of word
     * */
    public static int getHighByte(int word) {
        return (word >> 8) & 0xFF;
    }
}

