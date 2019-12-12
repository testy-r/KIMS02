package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Neuron {
	
	protected double value;

	private List<Synapse> inputSynapses;
	private List<Synapse> outputSynapses;
	private double bias;
	private double biasWeight;
	private double error;
	private double gradient;
	
	private Random random = new Random();
	
	
	protected Neuron() {
		
		this.outputSynapses = new ArrayList<Synapse>();
		this.bias = random.nextDouble();
		this.biasWeight = random.nextDouble();
	}
	
	protected Neuron(List<Neuron> inputNeuron) {
		
		this.inputSynapses = new ArrayList<Synapse>();
		this.outputSynapses = new ArrayList<Synapse>();
		this.bias = random.nextDouble();
		this.biasWeight = random.nextDouble();
		
		for(Neuron neuron : inputNeuron) {
			
			Synapse synapse = new Synapse(neuron, this);
			inputSynapses.add(synapse);
			neuron.outputSynapses.add(synapse);
		}
	}
	
	protected void updateValue() {
		
		double sum = 0.0;
		
		for(Synapse synapse : inputSynapses) {
			sum += synapse.weight * synapse.inputNeuron.value;
		}

		sum += this.bias * this.biasWeight;

		value = sigmoid((sum/inputSynapses.size()));		
	}
	
	private void updateError() {
		
		this.error = 0.0;
		
		for(Synapse synapse : outputSynapses) {
			this.error += synapse.weight * synapse.outputNeuron.error;
		}
	}
	
	private void updateError(double target) {
		this.error = target - this.value;
	}
	
	protected void updateGradient() {
		
		updateError();
		
		this.gradient = -this.error * this.value * (1 - this.value);
	}
	
	protected void updateGradient(double target) {
		
		updateError(target);
		
		this.gradient = -this.error * this.value * (1 - this.value);
	}
	
	protected void updateWeight(double learningRate) {
		
		for(Synapse synapse : inputSynapses) {
			synapse.weight = synapse.weight - learningRate * this.gradient * synapse.inputNeuron.value;
		}

		this.biasWeight += learningRate * this.gradient * this.bias;
	}
	
	private double sigmoid(double input) {
		return 1.0/(1.0+Math.exp(-input));
	}
	
	private class Synapse {
		
		private Neuron inputNeuron;
		private Neuron outputNeuron;
		private double weight;
		
		
		private Synapse(Neuron inputNeuron, Neuron outputNeuron) {
			
			this.inputNeuron = inputNeuron;
			this.outputNeuron = outputNeuron;
			this.weight = random.nextDouble();
		}
	}

}
