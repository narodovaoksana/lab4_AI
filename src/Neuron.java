class Neuron {
    private double[][] weights;
    private double[] thresholds;
    private int numInputs;
    private int numOutputs;
    private double learningRate = 0.2;

    public Neuron(int numInputs) {
        this.numInputs = numInputs;
        this.numOutputs = 26;
        weights = new double[numOutputs][numInputs];
        thresholds = new double[numOutputs];

        for (int i = 0; i < numOutputs; i++) {
            thresholds[i] = Math.random() * 0.5;
            for (int j = 0; j < numInputs; j++) {
                weights[i][j] = Math.random() * 0.5;
            }
        }
    }

    private double sigmoid(double s) {
        return 1 / (1 + Math.exp(-s));
    }

    private double sigmoidDerivative(double output) {
        return output * (1 - output);
    }

    public void train(int[] input, int[] desiredOutput) {
        double[] actualOutput = calculateOutput(input);
        for (int i = 0; i < numOutputs; i++) {
            double error = desiredOutput[i] - actualOutput[i];
            for (int j = 0; j < numInputs; j++) {
                weights[i][j] += learningRate * error * sigmoidDerivative(actualOutput[i]) * input[j];
            }
            thresholds[i] -= learningRate * error * sigmoidDerivative(actualOutput[i]);
        }
    }

    private double[] calculateOutput(int[] input) {
        double[] outputs = new double[numOutputs];
        for (int i = 0; i < numOutputs; i++) {
            double sum = 0;
            for (int j = 0; j < numInputs; j++) {
                sum += input[j] * weights[i][j];
            }
            outputs[i] = sigmoid(sum - thresholds[i]);
        }
        return outputs;
    }

    public double[] classify(int[] input) {
        return calculateOutput(input);
    }
}
