/**************************************************************************
 *
 * [2019] Fir3will, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of "Fir3will" and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to "Fir3will" and its suppliers
 * and may be covered by U.S. and Foreign Patents, patents
 * in process, and are protected by trade secret or copyright law
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from "Fir3will".
 **************************************************************************/
package com.hk.neuralnetwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.hk.io.stream.InStream;
import com.hk.io.stream.OutStream;
import com.hk.io.stream.Stream;
import com.hk.io.stream.StreamException;
import com.hk.math.Rand;
import com.hk.neuralnetwork.Mat.MatFunc;

public class NeuralNetwork implements Cloneable
{
	public final int inputNodes;
	public final int hiddenLayers, hiddenNodes;
	public final int outputNodes;
	public final Mat[] weights, biases;
	private ActivationFunction activationFunction;
	private double learningRate;
	
	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes)
	{
		this(inputNodes, 1, hiddenNodes, outputNodes);
	}
	
	public NeuralNetwork(int inputNodes, int hiddenLayers, int hiddenNodes, int outputNodes)
	{
		this.inputNodes = inputNodes;
		this.hiddenLayers = hiddenLayers;
		this.hiddenNodes = hiddenNodes;
		this.outputNodes = outputNodes;
		
		weights = new Mat[hiddenLayers + 1];
		for(int i = 0; i < hiddenLayers + 1; i++)
		{
			if(i == 0)
			{
				weights[i] = new Mat(hiddenNodes, inputNodes);
			}
			else if(i == hiddenLayers)
			{
				weights[i] = new Mat(outputNodes, hiddenNodes);
			}
			else
			{
				weights[i] = new Mat(hiddenNodes, hiddenNodes);
			}
			
			weights[i].randomize();
		}
		
		biases = new Mat[hiddenLayers + 1];
		for(int i = 0; i < hiddenLayers + 1; i++)
		{
			if(i == hiddenLayers)
			{
				biases[i] = new Mat(outputNodes, 1);
			}
			else
			{
				biases[i] = new Mat(hiddenNodes, 1);
			}

			biases[i].randomize();
		}
		
		learningRate = 0.01;
		activationFunction = new ActivationFunction(Mat.SIGMOID, Mat.SIGMOID_DERIVATIVE);
	}
	
	public NeuralNetwork(NeuralNetwork copy)
	{
		this.inputNodes = copy.inputNodes;
		this.hiddenLayers = copy.hiddenLayers;
		this.hiddenNodes = copy.hiddenNodes;
		this.outputNodes = copy.outputNodes;

		weights = new Mat[copy.weights.length];
		for(int i = 0; i < weights.length; i++)
		{
			weights[i] = copy.weights[i].clone();
		}
		
		biases = new Mat[copy.biases.length];
		for(int i = 0; i < biases.length; i++)
		{
			biases[i] = copy.biases[i].clone();
		}
		learningRate = copy.learningRate;
		activationFunction = copy.activationFunction;
	}
	
	public double[] process(double[] inputArray)
	{
		if(inputArray.length != inputNodes) throw new RuntimeException("Input must have " + inputNodes + " element" + (inputNodes == 1 ? "" : "s"));
		
		Mat input = Mat.fromArray(inputArray);

//		System.out.println(input.schtuff());
		for(int i = 1; i < hiddenLayers + 2; i++)
		{
			input = weights[i - 1].mult(input).add(biases[i - 1]).map(activationFunction.function);
//			System.out.println(StringUtil.repeat("\t", i) + input.schtuff());
		}
		
		return input.toArray();
	}
	
	public void train(double[] inputArray, double[] correct)
	{
		if(inputArray.length != inputNodes) throw new RuntimeException("Input must have " + inputNodes + " element" + (inputNodes == 1 ? "" : "s"));
		if(correct.length != outputNodes) throw new RuntimeException("Output must have " + outputNodes + " element" + (outputNodes == 1 ? "" : "s"));

		Mat input = Mat.fromArray(inputArray);
		Mat[] layers = new Mat[hiddenLayers + 2];
		layers[0] = input;

		for(int i = 1; i < hiddenLayers + 2; i++)
		{
			input = weights[i - 1].mult(input).add(biases[i - 1]).map(activationFunction.function);
			layers[i] = input;
		}
		
		Mat target = Mat.fromArray(correct);
		for(int i = hiddenLayers + 1; i > 0; i--)
		{
			// Calculate Error
			Mat error = target.subtract(layers[i]);
			
			// Calculate Gradient
			Mat gradient = layers[i].map(activationFunction.derivative);
			gradient = gradient.elementMult(error);
			gradient = gradient.mult(learningRate);
			
			// Calculate Delta
			Mat delta = gradient.mult(layers[i - 1].transpose());
			
			// Adjust weights and biases
			biases[i - 1] = biases[i - 1].add(gradient);
			weights[i - 1] = weights[i - 1].add(delta);
		
			// Reset target for next loop
			target = weights[i - 1].transpose().mult(error).add(layers[i - 1]);
		}
	}
	
	public double getLearningRate()
	{
		return learningRate;
	}
	
	public NeuralNetwork setLearningRate(double learningRate)
	{
		this.learningRate = learningRate;
		return this;
	}
	
	public ActivationFunction getActivationFunction()
	{
		return activationFunction;
	}
	
	public NeuralNetwork setActivationFunction(ActivationFunction activationFunction)
	{
		this.activationFunction = activationFunction;
		return this;
	}
	
	public NeuralNetwork mix(final NeuralNetwork other, boolean weights, boolean biases)
	{
		if(inputNodes != other.inputNodes || hiddenLayers != other.hiddenLayers || hiddenNodes != other.hiddenNodes || outputNodes != other.outputNodes)
		{
			throw new IllegalArgumentException("These neural networks aren't compatible");
		}
		
		NeuralNetwork nn = clone();
		if(weights)
		{
			for(int i = 0; i < hiddenLayers + 1; i++)
			{
				Mat weight = nn.weights[i];
				
				final int indx = i;
				weight.map(new MatFunc()
				{
					@Override
					public double perform(double val, int r, int c)
					{
						return Rand.nextBoolean() ? val : other.weights[indx].data[r][c];
					}
				});
			}
		}
		if(biases)
		{
			for(int i = 0; i < hiddenLayers + 1; i++)
			{
				Mat bias = nn.biases[i];
				
				final int indx = i;
				bias.map(new MatFunc()
				{
					@Override
					public double perform(double val, int r, int c)
					{
						return Rand.nextBoolean() ? val : other.biases[indx].data[r][c];
					}
				});
			}
		}
		return nn;
	}
	
	public NeuralNetwork mutateThis(final double chance)
	{
		for(int i = 0; i < weights.length; i++)
		{
			weights[i].map(new MatFunc()
			{
				@Override
				public double perform(double val, int r, int c)
				{
					return Rand.nextDouble() < chance ? val + (Rand.nextDouble(2) - 1) / 10 : val;
				}
			});
		}
		return this;
	}
	
	public NeuralNetwork clone()
	{
		return new NeuralNetwork(this);
	}
	
	public void writeTo(File file) throws Exception
	{
		writeTo(file, true);
	}
	
	public void writeTo(File file, boolean safe) throws Exception
	{
		FileOutputStream fout = new FileOutputStream(file);
		Stream out = new OutStream(fout, safe);
		writeTo(out);
		out.close();
		fout.close();
	}
	
	public void writeTo(Stream out) throws StreamException
	{
		for(int i = 0; i < weights.length; i++)
		{
			Mat weight = weights[i];
			
			for(int x = 0; x < weight.rows; x++)
			{
				for(int y = 0; y < weight.cols; y++)
				{
					out.writeDouble(weight.data[x][y]);
				}
			}
		}
		for(int i = 0; i < biases.length; i++)
		{
			Mat bias = biases[i];
			
			for(int x = 0; x < bias.rows; x++)
			{
				for(int y = 0; y < bias.cols; y++)
				{
					out.writeDouble(bias.data[x][y]);
				}
			}
		}
	}

	public void readFrom(File file) throws Exception
	{
		readFrom(file, true);
	}
	
	public void readFrom(File file, boolean safe) throws Exception
	{
		FileInputStream fin = new FileInputStream(file);
		Stream in = new InStream(fin, safe);
		readFrom(in);
		in.close();
		fin.close();
	}
	
	public void readFrom(Stream in) throws StreamException
	{
		for(int i = 0; i < weights.length; i++)
		{
			Mat weight = weights[i];
			
			for(int x = 0; x < weight.rows; x++)
			{
				for(int y = 0; y < weight.cols; y++)
				{
					weight.data[x][y] = in.readDouble();
				}
			}
		}
		for(int i = 0; i < biases.length; i++)
		{
			Mat bias = biases[i];
			
			for(int x = 0; x < bias.rows; x++)
			{
				for(int y = 0; y < bias.cols; y++)
				{
					bias.data[x][y] = in.readDouble();
				}
			}
		}
	}
	
	public static class ActivationFunction
	{
		public final MatFunc function, derivative;
		
		public ActivationFunction(MatFunc function, MatFunc derivative)
		{
			this.function = function;
			this.derivative = derivative;
		}
	}
}
