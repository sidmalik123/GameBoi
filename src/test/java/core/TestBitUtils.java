package core;

import cpu.CPU;
import cpu.CPUImpl;
import cpu.InstructionExecutor;
import cpu.InstructionExecutorImpl;
import cpu.registers.RegistersImpl;
import mmu.MMU;
import mmu.MMUFactoryImpl;
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

        assert (BitUtils.joinBytes(higherByte, lowerByte) != 0b1001110100100100);
    }
}
