package gpu;

import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestGPU {

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
}
