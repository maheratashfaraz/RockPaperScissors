package main.neural;

import java.awt.image.BufferedImage;

class RGB {
    double[][] redValues;
    double[][] greenValues;
    double[][] blueValues;
    BufferedImage image;

    public RGB(double[][] redValues, double[][] greenValues, double[][] blueValues, BufferedImage image) {
        this.redValues = redValues;
        this.greenValues = greenValues;
        this.blueValues = blueValues;
        this.image = image;
    }

    //convert two-d array to to one-d flattenedRgb Values
    public double[] getFinalSingleArray() {
        int increment = 0;
        int firstPosition = image.getHeight() * image.getWidth();
        int secondPosition = image.getHeight() * image.getWidth() * 2;
        int n = image.getHeight() * image.getWidth() * 3;
        double[] flattenedRGB = new double[n];
        for (int width = 0; width < image.getHeight(); width++) {
            for (int height = 0; height < image.getWidth(); height++) {
                flattenedRGB[increment] = redValues[width][height];
                flattenedRGB[firstPosition + increment] = greenValues[width][height];
                flattenedRGB[secondPosition + increment] = blueValues[width][height];
                increment++;
            }

        }
        return flattenedRGB;
    }
}

