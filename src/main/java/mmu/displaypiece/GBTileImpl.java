package mmu.displaypiece;


public class GBTileImpl implements GBTile {

    GBTileLine[] lines = new GBTileLine[8];

    public void setLine(int lineNum, GBTileLine line) {
        if (lineNum > 7 || lineNum < 0)
            throw new IllegalArgumentException("Invalid pixel num: must be b/w 0-7");
        lines[lineNum-1] = line;
    }

    public GBTileLine getLine(int lineNum) {
        return lines[lineNum-1];
    }
}
