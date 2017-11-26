package mmu;

import core.TimingObserver;

import java.util.Map;

public interface GBMMU extends MMU, TimingObserver {

    /**
     * Returns tile rendering data,
     * used for both background and window
     * */
    Map<Integer, GBTile> getBackgroundTileMap();

    Map<Integer, GBTile> getWindowTileMap();
}
