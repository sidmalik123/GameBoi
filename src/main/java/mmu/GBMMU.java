package mmu;

import core.TimingObserver;

public interface GBMMU extends MMU, TimingObserver {

    /**
     * Returns the tile identification for the background
     * */
    int[] getBackgroundTiteIdentificationData();

    /**
     * Returns the tile identification for the window
     * */
    int[] getWindowTilaIdentificationData();


}
