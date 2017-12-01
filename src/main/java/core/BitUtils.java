package core;

public class BitUtils {

    public static boolean isHalfCarryAdd(int val1, int val2) {
        return (((val1 & 0xF) + (val2 & 0xF)) & 0x10) == 0x10;
    }

    public static boolean isCarryAdd(int val1, int val2) {
        return (((val1 & 0xFF) + (val2 & 0xFF)) & 0x100) == 0x100;
    }

    public static boolean isHalfCarrySub(int val1, int val2) {
        return ((val1 & 0xF) - (val2 & 0xF)) < 0;
    }

    public static boolean isCarrySub(int val1, int val2) {
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
}

