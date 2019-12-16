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
package main.metaballs;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.vector.Vector2F;

public class Metaballs extends GuiScreen
{
	private final int scale = 2, h, w;
	private final Metaball[] balls;
	private final BufferedImage img;
	private boolean dark;
	
	public Metaballs(Game game)
	{
		super(game);
		
		w = game.width / scale;
		h = game.height / scale;
		img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		balls = new Metaball[25];
		for(int i = 0; i < balls.length; i++)
		{
			balls[i] = new Metaball();
		}
	}

	@Override
	public void update(double delta)
	{
		for(int i = 0; i < balls.length; i++)
		{
			balls[i].update(delta);
		}
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				float sum = 0F;
				for(int i = 0; i < balls.length; i++)
				{
					sum += 5F / Point2D.distance(x, y, balls[i].pos.x, balls[i].pos.y);
				}
				if(dark)
				{
					img.setRGB(x, y, sum > 1 ? 0xFFFFFFFF : 0xFF000000);
				}
				else
				{
					Color c = new Color(Color.HSBtoRGB(1F - Math.min(sum, 1F), 1F, 1F));
					img.setRGB(x, y, c.getRGB());
				}
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.scale(scale, scale);
		g2d.drawImage(img, 0, 0);
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed && key == KeyEvent.VK_SPACE)
		{
			dark = !dark;
		}
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Metaballs";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Metaballs(frame.game));
		frame.launch();
	}
	
	private class Metaball
	{
		public final Vector2F pos = new Vector2F(w / 2, h / 2), vel = Vector2F.randUnitVector().multLocal(25);
		
		public void update(double delta)
		{
			pos.x += vel.x * delta;
			pos.y += vel.y * delta;
			
			if(pos.x < 0 || pos.x > w)
			{
				vel.x = -vel.x;
			}
			if(pos.y < 0 || pos.y > h)
			{
				vel.y = -vel.y;
			}
		}
	}
}
