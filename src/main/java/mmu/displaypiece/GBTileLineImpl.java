package mmu.displaypiece;

/**
 * GBTileLine implementing class, stores pixels in an array
 * */
public class GBTileLineImpl implements GBTileLine {

    int[] pixels = new int[8];

    public void setPixelColorNum(int pixelNum, int colorNum) {
        if (pixelNum > 7 || pixelNum < 0)
            throw new IllegalArgumentException("Invalid pixel num: must be b/w 0-7");

        pixels[pixelNum] = colorNum;
    }

    public int getPixelColorNum(int pixelNum) {
        if (pixelNum > 8 || pixelNum < 1)
            throw new IllegalArgumentException("Invalid pixel num: must be b/w 0-7");

        return pixels[pixelNum];
    }
}
