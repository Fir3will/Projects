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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class Sandbox extends Game
{
	private final int size = 500;
	private final Ball[] balls;
	private int selectedBall = -1;
	private boolean paused = false;
	
	public Sandbox()
	{
		balls = new Ball[size];
		for(int i = 0; i < size; i++)
		{
			balls[i] = new Ball(2);
			balls[i].pos.set(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT));
			balls[i].vel.set(Vector2F.randUnitVector());
		}
	}

	@Override
	public void update(int ticks)
	{
		if(!paused)
		{
			for(int i = 0; i < size; i++)
			{
				balls[i].update(!getHandler().isKeyDown(KeyEvent.VK_SPACE));
			}
				
			for(int i = 0; i < size; i++)
			{
				for(int j = i + 1; j < size; j++)
				{
					if(balls[i].collided(balls[j]))
					{
						balls[i].resolveCollision(balls[j]);
					}
				}
			}
			
			if(selectedBall != -1)
			{
				Ball b = balls[selectedBall];
				b.vel.subtractLocal(b.pos.subtract(getHandler().mouseX(), getHandler().mouseY()).divideLocal(200));
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_CENTER);
		for(int i = 0; i < size; i++)
		{
			g2d.enable(G2D.G_FILL);
			g2d.setColor(balls[i].clr);
			g2d.drawCircle(balls[i].pos.x, balls[i].pos.y, balls[i].radius);
			g2d.disable(G2D.G_FILL);
			g2d.setColor(Color.BLACK);
			g2d.drawCircle(balls[i].pos.x, balls[i].pos.y, balls[i].radius);
		}
		g2d.disable(G2D.G_CENTER);
		
		g2d.setColor(Color.BLACK);
		if(selectedBall != -1)
		{
			Ball b = balls[selectedBall];
			g2d.drawLine(b.pos.x, b.pos.y, getHandler().mouseX(), getHandler().mouseY());
		}
	}
	
	@Override
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(button == MouseEvent.BUTTON1)
		{
			if(pressed)
			{
				if(selectedBall == -1)
				{
					for(int i = 0; i < size; i++)
					{
						if(balls[i].inBounds(x, y))
						{
							selectedBall = i;
							break;
						}
					}
				}
			}
			else
			{
				selectedBall = -1;
			}
		}
		else if(button == MouseEvent.BUTTON2)
		{
			for(int i = 0; i < size; i++)
			{
				if(balls[i].pos.distance(x, y) < 25)
				{
					balls[i].vel.subtractLocal(balls[i].pos.subtract(x, y).normalizeLocal());
				}
			}
		}
		else if(button == MouseEvent.BUTTON3)
		{
			for(int i = 0; i < size; i++)
			{
				if(balls[i].pos.distance(x, y) < 25)
				{
					balls[i].vel.addLocal(balls[i].pos.subtract(x, y).normalizeLocal());
				}
			}
		}
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed)
		{
			if(key == KeyEvent.VK_X)
			{
				paused = !paused;
			}
			else if(key == KeyEvent.VK_Q)
			{
				for(int i = 0; i < size; i++)
				{
					balls[i].vel.zero();
				}
			}
			else if(key == KeyEvent.VK_W)
			{
				for(int i = 0; i < size; i++)
				{
					balls[i].vel.negateLocal();
				}
			}
		}
	}

	public static void main(String[] args)
	{
		GameSettings settings = new GameSettings();
		settings.title = "Sandbox";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1600;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = 60;

		System.setProperty("Main.WIDTH", String.valueOf(settings.width / 4));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height / 4));

		Sandbox game = new Sandbox();
		Main.initialize(game, settings);
	}
}
