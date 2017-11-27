package mmu;

import core.TimingObserver;
import mmu.displaypiece.GBSprite;
import mmu.displaypiece.GBTile;

import java.util.List;
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

    /**
     * Returns a map of tileNum -> Tile for the window
     * */
    List<GBSprite> getSprites();
}
