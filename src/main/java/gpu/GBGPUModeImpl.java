package gpu;

public class GBGPUModeImpl implements GBGPUMode {

    private GPUModeType modeType;
    private boolean isLCDInterruptEnabled;

    public GBGPUModeImpl(GPUModeType modeType) {
        this.modeType = modeType;
    }

    public GPUModeType getGPUModeType() {
        return modeType;
    }

    public boolean isLCDInterruptEnabled() {
        return isLCDInterruptEnabled;
    }

    public void setLCDInterruptEnabled(boolean isLCDInterruptEnabled) {
        this.isLCDInterruptEnabled = isLCDInterruptEnabled;
    }
}
