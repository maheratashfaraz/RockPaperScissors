package main.neural;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Maher on 15/10/2015.
 */
public class PictureAccessor {
    Thresholds thresholds;
    ImageConverter imageConverter;

    public PictureAccessor(Thresholds thresholds) {
        this.thresholds = thresholds;
        this.imageConverter = new ImageConverter(thresholds);
    }

    public TrainingData getTrainingData(String folderPath, int numberOfPixels, int numberOfOutputs, HashMap<String, double[]> map,
                                        int width, int height) {

        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        int numberOfTrainingSamples = listOfFiles.length;
        double[][] input = new double[numberOfTrainingSamples][numberOfPixels];
        double[][] output = new double[numberOfTrainingSamples][numberOfOutputs];
        System.out.println("creating input data");
        for (int f = 0; f < numberOfTrainingSamples; f++) {
            input[f] = convertToInputArray(listOfFiles[f], width, height);
            output[f] = getOutputArray(listOfFiles[f], map);
            System.out.println("created input data: " + f + " of " + numberOfTrainingSamples);

        }

        System.out.println("finished creating input data");
        return new TrainingData(input, output);
    }

    private double[] getOutputArray(File file, HashMap<String, double[]> map) {
        return map.get(file.getName().split("_")[0]);
    }


    public double[] convertToInputArray(BufferedImage image, int width, int height) {
        return imageConverter.convertImage(image, width, height);
    }

    public double[] convertToInputArray(File file, int width, int height) {
        BufferedImage image = convertFileToImage(file);
        return imageConverter.convertImage(image, width, height);
    }

    public BufferedImage convertFileToImage(File file) {

        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;

    }

    class PredictData {
        double[] data;
        String predictionFileName;
        public PredictData(double[] data, String predictionFileName) {
            this.data = data;
            this.predictionFileName = predictionFileName;
        }
    }

    public PredictData[] getPredictionData(String predictionFolderPath, int numberOfPixels, int width, int height) {
        File folder = new File(predictionFolderPath);
        File[] listOfFiles = folder.listFiles();
        int numberOfTrainingSamples = listOfFiles.length;
        PredictData[] predictData = new PredictData[numberOfTrainingSamples];

        for (int f = 0; f < numberOfTrainingSamples; f++) {
            predictData[f] = new PredictData(convertToInputArray(listOfFiles[f], width, height), listOfFiles[f].getName());
        }
        return predictData;
    }

    public class TrainingData {
        double[][] input;
        double[][] output;

        public TrainingData(double[][] input, double[][] output) {
            this.input = input;
            this.output = output;
        }


    }


}
