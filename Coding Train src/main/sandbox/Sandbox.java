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
package main.sandbox;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class Sandbox extends GuiScreen
{
	private final int size = 250;
	private final Ball[] balls;
	private int selectedBall = -1;
	private boolean paused = false, gravity = true;
	
	public Sandbox(Game game)
	{
		super(game);
		balls = new Ball[size];
		for(int i = 0; i < size; i++)
		{
			float f = i / (size - 1F);
			balls[i] = new Ball(game, 8, f);
			balls[i].pos.set(Rand.nextFloat(game.width), Rand.nextFloat(game.height));
			balls[i].vel.set(Vector2F.randUnitVector());
		}
//		balls[0].vel.set(size * 10, 0).rotateAround(Rand.nextFloat(FloatMath.PI * 2), true);
	}

	@Override
	public void update(double delta)
	{
		if(!paused)
		{
			for(int i = 0; i < size; i++)
			{
				balls[i].update(delta, gravity);
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
		}
		
		if(selectedBall != -1)
		{
			Ball b = balls[selectedBall];
			b.vel.subtractLocal(b.pos.subtract(game.handler.get()).divideLocal(200));
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_CENTER);
		g2d.enable(G2D.G_FILL);
		for(int i = 0; i < size; i++)
		{
			g2d.setColor(balls[i].clr.r, balls[i].clr.g, balls[i].clr.b);
			g2d.drawCircle(balls[i].pos.x, balls[i].pos.y, balls[i].radius);
		}
		g2d.disable(G2D.G_FILL);
		g2d.setColor(Color.BLACK);
		for(int i = 0; i < size; i++)
		{
			g2d.drawCircle(balls[i].pos.x, balls[i].pos.y, balls[i].radius);
		}
		g2d.disable(G2D.G_CENTER);
		
		if(selectedBall != -1)
		{
			Ball b = balls[selectedBall];
			g2d.drawLine(b.pos.x, b.pos.y, game.handler.getX(), game.handler.getY());
		}
	}
	
	@Override
	public void mouse(float x, float y, boolean pressed)
	{
		int button = handler.getButton();
		
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
			else if(key == KeyEvent.VK_SPACE)
			{
				gravity = !gravity;
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
		Settings settings = new Settings();
		settings.title = "Sandbox";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Sandbox(frame.game));
		frame.launch();
	}
}
