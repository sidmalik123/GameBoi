package mmu.displaypiece;

public class GBSpritesAttributesImpl implements GBSpriteAttributes {
    private boolean isSpritePrioritized;
    private boolean isXFlip;
    private boolean isYFlip;
    private int paletteNum;

    public boolean isSpritePrioritized() {
        return isSpritePrioritized;
    }

    public void isSpritePrioritized(boolean isPrioritized) {
        this.isSpritePrioritized = isPrioritized;
    }

    public boolean isXFlip() {
        return isXFlip;
    }

    public void setXFlip(boolean xFlip) {
        this.isXFlip = xFlip;
    }

    public boolean isYFlip() {
        return isYFlip;
    }

    public void setYFlip(boolean yFlip) {
        this.isYFlip = yFlip;
    }

    public int getPaletteNum() {
        return paletteNum;
    }

    public void setPaletteNum(int paletteNum) {
        this.paletteNum = paletteNum;
    }
}
