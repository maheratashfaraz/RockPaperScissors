package main.neural;

import org.encog.engine.network.activation.ActivationElliott;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.scg.ScaledConjugateGradient;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;

public class NeuralNetwork {

    public BasicNetwork trainSystem(double[][] input, double[][] expected, int numberOfInputNeurons, int numberOfOutputNeurons) {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, numberOfInputNeurons));
        network.addLayer(new BasicLayer(new ActivationElliott(), true, 100));
        network.addLayer(new BasicLayer(new ActivationElliott(), false, numberOfOutputNeurons));
        network.getStructure().finalizeStructure();
        network.reset(123456);

        MLDataSet trainingSet = new BasicMLDataSet(input, expected);
        final ScaledConjugateGradient train = new ScaledConjugateGradient(network, trainingSet);
        int epoch = 1;
        while (epoch < 600) {
            train.iteration();
            Main.compute(network);
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;
        }
        train.finishTraining();
        System.out.println("Saving network");
        EncogDirectoryPersistence.saveObject(new File("rockpaperscissors.eg"), network);
        return network;
    }
}
