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
package main.mandelbrot;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;

public class Mandelbrot extends GuiScreen
{
	private int iters = 100;
	private final int scale = 2, w, h;
	
	public Mandelbrot(Game game)
	{
		super(game);
		
		w = game.width / scale;
		h = game.height / scale;
	}

	@Override
	public void update(double delta)
	{
		
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				float x1 = x / (w / 4F) - 2F;
				float y1 = y / (h / 4F) - 2F;
				double a = x1;
				double b = y1;
				
				int z = 0;
				
				while(z < iters)
				{
					double a2 = a * a - b * b;
					double b2 = 2 * a * b;

					a = a2 + x1;
					b = b2 + y1;
					
					if(a2 * a2 + b2 * b2 > 16)
					{
						break;
					}
					z++;
				}
				
				z = z == iters ? 0 : z;
				
				g2d.setColor((float) z / iters, (float) z / iters, (float) z / iters);
				g2d.drawRectangle(x * scale, y * scale, scale, scale);
			}
		}
		g2d.disable(G2D.G_FILL);
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Mandelbrot";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;
		
		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Mandelbrot(frame.game));
		frame.launch();
	}
}
