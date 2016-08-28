package main.neural;
import com.github.sarxos.webcam.Webcam;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Main {
    // Directories to training and validation folder images
    static String trainingFolder = "C:\\Users\\Maher\\Desktop\\Dataset\\trainingfolder";
    static String predictionFolderPath = "C:\\Users\\Maher\\Desktop\\Dataset\\testfolder";

    static HashMap<String, double[]> map = new HashMap<>();
    static HashMap<Integer, String> reverseMap = new HashMap<>();
    static Thresholds thresholds = new Thresholds(0.0055, 0.1083, 0.833, 0.997, 0.1, 0.8);
    static PictureAccessor pictureAccessor = new PictureAccessor(thresholds);
    static int width = 60;
    static int height = 60;
    static int numberOfPixels = width * height;
    static int numberOfOutputs = 3;

    public static void main(String[] args) throws IOException {
        // Create maps for sparse vectors
        map.put("paper", new double[]{1, 0, 0});
        map.put("rock", new double[]{0, 1, 0});
        map.put("scissors", new double[]{0, 0, 1});
        reverseMap.put(0, "paper");
        reverseMap.put(1, "rock");
        reverseMap.put(2, "scissors");

        PictureAccessor.TrainingData trainingData = pictureAccessor.getTrainingData(trainingFolder, numberOfPixels, numberOfOutputs, map, width, height);
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        BasicNetwork basicNetwork = neuralNetwork.trainSystem(trainingData.input, trainingData.output, numberOfPixels, numberOfOutputs);
        compute(basicNetwork);
    }

    public static void compute(BasicNetwork basicNetwork) {
        PictureAccessor.PredictData[] predictData = pictureAccessor.getPredictionData(predictionFolderPath, numberOfPixels, width, height);
        int correctPredictions = 0;
        for (PictureAccessor.PredictData predict : predictData) {
            String predictedAction = reverseMap.get(getMaxIndex(basicNetwork.compute(new BasicMLData(predict.data)).getData()));
            if (predict.predictionFileName.split("_")[0].equals(predictedAction)) {
                correctPredictions++;
            }
        }
        double accuracy = ((double) correctPredictions / predictData.length) * 100;
        System.out.println("Accuracy: " + accuracy);
    }

    private static int getMaxIndex(double[] data) {
        int maxIndex = 0;
        for (int i = 1; i < data.length; i++) {
            double newnumber = data[i];
            if ((newnumber > data[maxIndex])) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static void createTrainingData() {
        try {
            int counter = 0;
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String line = buffer.readLine();
                Webcam webcam = Webcam.getDefault();
                webcam.setViewSize(new Dimension(320, 240));
                webcam.open();
                BufferedImage image = webcam.getImage();
                webcam.close();
                ImageIO.write(image, "PNG", new File("training_data/" + "test_" + counter + ".png"));
                counter++;
                System.out.println("created picture");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
