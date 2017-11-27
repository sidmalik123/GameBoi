package mmu.displaypiece;

/**
 * Interface to represent a Game Boy Sprite
 * */
public interface GBSprite {

    /**
     * Returns the X coordinate of the Sprite on the screen
     * */
    int getXPos();

    /**
     * Sets the X coordiinate of the Sprite on the screen
     * */
    void setXPos(int xPos);

    /**
     * Returns the Y coordinate of the Sprite on the screen
     * */
    int getYPos();

    /**
     * Sets the Y coordiinate of the Sprite on the screen
     * */
    void setYPos(int xPos);

    /**
     * Returns the tile this Sprite points to
     * */
    GBTile getTile();

    /**
     * Sets the tile this Sprite points to
     * */
    void setTile(GBTile tile);

    /**
     * gets this sprite's attributes
     * */
    GBSpriteAttributes getAttributes();

    /**
     * Sets this sprite's aatributes
     * */
    void serAttributes(GBSpriteAttributes attributes);
}
