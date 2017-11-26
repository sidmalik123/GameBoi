package gpu.palette;

/**
 * Interface to represent a GameBoy color palette
 * */
public interface GBPalette {

    /**
     * The 4 colors GBC is capable of displaying
     * */
    enum Color {WHITE, LIGHT_GREY, DARK_GREY, BLACK};

    /**
     * Sets the mapping from colorNum -> color,
     * colorNum should be between 1-4
     * */
    void setColor(int colorNum, Color color);

    /**
     * Gets the Color colorNum is mapped to,
     * colorNum should be between 1-4
     * */
    Color getColor(int colorNum);
}
