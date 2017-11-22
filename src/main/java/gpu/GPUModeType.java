package gpu;

/**
 * The different modes the GPU enters
 * when a CRT scan is done
 * */
public enum GPUModeType {

    ACCESSING_OAM, ACCESSING_VRAM, HBLANK, VBLANK;

    /**
     * Returns the number of CPU cycles the GPU
     * spends in a mode
     * */
    public int getNumCyclesToSpend() {
        switch (this) {
            case ACCESSING_OAM:
                return 80;
            case ACCESSING_VRAM:
                return 172;
            case HBLANK:
                return 204;
            case VBLANK:
                return 456;
        }
        throw new IllegalArgumentException("Unknown GPUModeType " + this);
    }
}
