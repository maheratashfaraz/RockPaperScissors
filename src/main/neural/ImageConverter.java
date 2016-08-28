package main.neural;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter {
    Thresholds thresholds;
    int counter = 0;

    public ImageConverter(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    public double[] convertImage(BufferedImage image, int width, int height) {
        Image pgimg = resizeImageIcon(image, width, height);
        return getArray(toBufferedImage(pgimg));
    }

    public double[] getArray(BufferedImage image) {
        RGBHolder rgbHolder = getRGBValuesFromBlackAndWhite(image);
        return getFinalSingleArray(image.getWidth(), image.getHeight(), rgbHolder.binaryValues);
    }

    //convert two-d array to to one-d flattenedRgb Values
    public double[] getFinalSingleArray(int imageWidth, int imageHeight, double[][] binary) {
        int increment = 0;
        int n = imageHeight * imageWidth;
        double[] flattenedRGB = new double[n];

        for (int width = 0; width < imageWidth; width++) {
            for (int height = 0; height < imageHeight; height++) {

                flattenedRGB[increment] = (binary[width][height]);
                increment++;
            }
        }
        return flattenedRGB;
    }

    private BufferedImage convertBinaryArrayToImage(double[][] binaryImage) {
        int height = binaryImage.length;
        int width = binaryImage.length;
        BufferedImage resultInImage =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int value;
                if (binaryImage[x][y] == 1) {
                    value = new Color(255, 255, 255).getRGB();
                    resultInImage.setRGB(x, y, value);
                } else {
                    value = new Color(0, 0, 0).getRGB();
                    resultInImage.setRGB(x, y, 0);
                }
            }
        }
        return resultInImage;
    }

    private void saveBlackAndWhiteImage(BufferedImage image, double[][] redValues, double[][] greenValues, double[][] blueValues) {
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage resultInImage =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int value = new Color((int) redValues[x][y], (int) greenValues[x][y], (int) blueValues[x][y]).getRGB();
                resultInImage.setRGB(y, x, value);
            }
        }
    }


    private double[][] erode(double[][] image) {
        for (int i = image.length / 2; i < image.length; i++) {
            for (int j = image.length / 2; j < image[i].length; j++) {
                if (image[i][j] == 0) {
                    if (i > image.length / 2 && image[i - 1][j] == 1) image[i - 1][j] = 2;
                    if (j > image.length / 2 && image[i][j - 1] == 1) image[i][j - 1] = 2;
                    if (i + 1 < image.length && image[i + 1][j] == 1) image[i + 1][j] = 2;
                    if (j + 1 < image[i].length && image[i][j + 1] == 1) image[i][j + 1] = 2;
                }
            }
        }
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == 2) {
                    image[i][j] = 0;
                }
            }
        }
        return image;
    }


    // Best dilate by one solution
    private double[][] dilate(double[][] image) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == 1) {
                    if (i > 0 && image[i - 1][j] == 0) image[i - 1][j] = 2;
                    if (j > 0 && image[i][j - 1] == 0) image[i][j - 1] = 2;
                    if (i + 1 < image.length && image[i + 1][j] == 0) image[i + 1][j] = 2;
                    if (j + 1 < image[i].length && image[i][j + 1] == 0) image[i][j + 1] = 2;
                }
            }
        }
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == 2) {
                    image[i][j] = 1;
                }
            }
        }
        return image;
    }


    private BufferedImage imageRotation(double degree, BufferedImage img) {
        AffineTransform tx = new AffineTransform();
        tx.rotate(degree, img.getWidth() / 2, img.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_BILINEAR);
        return img = op.filter(img, null);
    }

    private boolean withinHueThreshold(float hueValue) {

        return (thresholds.hueLowerBoundLowerThreshold <= hueValue && hueValue <= thresholds.hueLowerBoundUpperThreshold)
                || (thresholds.hueUpperBoundLowerThreshold <= hueValue && hueValue <= thresholds.hueUpperBoundUpperThreshold);
    }

    public Image resizeImageIcon(Image img, int i, int b) {
        Image newimg = img.getScaledInstance(i, b,
                java.awt.Image.SCALE_SMOOTH); // scale it the  smoothed manner
        return newimg;
    }

    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    public BufferedImage getBlackAndWhite(BufferedImage image) {
        RGBHolder rgbHolder = getRGBValues(image);
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage resultInImage =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int value = new Color((int) rgbHolder.redValues[x][y], (int) rgbHolder.greenValues[x][y], (int) rgbHolder.blueValues[x][y]).getRGB();
                resultInImage.setRGB(x, y, value);
            }
        }
        return resultInImage;
    }

    public RGBHolder getRGBValues(BufferedImage image) {
        double[][] redValues = new double[image.getWidth()][image.getHeight()];
        double[][] greenValues = new double[image.getWidth()][image.getHeight()];
        double[][] blueValues = new double[image.getWidth()][image.getHeight()];
        double[][] binaryValues = new double[image.getWidth()][image.getHeight()];

        for (int width = 0; width < image.getWidth(); width++) {
            for (int height = 0; height < image.getHeight(); height++) {
                Color rgb = new Color(image.getRGB(width, height));
                redValues[width][height] = rgb.getRed();
                greenValues[width][height] = rgb.getBlue();
                blueValues[width][height] = rgb.getGreen();
                float[] hsv = new float[3];
                Color.RGBtoHSB(rgb.getRed(), rgb.getBlue(), rgb.getGreen(), hsv);
                if (withinHueThreshold(hsv[0])
                        && thresholds.saturationLowerThreshold <= hsv[1] && hsv[1] <= thresholds.saturationUpperThreshold) {
                    redValues[width][height] = 255;
                    greenValues[width][height] = 255;
                    blueValues[width][height] = 255;
                    binaryValues[width][height] = 1;
                } else {
                    redValues[width][height] = 0;
                    greenValues[width][height] = 0;
                    blueValues[width][height] = 0;
                    binaryValues[width][height] = 0;
                }
            }
        }
        return new RGBHolder(redValues, greenValues, blueValues, binaryValues);
    }

    public RGBHolder getRGBValuesFromBlackAndWhite(BufferedImage image) {
        double[][] redValues = new double[image.getWidth()][image.getHeight()];
        double[][] greenValues = new double[image.getWidth()][image.getHeight()];
        double[][] blueValues = new double[image.getWidth()][image.getHeight()];
        double[][] binaryValues = new double[image.getWidth()][image.getHeight()];
        for (int width = 0; width < image.getWidth(); width++) {
            for (int height = 0; height < image.getHeight(); height++) {
                Color rgb = new Color(image.getRGB(width, height));
                redValues[width][height] = rgb.getRed();
                greenValues[width][height] = rgb.getBlue();
                blueValues[width][height] = rgb.getGreen();
                if (rgb.getRed() == 255) {
                    binaryValues[width][height] = 1;
                } else {
                    binaryValues[width][height] = 0;
                }
            }
        }
        return new RGBHolder(redValues, greenValues, blueValues, binaryValues);
    }

    public class RGBHolder {
        double[][] redValues;
        double[][] greenValues;
        double[][] blueValues;
        double[][] binaryValues;

        public RGBHolder(double[][] redValues, double[][] greenValues, double[][] blueValues, double[][] binaryValues) {
            this.redValues = redValues;
            this.greenValues = greenValues;
            this.blueValues = blueValues;
            this.binaryValues = binaryValues;
        }
    }
}
