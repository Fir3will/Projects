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
package main.noise;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.OpenSimplexNoise;
import com.hk.math.Rand;

public class Noise extends GuiScreen
{
	private final int scale = 5, w, h;
	private final OpenSimplexNoise osn = new OpenSimplexNoise(Rand.nextLong());
	private double time;
			
	public Noise(Game game)
	{
		super(game);
		
		w = game.width / scale;
		h = game.height / scale;
	}

	@Override
	public void update(double delta)
	{
		time += delta / 2;
	}

	@Override
	public void paint(G2D g2d)
	{		
		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				double n = (osn.eval(x / 10F, y / 10F, time) + 1) / 2;
				
				g2d.setColor(1F, (float) n, (float) n);
				g2d.drawRectangle(x * scale, y * scale, scale, scale);
			}
		}
		g2d.disable(G2D.G_FILL);
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Noise";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Noise(frame.game));
		frame.launch();
	}
}
