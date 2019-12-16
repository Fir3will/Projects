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
package main.travelingsalesperson;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class TravelingSalesperson extends GuiScreen
{
	private final int amtOfCities = 10;
	private final Vector2F[] cities;
	private final float[][] distances;
	private final int[] bestOrder;
	private double bestDist = Double.MAX_VALUE;
	private final int[] order;
	private double orderDist;
	
	public TravelingSalesperson(Game game)
	{
		super(game);
		
		cities = new Vector2F[amtOfCities];
		distances = new float[amtOfCities][amtOfCities];
		bestOrder = new int[amtOfCities];
		order = new int[amtOfCities];
		reset();
	}
	
	private void reset()
	{
		for(int i = 0; i < cities.length; i++)
		{
			cities[i] = new Vector2F(Rand.nextFloat(game.width), Rand.nextFloat(150, game.height));
		}
		for(int i = 0; i < cities.length; i++)
		{
			for(int j = 0; j < cities.length; j++)
			{
				distances[i][j] = i == j ? 0 : cities[i].distance(cities[j]);
			}
		}
		for(int i = 0; i < order.length; i++)
		{
			order[i] = i;
			bestOrder[i] = i;
		}
		bestDist = orderDist = calculateDist(order);
	}
	
	@Override
	public void update(double delta)
	{
		int i, j;
		int amt = 2500;
		for(; amt >= 0; amt--)
		{
			System.arraycopy(bestOrder, 0, order, 0, amtOfCities);
			int switches = Rand.nextInt(1, amtOfCities);
			for(; switches >= 0; switches--)
			{
				do
				{
					i = Rand.nextInt(amtOfCities);
					j = Rand.nextInt(amtOfCities);
				} while(i == j);
				int tmp = order[i];
				order[i] = order[j];
				order[j] = tmp;
				i = j = 0;
			}
			orderDist = calculateDist(order);
			
			if(orderDist < bestDist)
			{
				bestDist = orderDist;
				System.arraycopy(order, 0, bestOrder, 0, amtOfCities);
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
		
		g2d.setFontSize(24F);
		g2d.setColor(Color.GREEN);
		g2d.drawString("Best Order: " + Arrays.toString(bestOrder) + ", " + String.format("%.2f", bestDist), 5, 25);
		g2d.setColor(Color.GRAY);
		g2d.drawString("Current Order: " + Arrays.toString(order) + ", " + String.format("%.2f", orderDist), 5, 55);
	}
	
	private double calculateDist(int[] order)
	{
		double total = 0D;
		for(int i = 0; i < order.length; i++)
		{
			int a = order[i];
			int b = i == order.length - 1 ? 0 : order[i + 1];
//			total += cities[a].distance(cities[b]);
			total += distances[a][b];
		}
		
		return total;
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		if(key == KeyEvent.VK_SPACE)
		{
			reset();
		}
	}
	
	public static void main(String[] args)
	{	
		Settings settings = new Settings();
		settings.title = "Traveling Salesperson";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new TravelingSalesperson(frame.game));
		frame.launch();
	}
}
