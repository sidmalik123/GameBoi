package mmu.displaypiece;

/**
 * Interface to represent a GameBoy display Tile
 * */
public interface GBTile {

    int NUM_LINES = 8;

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
