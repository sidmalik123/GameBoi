package mmu.displaypiece;

/**
 * Interface to represent a Sprite attributes
 * */
public interface GBSpriteAttributes {

    /**
     * If true then sprite is rendered above background and window,
     * if false then background and window are rendered above sprite
     * unless the color of background is white
     * */
    boolean isSpritePrioritized();

    /**
     * Sets the background priority
     * */
    void isSpritePrioritized(boolean isPrioritized);

    /**
     * Do we need to flip the sprite horizontally
     * */
    boolean isXFlip();

    /**
     * Sets if the sprite needs to be horizontally flipped
     * */
    void setXFlip(boolean xFlip);

    /**
     * Do we need to flip the sprite vertically
     * */
    boolean isYFlip();

    /**
     * Sets if the sprite needs to be flip vertically flipped
     * */
    void setYFlip(boolean yFlip);

    /**
     * returns the palette num used by this sprite
     * */
    int getPaletteNum();

    /**
     * sets the palette num used by this sprite,
     * paletteNum must be 1 or 2
     * */
    void setPaletteNum(int paletteNum);
}
