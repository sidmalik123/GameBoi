package core;

public class BitUtils {

    public static boolean isHalfCarryAdd8Bit(int val1, int val2) {
        return (val1 & 0x0F) + (val2 & 0x0F) > 0x0F;
    }

    public static boolean isCarryAdd8Bit(int val1, int val2) {
        return val1 + val2 > 0xFF;
    }

    public static boolean isHalfCarryAdd16Bit(int val1, int val2) {
        return (val1 & 0x0FFF) + (val2 & 0x0FFF) > 0x0FFF;
    }

    public static boolean isCarryAdd16Bit(int val1, int val2) {
        return val1 + val2 > 0xFFFF;
    }

    public static boolean isHalfCarrySub8Bit(int val1, int val2) {
        return ((val1 & 0xF) - (val2 & 0xF)) < 0;
    }

    public static boolean isCarrySub8Bit(int val1, int val2) {
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

