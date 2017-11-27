package mmu.displaypiece;

public class GBSpriteImpl implements GBSprite {

    private int xPos;
    private int yPos;
    private GBTile tile;
    private GBSpriteAttributes attributes;

    public int getXPos() {
        return 0;
    }

    public void setXPos(int xPos) {

    }

    public int getYPos() {
        return 0;
    }

    public void setYPos(int xPos) {

    }

    public void setTile(GBTile tile) {
        this.tile = tile;
    }

    public GBSpriteAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(GBSpriteAttributes attributes) {
        this.attributes = attributes;
    }

    public GBTile getTile() {
        return tile;
    }

}
