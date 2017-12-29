package gpu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayImpl extends JPanel implements Display {

    private final BufferedImage img = new BufferedImage(160, 144, BufferedImage.TYPE_INT_RGB);

    public DisplayImpl() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame mainWindow = new JFrame("GameBoi");
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindow.setLocationRelativeTo(null);

            this.setSize(160, 144);

            mainWindow.setContentPane(this);
            mainWindow.setResizable(false);
            mainWindow.setVisible(true);
            mainWindow.setSize(160, 144);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

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