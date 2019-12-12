package app;

import java.util.ArrayList;
import java.util.List;

import util.ImageLoader;
import util.DataSet;

public class NeuralNet {
	
	private static ImageLoader imageLoader = new ImageLoader();
	
	private static List<DataSet> database;
	private static List<DataSet> testbase;
	
	private List<Neuron> input;
	private List<List<Neuron>> hidden;
	private List<Neuron> output;
	
	private double learningRate;
	
	
	public NeuralNet(int inputSize, int hiddenLayers, int hiddenSize, int outputSize, double learningRate) {
		
		this.learningRate = learningRate;

		//get trainingsdata
		
		if(database == null) {
			database = imageLoader.buildDatabase("C:/Users/Rene/source/KIP2/src/data/traindata/");
		}
		if(testbase == null) {
			testbase = imageLoader.buildDatabase("C:/Users/Rene/source/KIP2/src/data/testdata");
		}
		
		//Build Input Neurons
		this.input = new ArrayList<Neuron>();
		for(int i = 0; i < inputSize; i++) {
			this.input.add(new Neuron());
		}

		//Build Hidden Neurons
		this.hidden = new ArrayList<List<Neuron>>();
		for(int i = 0; i < hiddenLayers; i++) {
			this.hidden.add(new ArrayList<Neuron>());
			
			for(int j = 0; j < hiddenSize; j++) {
				this.hidden.get(i).add(new Neuron(i == 0 ? this.input : this.hidden.get(i - 1)));
			}
		}

		//Build Output Neurons
		this.output = new ArrayList<Neuron>();
		for(int i = 0; i < outputSize; i++) {
			this.output.add(new Neuron(hiddenLayers == 0 ? this.input : this.hidden.get(hiddenLayers - 1)));
		}
	}
	
	public void train(int epochs) {	
		for(int i = 0; i < epochs; i++) {
			for(DataSet dataset : database) {
				forwardpropagate(dataset.values);
				backpropagate(dataset.targets);
			}
		}
	}
	
	//Input Layers filled in with the Data
	public void fillInputLayer(List<Double> values) {
		for(int i = 0; i < this.input.size(); i++) {
			this.input.get(i).value = values.get(i);
		}
	}
	
	public void forwardpropagate(List<Double> values) {
		
		fillInputLayer(values);
		
		for(List<Neuron> layer : this.hidden) {
			for(Neuron neuron : layer) {
				neuron.updateValue();
			}
		}
		
		for(Neuron neuron : this.output) {
			neuron.updateValue();
		}
	}
	
	public void backpropagate(double[] targets) {
		
		for(int i = 0; i < this.output.size(); i++) {
			this.output.get(i).updateGradient(targets[i]);
		}
		for(int i = this.hidden.size() - 1; i >= 0; i--) {
			for(Neuron neuron : this.hidden.get(i)) {
				neuron.updateGradient();
			}
		}
		for(List<Neuron> layer : this.hidden) {
			for(Neuron neuron : layer) {
				neuron.updateWeight(this.learningRate);
			}
		}
		for(Neuron neuron : this.output) {
			neuron.updateWeight(this.learningRate);
		}
	}
	
	public void test() {
		
		int correct = 0;
		int total = 0;
		
		for(DataSet dataset : testbase) {
			forwardpropagate(dataset.values);
			
			String result = "";
			
			switch(getResult()) {
			case 0: result = "Chevron"; break;
			case 1: result = "EinSieben"; break;
			case 2: result = "Kreis"; break;
			case 3: result = "Kreuz"; break;
			case 4: result = "Strich"; break;
			}
			
			System.out.println("Ergebnis: " + result);
			System.out.println("Tatsaechlich: " + dataset.type);
			System.out.println();
			
			if(result.equals(dataset.type)) correct++;
			total++;
		}
		
		System.out.println("Verhaeltnis Richtige/Gesamt: " + correct + "/" + total);
	}
	
	public int getResult() {
		
		int index = 0;
		double max = 0.0;
		
		for(int i = 0; i < this.output.size(); i++) {
			if(this.output.get(i).value > max) {
				index = i;
				max = this.output.get(i).value;
			}
			System.out.println(this.output.get(i).value);
		}
		
		return index;
	}
	
}
