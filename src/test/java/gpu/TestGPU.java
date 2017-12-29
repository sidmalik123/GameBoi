package gpu;

import cpu.clock.ClockImpl;
import interrupts.InterruptManagerImpl;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestGPU {

    private GPU gpu;

    @Before
    public void resetGPU() { // clean gpu for every test
        gpu = new GPUImpl(new MockDisplay(), new InterruptManagerImpl(), new ClockImpl());
    }

    @Test
    public void testGPUModeValues() {
        assertEquals(GPUMode.ACCESSING_OAM.getNumCyclesToSpend(), 80);
        assertEquals(GPUMode.ACCESSING_VRAM.getNumCyclesToSpend(), 172);
        assertEquals(GPUMode.HBLANK.getNumCyclesToSpend(), 204);
        assertEquals(GPUMode.VBLANK.getNumCyclesToSpend(), 456);

        assertEquals(GPUMode.ACCESSING_OAM.getModeNum(), 2);
        assertEquals(GPUMode.ACCESSING_VRAM.getModeNum(), 3);
        assertEquals(GPUMode.HBLANK.getModeNum(), 0);
        assertEquals(GPUMode.VBLANK.getModeNum(), 1);

        assertEquals(GPUMode.ACCESSING_OAM.getInterruptBitNum(), 5);
        assertEquals(GPUMode.HBLANK.getInterruptBitNum(), 3);
        assertEquals(GPUMode.VBLANK.getInterruptBitNum(), 4);

        try {
            GPUMode.ACCESSING_VRAM.getInterruptBitNum();
            fail();
        } catch (NotImplementedException ignore) {}
    }

    @Test
    public void testStartMode() {
        assertEquals(getCurrMode(), 2);
    }

    @Test
    public void testWriteToCurrLine() { // writes to curr line default to 0x00
        gpu.write(GPU.CURR_LINE_NUM_ADDRESS, 0x3F);
        assertCurrLineNum(0x00);
    }

    @Test
    public void testLCDDisabled() {
        gpu.write(GPU.CURR_LINE_NUM_ADDRESS, 20);
        gpu.handleClockIncrement(20);
        assertEquals(getCurrMode(), 1); // vblank
        assertCurrLineNum(0x00);
    }

    @Test
    public void testModeChange() {
        enableLCD();
        for (int line = 0; line < 144; ++line) {
            int oldLineNum = gpu.read(GPU.CURR_LINE_NUM_ADDRESS);
            for (int i = 1; i < 80; ++i) {
                gpu.handleClockIncrement(1);
                assertEquals(getCurrMode(), 2);
                assertCurrLineNum(oldLineNum);
            }
            for (int i = 0; i < 172; ++i) {
                gpu.handleClockIncrement(1);
                assertEquals(getCurrMode(), 3);
                assertCurrLineNum(oldLineNum);
            }
            for (int i = 0; i < 204; ++i) {
                gpu.handleClockIncrement(1);
                assertEquals(getCurrMode(), 0);
                assertCurrLineNum(oldLineNum);
            }

            gpu.handleClockIncrement(1);
            assertCurrLineNum(oldLineNum + 1);
            if (line < 143) {
                assertEquals(getCurrMode(), 2);
            } else {
                assertEquals(getCurrMode(), 1); // vblank
            }
        }
        for (int numLinesInVblank = 0; numLinesInVblank < 10; ++numLinesInVblank) {
            for (int i = 1; i < 456; ++i) {
                gpu.handleClockIncrement(1);
                assertEquals(getCurrMode(), 1); // stay in vblank
                assertCurrLineNum(144 + numLinesInVblank);
            }
            gpu.handleClockIncrement(1);
            if (numLinesInVblank < 9) {
                assertEquals(getCurrMode(), 1);
                assertCurrLineNum(144 + numLinesInVblank + 1);
            } else {
                assertEquals(getCurrMode(), 2);
                assertCurrLineNum(0);
            }
        }
    }

    private int getCurrMode() {
        return gpu.read(GPU.LCD_STATUS_REGISTER_ADDRESS) & 3;
    }

    private void enableLCD() {
        gpu.write(GPU.LCD_CONTROL_REGISTER_ADDRESS, 0b10000000);
    }

    private void assertCurrLineNum(int expected) {
        assertEquals(gpu.read(GPU.CURR_LINE_NUM_ADDRESS), expected);
    }
}
