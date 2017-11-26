package mmu.tiles;

/**
 * Interface to represent a GameBoy Tile line,
 * has 8 pixels
 * */
public interface GBTileLine {

    /**
     * Sets pixel pixelNum to have color colorNum
     * */
    void setPixelColorNum(int pixelNum, int colorNum);

    /**
     * Gets the color number assigned to pixel pixelNum
     * */
    int getPixelColorNum(int pixelNum);
}
