package main.neural;

import libsvm.*;

import java.util.Vector;

public class SVM {
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

    public svm_model trainSystem(double[][] input, double[][] expected, int numberOfInputNeurons, int numberOfOutputNeurons) {
        Vector<svm_node[]> svmInputs = new Vector<svm_node[]>();
        Vector<Double> vy = new Vector<Double>();

        for (int i = 0; i < input.length; i++) {
            svm_node[] node = new svm_node[numberOfInputNeurons];
            // createTrainingData();
            double label = getMaxIndex(expected[i]);
            double[] currentInputs = input[i];
            for (int j = 0; j < numberOfInputNeurons; j++) {
                node[j] = new svm_node();
                node[j].index = j;
                node[j].value = currentInputs[j];
            }
            vy.add(label);
            svmInputs.add(node);

        }

        svm_parameter param = new svm_parameter();
        // default values
        param.svm_type = svm_parameter.NU_SVR;
        param.kernel_type = svm_parameter.POLY;
        param.degree = 3;
        param.gamma = 0.5;    // 1/num_features
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 100;
        param.C = 100;
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];

        svm_problem prob = new svm_problem();
        prob.l = vy.size();
        prob.x = new svm_node[prob.l][];
        for (int i = 0; i < prob.l; i++)
            prob.x[i] = svmInputs.elementAt(i);
        prob.y = new double[prob.l];
        for (int i = 0; i < prob.l; i++)
            prob.y[i] = vy.elementAt(i);

        return svm.svm_train(prob, param);
    }
}
