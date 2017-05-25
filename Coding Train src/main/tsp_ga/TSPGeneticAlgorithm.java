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
package main.tsp_ga;

import java.awt.Color;
import java.util.Arrays;

import com.hk.array.ArrayUtil;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class TSPGeneticAlgorithm extends Game
{
	private final int amtOfCities = 50;
	private final int popSize = 100;
	private final Vector2F[] cities;
	private final int[] bestOrder;
	private double bestDist = Double.MAX_VALUE;
	private final int[][] population;
	private final double[] populationFitness;
	private int generation = 0;
	
	public TSPGeneticAlgorithm()
	{
		cities = new Vector2F[amtOfCities];
		for(int i = 0; i < cities.length; i++)
		{
			cities[i] = new Vector2F(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT));
		}
		bestOrder = new int[amtOfCities];
		population = new int[popSize][amtOfCities];
		populationFitness = new double[popSize];
		for(int i = 0; i < population.length; i++)
		{
			for(int j = 0; j < population[i].length; j++)
			{
				population[i][j] = j;
			}
			ArrayUtil.shuffleArray(population[i]);
		}
	}
	
	@Override
	public void update(int ticks)
	{
		calculateFitness();
		normalizeFitness();
		nextGeneration();
		generation++;
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		for(int i = 0; i < cities.length; i++)
		{
			g2d.drawCircle(cities[i].x, cities[i].y, 3);
		}
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);

		g2d.setColor(Color.GREEN);
		for(int i = 0; i < bestOrder.length - 1; i++)
		{
			g2d.drawLine(cities[bestOrder[i]].x, cities[bestOrder[i]].y, cities[bestOrder[i + 1]].x, cities[bestOrder[i + 1]].y);
		}
		g2d.drawLine(cities[bestOrder[bestOrder.length - 1]].x, cities[bestOrder[bestOrder.length - 1]].y, cities[bestOrder[0]].x, cities[bestOrder[0]].y);
		
		g2d.setColor(Color.GREEN);
		g2d.drawString("Best Order: " + Arrays.toString(bestOrder) + ", " + String.format("%.2f", bestDist), 5, 15);
		g2d.drawString("Generation: " + generation, 5, 30);
	}
	
	private void calculateFitness()
	{
		for(int i = 0; i < population.length; i++)
		{
			double dst = calculateDist(population[i]);
			populationFitness[i] = 1F / (dst + 1F);
			
			if(dst < bestDist)
			{
				bestDist = dst;
				for(int j = 0; j < population[i].length; j++)
				{
					bestOrder[j] = population[i][j];
				}
			}
		}
	}
	
	private void normalizeFitness()
	{
		double sum = 0D;
		for(int i = 0; i < population.length; i++)
		{
			sum += populationFitness[i];
		}
		for(int i = 0; i < population.length; i++)
		{
			populationFitness[i] /= sum;
		}
	}
	
	private void nextGeneration()
	{
		int[][] newPopulation = new int[popSize][amtOfCities];
		for(int i = 0; i < population.length; i++)
		{
			int[] or1 = pickOne(population, populationFitness);
			int[] or2 = pickOne(population, populationFitness);
			int[] orFinal = crossOver(or1, or2);
			if(Rand.nextFloat() < 0.1F)
			{
				mutate(orFinal);
			}
			newPopulation[i] = orFinal;
		}
		for(int i = 0; i < population.length; i++)
		{
			for(int j = 0; j < population[i].length; j++)
			{
				population[i][j] = newPopulation[i][j];
			}
		}
	}
	
	private int[] pickOne(int[][] population, double[] fitness)
	{
		int indx = 0;
		double r = Rand.nextDouble();
		
		while(r > 0)
		{
			r = r - fitness[indx];
			indx++;
		}
		return population[indx - 1];
	}
	
	private int[] crossOver(int[] or1, int[] or2)
	{
		int[] orFinal = new int[(or1.length + or2.length) / 2];
		System.arraycopy(or1, 0, orFinal, 0, orFinal.length);
		return orFinal;
	}
	
	private void mutate(int[] arr)
	{
		ArrayUtil.swap(arr, Rand.nextInt(arr.length), Rand.nextInt(arr.length));
	}
	
	private double calculateDist(int[] order)
	{
		double total = 0D;
		for(int i = 0; i < order.length - 1; i++)
		{
			total += cities[order[i]].distance(cities[order[i + 1]]);
		}
		
		return total;
	}
	
	public static void main(String[] args)
	{
		System.setProperty("Main.WIDTH", "1024");
		System.setProperty("Main.HEIGHT", "768");
		TSPGeneticAlgorithm game = new TSPGeneticAlgorithm();
		
		GameSettings settings = new GameSettings();
		settings.title = "Traveling Salesperson (Genetic Algorithm)";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
}
