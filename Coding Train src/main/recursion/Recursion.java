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
package main.recursion;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.hk.math.FloatMath;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class Recursion extends Game
{
	private final List<Trail> trails;
	
	public Recursion()
	{
		trails = new ArrayList<>();
	}

	@Override
	public void update(int ticks)
	{
		
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.enable(G2D.G_CENTER);
		drawCircle(g2d, getTicks() / 5F, g2d.width / 2F, g2d.height / 2F, g2d.height / 2F);
		g2d.disable(G2D.G_CENTER);

		g2d.setColor(Color.DARK_GRAY);
		for(int i = 0; i < trails.size(); i++)
		{
			Trail t = trails.get(i);
			g2d.drawPoint(t.pos.x, t.pos.y);
			
			if(t.isDead())
			{
				trails.remove(i);
				i--;
			}
		}
	}
		
	public void drawCircle(G2D g2d, float ticks, float x, float y, float radius)
	{
		trails.add(new Trail(x, y));
		g2d.drawCircle(x, y, radius);
		
		if(radius > 5)
		{
			Vector2F v = new Vector2F(radius / 2F, 0);
			v.rotateAround(FloatMath.toRadians(ticks % 360), true);

			drawCircle(g2d, ticks * 2, x + v.x, y + v.y, radius / 2);
			drawCircle(g2d, ticks * 2, x - v.x, y - v.y, radius / 2);
		}
	}

	public static void main(String[] args)
	{
		
		GameSettings settings = new GameSettings();
		settings.title = "Recursion";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 800;
		settings.height = 800;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;
		
		System.setProperty("Main.WIDTH", String.valueOf(settings.width / 4));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height / 4));
		Recursion game = new Recursion();
		Main.initialize(game, settings);
	}
	
	private class Trail
	{
		public final Vector2F pos;
		private int ticks;
		
		public Trail(float x, float y)
		{
			this.pos = new Vector2F(x, y);
		}
		
		public boolean isDead()
		{
			return ticks++ >= 100;
		}
	}
}
