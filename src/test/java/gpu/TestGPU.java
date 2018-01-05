package gpu;

import interrupts.Interrupt;
import interrupts.InterruptManager;
import interrupts.InterruptManagerImpl;
import mmu.MMU;
import mmu.MMUImpl;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestGPU {

    private GPU gpu;
    private InterruptManager interruptManager;
    private MMU mmu;

    @Before
    public void resetGPU() { // clean gpu for every test
        mmu = new MMUImpl();
        interruptManager = new InterruptManagerImpl(mmu);
        gpu = new GPUImpl(new MockDisplay(), interruptManager, mmu);
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
        mmu.write(MMU.CURR_LINE_NUM_ADDRESS, 0x3F);
        assertCurrLineNum(0x00);

        mmu.setCurrLineNum(100);
        assertEquals(mmu.read(MMU.CURR_LINE_NUM_ADDRESS), 100);
    }

    @Test
    public void testLCDDisabled() {
        disableLCD();
        mmu.write(MMU.CURR_LINE_NUM_ADDRESS, 20);
        gpu.handleClockIncrement(20);
        assertEquals(getCurrMode(), 1); // vblank
        assertCurrLineNum(0x00);
    }

    @Test
    public void testModeChange() {
        enableLCD();
        interruptManager.setInterruptsEnabled(true);
        mmu.write(MMU.INTERRUPT_ENABLE_REGISTER, 0xFF); // enable all register
        for (int line = 0; line < 144; ++line) {
            int oldLineNum = mmu.read(MMU.CURR_LINE_NUM_ADDRESS);
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
                assertEquals(interruptManager.getPendingInterrupt(), Interrupt.VBLANK); // vblank interrupt is requested
            }
        }
        int vblankLineNum = 144;
        for (int numLinesInVblank = 0; numLinesInVblank < 10; ++numLinesInVblank) {
            for (int i = 1; i < 456; ++i) {
                gpu.handleClockIncrement(1);
                assertEquals(getCurrMode(), 1); // stay in vblank
                assertCurrLineNum(vblankLineNum);
            }
            ++vblankLineNum;
            gpu.handleClockIncrement(1);
            if (vblankLineNum <= 153)
                assertCurrLineNum(vblankLineNum);
            else
                assertCurrLineNum(0);
        }
    }

    @Test
    public void testDimensions() {
        assertEquals(GPU.HEIGHT, 144);
        assertEquals(GPU.WIDTH, 160);
    }

    private int getCurrMode() { // bit 1 and bit 0 represent the mode num
        return mmu.read(MMU.LCD_STATUS_REGISTER_ADDRESS) & 3;
    }

    private void enableLCD() {
        mmu.write(MMU.LCD_CONTROL_REGISTER_ADDRESS, 0b10000000);
    }

    private void disableLCD() {
        mmu.write(MMU.LCD_CONTROL_REGISTER_ADDRESS, 0x00);
    }

    private void assertCurrLineNum(int expected) {
        assertEquals(mmu.read(MMU.CURR_LINE_NUM_ADDRESS), expected);
    }
}
