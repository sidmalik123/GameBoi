package mmu.tiles;

/**
 * Interface to represent a GameBoy display Tile
 * */
public interface GBTile {

    /**
     * Sets a GBTile line
     * requires lineNum b/w 0-7
     * */
    void setLine(int lineNum, GBTileLine line);

    /**
     * Gets GBTile line lineNum for the tile
     * requires lineNum b/w 0-7
     * */
    GBTileLine getLine(int lineNum);
}
