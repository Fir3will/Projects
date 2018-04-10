/**************************************************************************
 *
 * [2017] Fir3will, All Rights Reserved.
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
package main.neuralnetwork;

import com.hk.math.Rand;

public class Neuron
{
	public final boolean rawInput;
	private final float[] weights;
	private float bias;
	public final Network network;
	
	public Neuron(Network network, int n, boolean isSize)
	{
		this.network = network;
		rawInput = !isSize;
		if(isSize)
		{
			weights = new float[n];
			for(int i = 0; i < n; i++)
			{
				weights[i] = Rand.nextFloat() * 2 - 1;
			}
			bias = Rand.nextFloat() * 2 - 1;
		}
		else
		{
			weights = new float[256];
			weights[n] = 1F;
			bias = 0F;
		}
	}
	
	public void tweak(float direction, float[] input, float[] nc)
	{
		if(!rawInput)
		{
			for(int i = 0; i < input.length; i++)
			{
				weights[i] += input[i] / direction;
				nc[i] += input[i] - weights[i];
			}
			bias += direction / 2F;
		}
	}
	
	public float getActivation(float[] input)
	{
		float sum = 0F;
		for(int i = 0; i < weights.length; i++)
		{
			sum += weights[i] * input[i];
		}
		return rawInput ? sum : Network.sig(sum + bias);
	}
}
