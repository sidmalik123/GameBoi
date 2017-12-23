package gpu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayImpl extends JPanel implements Display {

    private final BufferedImage img = new BufferedImage(160, 144, BufferedImage.TYPE_INT_RGB);

    @Override
    public void setPixel(int x, int y, Color color) {
        img.setRGB(x, y, color.getRGB());
    }

    @Override
    public void refresh() {
        validate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(img, 0, 0, java.awt.Color.WHITE,null);
        g2d.dispose();
    }
}