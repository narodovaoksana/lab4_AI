import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

class DrawingPanel extends JPanel {
    private boolean[][] pixels;
    private int pixelSize;

    public DrawingPanel(int rows, int cols) {
        pixels = new boolean[rows][cols];
        pixelSize = 15;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawPixel(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                drawPixel(e.getX(), e.getY());
            }
        });
    }

    private void drawPixel(int x, int y) {
        int row = y / pixelSize;
        int col = x / pixelSize;

        if (row >= 0 && row < pixels.length && col >= 0 && col < pixels[0].length) {
            pixels[row][col] = true;
            repaint();
        }
    }

    public int[] getPixels() {
        int[] flatPixels = new int[pixels.length * pixels[0].length];
        int index = 0;

        for (boolean[] row : pixels) {
            for (boolean pixel : row) {
                flatPixels[index++] = pixel ? 1 : 0;
            }
        }

        return flatPixels;
    }

    public void clear() {
        for (int row = 0; row < pixels.length; row++) {
            Arrays.fill(pixels[row], false);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col] ? Color.BLACK : Color.WHITE);
                g.fillRect(col * pixelSize, row * pixelSize, pixelSize, pixelSize);
                g.setColor(Color.GRAY);
                g.drawRect(col * pixelSize, row * pixelSize, pixelSize, pixelSize);
            }
        }
    }
}
