package core;

public class BitUtils {

    public static boolean isHalfCarryAdd(int val1, int val2) {
        return (((val1 & 0xF) + (val2 & 0xF)) & 0x10) == 0x10;
    }

    public static boolean isCarryAdd(int val1, int val2) {
        return (((val1 & 0xFF) + (val2 & 0xFF)) & 0x100) == 0x100;
    }

    public static boolean isZeroAdd(int val1, int val2) {
        return ((val1 + val2) & 0xFF) == 0x00;
    }

    public static boolean isHalfCarrySub(int val1, int val2) {
        return ((val1 & 0xF) - (val2 & 0xF)) < 0;
    }

    public static boolean isCarrySub(int val1, int val2) {
        return ((val1 & 0xFF) - (val2 & 0xFF)) < 0;
    }

    public static boolean isZeroSub(int val1, int val2) {
        return ((val1 - val2) & 0xFF) == 0x00;
    }
}

