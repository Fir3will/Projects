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
package main.evolutionarysb;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class EvolutionarySB extends Game
{
	public final float spawnChance = 0.002F;
	public final List<Vehicle> vehicles;
	public final List<Vector2F> food, poison;
	private boolean debug = false, speed = false;
	
	public EvolutionarySB()
	{
		vehicles = new ArrayList<>();
		food = new ArrayList<>();
		poison = new ArrayList<>();
		
		reset();
	}
	
	public void reset()
	{
		vehicles.clear();
		food.clear();
		poison.clear();
		
		for(int i = 0; i < 50; i++)
		{
			Vehicle v = new Vehicle(null);
			v.pos.set(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT));
			vehicles.add(v);
		}
		
		for(int i = 0; i < 40; i++)
		{
			food.add(new Vector2F(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT)));
		}
		
		for(int i = 0; i < 20; i++)
		{
			poison.add(new Vector2F(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT)));
		}
	}
	
	@Override
	public void update(int ticks)
	{
		if(!speed && ticks % 10 != 0) return;
		if(Rand.nextFloat() < 0.01F)
		{
			food.add(new Vector2F(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT)));
		}
		if(Rand.nextFloat() < 0.01F)
		{
			poison.add(new Vector2F(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT)));
		}
		
		for(int i = 0; i < vehicles.size(); i++)
		{
			Vehicle v = vehicles.get(i);
			v.checkBoundaries();
			v.act(food, poison);
			v.update();
			
			if(v.isDead())
			{
				vehicles.remove(i);
				i--;
				food.add(new Vector2F(v.pos));
			}
			if(Rand.nextFloat() < spawnChance)
			{
				vehicles.add(v.clone());
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		for(int i = 0; i < vehicles.size(); i++)
		{
			vehicles.get(i).paint(g2d, debug);
		}
		
		g2d.enable(G2D.G_FILL);

		g2d.setColor(Color.GREEN);
		for(int i = 0; i < food.size(); i++)
		{
			g2d.drawCircle(food.get(i).x, food.get(i).y, 3);
		}
		
		g2d.setColor(Color.RED);
		for(int i = 0; i < poison.size(); i++)
		{
			g2d.drawCircle(poison.get(i).x, poison.get(i).y, 3);
		}
		
		g2d.disable(G2D.G_FILL);
	}
	
	@Override
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if(!pressed && keyCode == KeyEvent.VK_SPACE)
		{
			debug = !debug;
		}
		else if(!pressed && keyCode == KeyEvent.VK_X)
		{
			speed = !speed;
		}
		else if(!pressed && keyCode == KeyEvent.VK_C)
		{
			int sze = food.size();
			food.addAll(poison);
			poison.clear();
			for(int i = 0; i < sze; i++)
			{
				poison.add(food.remove(0));
			}
		}
		else if(!pressed && keyCode == KeyEvent.VK_V)
		{
			reset();
		}
	}
	
	public static void main(String[] args)
	{
		System.setProperty("Main.WIDTH", "1024");
		System.setProperty("Main.HEIGHT", "768");
		EvolutionarySB game = new EvolutionarySB();
		
		GameSettings settings = new GameSettings();
		settings.title = "Evolutionary Steering Behaviors";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.DARK_GRAY;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
}
