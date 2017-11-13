package core;

public class BitUtils {

    public static boolean isHalfCarry(int val1, int val2) {
        return (((val1 & 0xF) + (val2 & 0xF)) & 0x10) == 0x10;
    }

    public static boolean isCarry(int val1, int val2) {
        return (((val1 & 0xFF) + (val2 & 0xFF)) & 0x100) == 0x100;
    }
}
