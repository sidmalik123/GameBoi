package gpu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayImpl extends JPanel implements Display {

    private final BufferedImage img;
    private static final int SCALE = 2;

    public DisplayImpl() {
        img = new BufferedImage(GPU.WIDTH * SCALE, GPU.HEIGHT * SCALE, BufferedImage.TYPE_INT_RGB);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame mainWindow = new JFrame("GameBoi");
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindow.setLocationRelativeTo(null);

            this.setSize(img.getWidth(), img.getHeight()); // fill image

            mainWindow.setContentPane(this);
            mainWindow.setResizable(false);
            mainWindow.setVisible(true);
            mainWindow.setSize(img.getWidth(), img.getHeight());
        } catch(Exception e) {
            throw new DisplayInstantiationException("Error while building display", e);
        }
    }

    @Override
    public void setPixel(int x, int y, Color color) {
        for (int i = 0; i < SCALE; ++i) {
            for (int j = 0; j < SCALE; ++j) {
                img.setRGB(x * SCALE + j, y * SCALE + i, color.getRGB());
            }
        }
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