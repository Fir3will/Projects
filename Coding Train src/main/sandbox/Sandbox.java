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
package main.sandbox;

import java.awt.Color;

import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class Sandbox extends Game
{
	private final int size = 100;
	private final Ball[] balls;
	private double lastTime = -1;
	
	public Sandbox()
	{
		balls = new Ball[size];
		for(int i = 0; i < size; i++)
		{
			balls[i] = new Ball(5F);
			balls[i].pos.set(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT));
			balls[i].vel.set(Vector2F.randUnitVector().mult(20));
		}
	}

	@Override
	public void update(int ticks)
	{
		double time = System.currentTimeMillis();
		if(lastTime == -1)
		{
			lastTime = time;
		}
		if(lastTime != time)
		{
			float df = (float) Math.min((time - lastTime) * 1E3D, 1D / 30D);
			for(int i = 0; i < size; i++)
			{
				balls[i].update(df);
			}
			
			for(int i = 0; i < size; i++)
			{
				for(int j = i + 1; j < size; j++)
				{
					if(balls[i].collided(balls[j]))
					{
						Vector2F ap = balls[i].pos.clone();
						Vector2F av = balls[i].vel.clone();
						Vector2F bp = balls[j].pos.clone();
						Vector2F bv = balls[j].vel.clone();
						balls[i].resolveCollision(df, bp, bv, balls[j].radius);
						balls[j].resolveCollision(df, ap, av, balls[i].radius);
					}
				}
			}
		}
		lastTime = time;
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		for(int i = 0; i < size; i++)
		{
			g2d.setColor(balls[i].clr);
			g2d.drawCircle(balls[i].pos.x, balls[i].pos.y, balls[i].radius);
		}
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);
	}
	
	private Vector2F startSpot;

	@Override
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(pressed)
		{
			startSpot = startSpot == null ? new Vector2F(x, y) : startSpot;
		}
		else
		{
			startSpot = null;
		}
	}

	@Override
	public void mouseMoved(float x, float y)
	{
	}

	public static void main(String[] args)
	{
		GameSettings settings = new GameSettings();
		settings.title = "Sandbox";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		System.setProperty("Main.WIDTH", String.valueOf(settings.width));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height));

		Sandbox game = new Sandbox();
		Main.initialize(game, settings);
	}
}
