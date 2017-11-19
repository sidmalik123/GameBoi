package gpu;

public enum GPUMode {

    ACCESSING_OAM, ACCESSING_VRAM, HBLANK, VBLANK;

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
        throw new IllegalArgumentException("Unknow GPUMode " + this);
    }
}
