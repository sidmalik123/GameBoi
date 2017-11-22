package gpu;

/**
 * Interface to represent a GameBoy GPU mode
 * */
public interface GBGPUMode {

    /**
     * Returns the type of this GPU mode
     * */
    GPUModeType getGPUModeType();

    /**
     *  Returns if LCD interrupt is enabled
     *  for this GPU mode
     * */
    boolean isLCDInterruptEnabled();

    /**
     *  Sets/Resets LCD interrupt for
     *  this GPU mode
     * */
    void setLCDInterruptEnabled(boolean isLCDInterruptEnabled);
}
