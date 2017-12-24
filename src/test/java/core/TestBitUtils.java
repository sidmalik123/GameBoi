package core;

import org.junit.Test;

public class TestBitUtils {

    @Test
    public void testIsBitSet() {
        int eightBit = 0b10011101;
        assert (BitUtils.isBitSet(eightBit,7));
        assert (!BitUtils.isBitSet(eightBit, 5));
        assert (BitUtils.isBitSet(eightBit, 0));
        assert (BitUtils.isBitSet(eightBit, 4));

        int sixteenBit = 0b1011000101100010;

        assert (!BitUtils.isBitSet(sixteenBit, 0));
        assert (BitUtils.isBitSet(sixteenBit, 1));
        assert (BitUtils.isBitSet(sixteenBit, 15));
        assert (BitUtils.isBitSet(sixteenBit, 8));
    }

    @Test
    public void testSetBit() {
        int num = 0;
        for (int i = 0; i < 8; ++i) {
            num = BitUtils.setBit(num, i);
        }

        // if all bits got set num should be 255 now
        assert (num == 255);
    }

    @Test
    public void testResetBit() {
        int num = 255;
        for (int i = 0; i < 8; ++i) {
            num = BitUtils.resetBit(num, i);
        }

        // if all bits got reset num should be 0 now
        assert (num == 0);

        num = 3;
        num = BitUtils.resetBit(num, 0);
        assert (num == 2);
    }

    @Test
    public void testJoinBytes() {
        int higherByte = 0b10011101;
        int lowerByte = 0b00100100;

        assert (BitUtils.joinBytes(higherByte, lowerByte) == 0b1001110100100100);
    }

    @Test
    public void testHalfCarryAdd() {
        int byte1 = 0b00001010;
        int byte2 = 0b00001100;

        assert (BitUtils.isHalfCarryByteAddition(byte1, byte2));

        int byte3 = 0b10010011;
        int byte4 = 0b10101010;

        assert (!BitUtils.isHalfCarryByteAddition(byte3, byte4));
    }

    @Test
    public void testCarryAdd() {
        int byte1 = 0b10010011;
        int byte2 = 0b10101010;

        assert (BitUtils.isCarryByteAddition(byte1, byte2));

        int byte3 = 0b00001010;
        int byte4 = 0b00001100;

        assert (!BitUtils.isCarryByteAddition(byte3, byte4));
    }

    @Test
    public void testLowAndHighByte() {
        int word = 0b1010101001010101;

        assert (BitUtils.getHighByte(word) == 0b10101010);
        assert (BitUtils.getLowByte(word) == 0b01010101);
    }

    @Test
    public void testHalfCarrySubtraction() {
        int byte1 = 0b0101110;
        int byte2 = 0b00100110;

        assert (!BitUtils.isHalfCarryByteSubtraction(byte1, byte2));

        int byte3 = 0b00101111;

        assert (BitUtils.isHalfCarryByteSubtraction(byte1, byte3));
    }

    @Test
    public void testCarrySubtraction() {
        int byte1 = 0b10010011;
        int byte2 = 0b10001010;

        assert (!BitUtils.isCarryByteSubtraction(byte1, byte2));

        int byte3 = 0b11111111;

        assert (BitUtils.isCarryByteSubtraction(byte1, byte3));
    }
}
