package app;

public class Main {

	public static void main(String[] args) {
		
		final double learningRate = 1;

		// NeuralNet hiddenLayerNN = new NeuralNet(25*25, 1, 30, 5, learningRate);
		// hiddenLayerNN.train(1000);
		// hiddenLayerNN.test();

		NeuralNet perceptronNN = new NeuralNet(25*25, 0, 0, 5, learningRate);
		perceptronNN.train(1000);
		perceptronNN.test();

	}

}