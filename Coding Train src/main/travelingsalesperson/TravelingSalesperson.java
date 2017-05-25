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
package main.travelingsalesperson;

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

public class TravelingSalesperson extends Game
{
	private final int amtOfCities = 10;
	private final Vector2F[] cities;
	private final int[] bestOrder;
	private double bestDist = Double.MAX_VALUE;
	private final int[] order;
	private double orderDist;
	
	public TravelingSalesperson()
	{
		cities = new Vector2F[amtOfCities];
		for(int i = 0; i < cities.length; i++)
		{
			cities[i] = new Vector2F(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT));
		}
		bestOrder = new int[amtOfCities];
		order = new int[amtOfCities];
		for(int i = 0; i < order.length; i++)
		{
			order[i] = i;
			bestOrder[i] = i;
		}
		bestDist = orderDist = calculateDist(order);
	}
	
	@Override
	public void update(int ticks)
	{
		ArrayUtil.swap(order, Rand.nextInt(order.length), Rand.nextInt(order.length));
		orderDist = calculateDist(order);
		
		if(orderDist < bestDist)
		{
			bestDist = orderDist;
			for(int i = 0; i < order.length; i++)
			{
				bestOrder[i] = order[i];
			}
		}
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

		g2d.setColor(Color.DARK_GRAY);
		for(int i = 0; i < order.length - 1; i++)
		{
			g2d.drawLine(cities[order[i]].x, cities[order[i]].y, cities[order[i + 1]].x, cities[order[i + 1]].y);
		}
		g2d.drawLine(cities[order[order.length - 1]].x, cities[order[order.length - 1]].y, cities[order[0]].x, cities[order[0]].y);
		
		g2d.setColor(Color.GREEN);
		for(int i = 0; i < bestOrder.length - 1; i++)
		{
			g2d.drawLine(cities[bestOrder[i]].x, cities[bestOrder[i]].y, cities[bestOrder[i + 1]].x, cities[bestOrder[i + 1]].y);
		}
		g2d.drawLine(cities[bestOrder[bestOrder.length - 1]].x, cities[bestOrder[bestOrder.length - 1]].y, cities[bestOrder[0]].x, cities[bestOrder[0]].y);
		
		g2d.setColor(Color.GREEN);
		g2d.drawString("Best Order: " + Arrays.toString(bestOrder) + ", " + String.format("%.2f", bestDist), 5, 15);
		g2d.setColor(Color.GRAY);
		g2d.drawString("Current Order: " + Arrays.toString(order) + ", " + String.format("%.2f", orderDist), 5, 30);
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
		TravelingSalesperson game = new TravelingSalesperson();
		
		GameSettings settings = new GameSettings();
		settings.title = "Traveling Salesperson";
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
