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
package main.evolutionarysb;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class EvolutionarySB extends GuiScreen
{
	public final float spawnChance = 0.002F;
	public final List<Vehicle> vehicles;
	public final List<Vector2F> food, poison;
	public float[] bestDNA;
	private boolean debug = false, speed = false;
	
	public EvolutionarySB(Game game)
	{
		super(game);
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
			Vehicle v = new Vehicle(this, i % 2 == 0 ? bestDNA : null);
			v.pos.set(Rand.nextFloat(game.width), Rand.nextFloat(game.height));
			vehicles.add(v);
		}
	}
	
	@Override
	public void update(double delta)
	{
		if(vehicles.isEmpty())
		{
			Vehicle v = new Vehicle(this, null);
			v.pos.set(game.width / 2, game.height / 2);
			vehicles.add(v);
		}
		
		if(Rand.nextFloat() < 0.3F)
		{
			food.add(new Vector2F(Rand.nextFloat(game.width - 20) + 10, Rand.nextFloat(game.height - 20) + 10));
		}
		if(Rand.nextFloat() < 0.1F)
		{
			poison.add(new Vector2F(Rand.nextFloat(game.width - 20) + 10, Rand.nextFloat(game.height - 20) + 10));
		}
		
		while(food.size() > 2000)
		{
			food.remove(0);
		}
		while(poison.size() > 2000)
		{
			poison.remove(0);
		}
		
		int maxTicks = 0;
		Vehicle best = null;
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
			}
			else if(v.ticksExisted > maxTicks)
			{
				best = v;
			}
			
			if(Rand.nextFloat() < spawnChance)
			{
				vehicles.add(v.clone());
			}
		}
		
		if(best != null)
		{
			bestDNA = Arrays.copyOf(best.dna, best.dna.length);
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.drawString(vehicles.size(), 5, 15);
		g2d.setColor(Color.GREEN);
		g2d.drawString(food.size(), 5, 30);
		g2d.setColor(Color.RED);
		g2d.drawString(poison.size(), 5, 45);
		if(speed)
		{
			for(int i = 1; i < 9; i++)
			{
				update(1 / 60D);
			}
			return;
		}
		
		for(int i = 0; i < vehicles.size(); i++)
		{
			vehicles.get(i).paint(g2d, debug);
		}
		
		g2d.enable(G2D.G_FILL | G2D.G_CENTER);

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
		
		g2d.disable(G2D.G_FILL | G2D.G_CENTER);
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
	
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(pressed)
		{
			if(button == MouseEvent.BUTTON1)
			{
				Vehicle v = new Vehicle(this, null);
				v.pos.set(x, y);
				vehicles.add(v);
			}
			else if(button == MouseEvent.BUTTON2)
			{
				food.add(new Vector2F(x, y));
			}
			else if(button == MouseEvent.BUTTON3)
			{
				poison.add(new Vector2F(x, y));
			}
		}
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Evolutionary Steering Behaviors";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.DARK_GRAY;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new EvolutionarySB(frame.game));
		frame.launch();
	}
}
