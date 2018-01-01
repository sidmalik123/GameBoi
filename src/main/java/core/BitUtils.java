package core;

public class BitUtils {
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

