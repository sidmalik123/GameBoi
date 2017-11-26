package mmu;

import core.TimingObserver;
import mmu.tiles.GBTile;

import java.util.Map;

public interface GBMMU extends MMU, TimingObserver {

    /**
     * Returns a map of tileNum -> Tile for the background
     * */
    Map<Integer, GBTile> getBackgroundTileMap();

    /**
     * Returns a map of tileNum -> Tile for the window
     * */
    Map<Integer, GBTile> getWindowTileMap();
}
