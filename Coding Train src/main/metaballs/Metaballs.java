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
package main.metaballs;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.hk.math.vector.Vector2F;
import com.sun.javafx.geom.Point2D;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class Metaballs extends Game
{
	private final int scale = 2, h, w;
	private final Metaball[] balls;
	private final BufferedImage img;
	
	public Metaballs()
	{
		w = Main.WIDTH / scale;
		h = Main.HEIGHT / scale;
		img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		balls = new Metaball[10];
		for(int i = 0; i < balls.length; i++)
		{
			balls[i] = new Metaball();
		}
	}

	@Override
	public void update(int ticks)
	{
		for(int i = 0; i < balls.length; i++)
		{
			balls[i].update();
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
				Color c = new Color(Color.HSBtoRGB(1F - Math.min(sum, 1F), 1F, 1F));
				img.setRGB(x, y, c.getRGB());
//				g2d.setColor(c);
//				g2d.drawRectangle(x * scale, y * scale, scale, scale);
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.scale(scale, scale);
		g2d.drawImage(img, 0, 0);
//		g2d.enable(G2D.G_FILL);
//		g2d.disable(G2D.G_FILL);
	}

	public static void main(String[] args)
	{
		System.setProperty("Main.WIDTH", "1024");
		System.setProperty("Main.HEIGHT", "768");
		Metaballs game = new Metaballs();
		
		GameSettings settings = new GameSettings();
		settings.title = "Metaballs";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
	
	private class Metaball
	{
		public final Vector2F pos = new Vector2F(w / 2, h / 2), vel = Vector2F.randUnitVector();
		
		public void update()
		{
			pos.addLocal(vel);
			
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
