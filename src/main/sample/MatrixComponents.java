package main.sample;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class MatrixComponents {
    int white = new Color(255, 255, 255).getRGB();
    int counter = 0;
    public MatrixComponents() {
    }

    public boolean isWhite(BufferedImage image, int posX, int posY) {
        int color = image.getRGB(posX, posY);
        if (color == white) {
            return true;
        } else {
            return false;
        }
    }


    public BufferedImage getHand(BufferedImage bimg) {
        int maxPixelCount = 0;
        BufferedImage finalImage = null;
        boolean[][] painted =
                new boolean[bimg.getHeight()][bimg.getWidth()];

        for (int i = 0; i < bimg.getHeight(); i++) {
            for (int j = 0; j < bimg.getWidth(); j++) {
                if (isWhite(bimg, j, i) && !painted[i][j]) {
                    Queue<Point> queue = new LinkedList<Point>();
                    queue.add(new Point(j, i));

                    BufferedImage bi = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_RGB);
                    int pixelCount = 0;
                    int value = new Color(255, 255, 255).getRGB();
                    while (!queue.isEmpty()) {
                        Point p = queue.remove();

                        if ((p.x >= 0) && (p.x < bimg.getWidth() && (p.y >= 0) && (p.y < bimg.getHeight()))) {
                            if (!painted[p.y][p.x] && isWhite(bimg, p.x, p.y)) {
                                painted[p.y][p.x] = true;
                                pixelCount++;
                                bi.setRGB(p.x, p.y, value);
                                queue.add(new Point(p.x + 1, p.y));
                                queue.add(new Point(p.x - 1, p.y));
                                queue.add(new Point(p.x, p.y + 1));
                                queue.add(new Point(p.x, p.y - 1));
                            }
                        }
                    }
                    counter++;
                    if (pixelCount > maxPixelCount) {
                        maxPixelCount = pixelCount;
                        finalImage = bi;
                    }
                }
            }
        }
        return finalImage;
    }
}
