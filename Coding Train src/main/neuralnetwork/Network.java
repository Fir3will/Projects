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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.hk.array.ArrayUtil;
import com.hk.math.Rand;

public class Network
{
	public final float[][] weights;
	public final Neuron[][] neuronLayers;
	
	public Network()
	{
		weights = new float[16][16];
		neuronLayers = new Neuron[3][];
		neuronLayers[0] = new Neuron[256];
		neuronLayers[1] = new Neuron[16];
//		neuronLayers[2] = new Neuron[16];
		neuronLayers[2] = new Neuron[10];
		for(int i = 0; i < neuronLayers.length; i++)
		{
			for(int j = 0; j < neuronLayers[i].length; j++)
			{
				neuronLayers[i][j] = new Neuron(this, i == 0 ? j : neuronLayers[i - 1].length, i != 0);
			}
		}
	}
	
	public void train(int amt) throws Exception
	{
		List<Object[]> files = new ArrayList<>();
		for(int i = 0; i < 10; i++)
		{			
			File fd = new File("digits", String.valueOf(i));
			File[] lf = fd.listFiles();
			for(File f : lf)
			{
				files.add(new Object[] { f, i });
			}
		}
		Collections.shuffle(files);
		float[][][] maps = new float[files.size()][][];
		int[] digits = new int[files.size()];
		for(int i1 = 0; i1 < files.size(); i1++)
		{
			Object[] os = files.get(i1);
			digits[i1] = (int) os[1];
			FileInputStream fis = new FileInputStream((File) os[0]);
			byte[] bs = new byte[32];
			fis.read(bs);
			fis.close();
			BigInteger bi = new BigInteger(bs);
			boolean[][] drawing = new boolean[16][16];
			for(int x = 0; x < 16; x++)
			{
				for(int y = 0; y < 16; y++)
				{
					int indx = x + y * 16;
					drawing[x][y] = bi.testBit(indx);
				}
			}
			float[][] map = NeuralNetwork.generateMap(drawing);
			maps[i1] = map;
		}
		for(int i = 0; i < amt; i++)
		{
			float[][][] ms = Arrays.copyOf(maps, maps.length);
			System.out.println("Average Cost: " + train(ms, digits));
		}
	}
	
	private float train(float[][][] maps, int[] digits)
	{
        for (int i = 0; i < maps.length; i++)
        {
            ArrayUtil.swap(maps, i, i + Rand.nextInt(maps.length - i));
        }
        
        float[][][] results = new float[maps.length][][];
        float cost = 0F;
        for (int i = 0; i < maps.length; i++)
        {
        	results[i] = setWeights(maps[i]);
        	cost += cost(results[i], digits[i]);
        }
        cost /= maps.length;
        
        for (int i = 0; i < maps.length; i++)
        {
        	float[] ds = new float[10];
        	ds[digits[i]] = 1F;
            tweak(ds, results[i]);
        }
        
        return cost;
	}
	
	public float[][] setWeights(float[][] map)
	{
		float[] mp = new float[256];
		for(int i = 0; i < 256; i++)
		{
			int x = i % 16;
			int y = i / 16;
			mp[i] = map[x][y];
		}
		float[][] results = new float[neuronLayers.length][];
		for(int i = 0; i < neuronLayers.length; i++)
		{
			float[] rs = new float[neuronLayers[i].length];
			
			for(int j = 0; j < neuronLayers[i].length; j++)
			{
				rs[j] = neuronLayers[i][j].getActivation(mp);
			}
			
			mp = rs;
			results[i] = mp;
		}
		return results;
	}
	
	public void tweak(float[] desired, float[][] results)
	{
		float[] cost = new float[neuronLayers[neuronLayers.length - 1].length];
		for(int j = 0; j < neuronLayers[neuronLayers.length - 1].length; j++)
		{
			cost[j] = desired[j] - results[neuronLayers.length - 1][j];
		}
		
		for(int i = neuronLayers.length - 1; i > 0; i--)
		{
			System.out.println("On Layer #" + i);
			System.out.println("\t" + Arrays.toString(results[i - 1]));
			System.out.println("\t" + Arrays.toString(cost));
			float[] nc = new float[neuronLayers[i - 1].length];
			for(int j = 0; j < neuronLayers[i].length; j++)
			{
				neuronLayers[i][j].tweak(cost[j], results[i - 1], nc);
			}
			cost = nc;
		}
	}
	
	public float cost(float[][] results, int digit)
	{
		float[] actual = results[results.length - 1];
		float[] desired = new float[actual.length];
		desired[digit] = 1F;
		float cost = 0F;
		for(int i = 0; i < actual.length; i++)
		{
			float f = actual[i] - desired[i];
			cost += f * f;
		}
		return cost;
	}

	public void save(boolean[][] drawing, int currentNumber) throws Exception
	{
		BigInteger bi = new BigInteger("0");
		for(int x = 0; x < 16; x++)
		{
			for(int y = 0; y < 16; y++)
			{
				if(drawing[x][y])
				{
					int indx = x + y * 16;
					bi = bi.setBit(indx);
				}
			}
		}
		File fd = new File("digits", String.valueOf(currentNumber));
		File dg = new File(fd, "0x" + Long.toHexString(System.currentTimeMillis()).toUpperCase() + ".txt");
		if(dg.exists())
		{
			dg.delete();
		}
		dg.createNewFile();
		FileOutputStream fos = new FileOutputStream(dg);
		byte[] bs = bi.toByteArray();
		fos.write(bs);
		fos.close();
		System.out.println(Arrays.toString(bs));
	}
	
	public static float sig(float t)
	{
		return (float) (1D / (1D + Math.pow(Math.E, t)));
	}
}
