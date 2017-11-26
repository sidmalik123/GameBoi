package gpu.palette;

public class GBPaletteImpl implements GBPalette {
    Color color1;

    Color color2;

    Color color3;

    Color color4;

    public void setColor(int colorNum, Color color) {
        switch (colorNum) {
            case 1:
                color1 = color;
                break;
            case 2:
                color2 = color;
                break;
            case 3:
                color3 = color;
                break;
            case 4:
                color4 = color;
            default:
                throw new IllegalArgumentException("Invalid colorNum: " + colorNum + " passed");
        }
    }

    public Color getColor(int colorNum) {
        switch (colorNum) {
            case 1:
                return color1;
            case 2:
                return color2;
            case 3:
                return color3;
            case 4:
                return color4;
            default:
                throw new IllegalArgumentException("Invalid colorNum: " + colorNum + " passed");
        }
    }
}
