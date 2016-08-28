package main.neural;

public class Thresholds {

    double hueLowerBoundLowerThreshold;
    double hueLowerBoundUpperThreshold;
    double hueUpperBoundLowerThreshold;
    double hueUpperBoundUpperThreshold;
    double saturationLowerThreshold;
    double saturationUpperThreshold;

    public Thresholds(double hueLowerBoundLowerThreshold,
                      double hueLowerBoundUpperThreshold,
                      double hueUpperBoundLowerThreshold,
                      double hueUpperBoundUpperThreshold,
                      double saturationLowerThreshold,
                      double saturationUpperThreshold) {
        this.hueLowerBoundLowerThreshold = hueLowerBoundLowerThreshold;
        this.hueLowerBoundUpperThreshold = hueLowerBoundUpperThreshold;
        this.hueUpperBoundLowerThreshold = hueUpperBoundLowerThreshold;
        this.hueUpperBoundUpperThreshold = hueUpperBoundUpperThreshold;
        this.saturationLowerThreshold = saturationLowerThreshold;
        this.saturationUpperThreshold = saturationUpperThreshold;
    }
}
