package main.noise;

import java.awt.Color;

import com.hk.math.OpenSimplexNoise;
import com.hk.math.Rand;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class Noise extends Game
{
	private final int scale = 10, w, h;
	private final OpenSimplexNoise osn = new OpenSimplexNoise(Rand.nextLong());
			
	public Noise()
	{
		w = Main.WIDTH / scale;
		h = Main.HEIGHT / scale;
	}

	@Override
	public void update(int ticks)
	{

	}

	@Override
	public void paint(G2D g2d)
	{
		double z = getTicks() / 30F;
		
		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				double n = osn.eval(x / 10F, y / 10F, z) + 1D;
				n /= 2D;
				
				g2d.setColor(1F, (float) n, (float) n);
				g2d.drawRectangle(x * scale, y * scale, scale, scale);
			}
		}
		g2d.disable(G2D.G_FILL);
	}

	public static void main(String[] args)
	{
		Noise game = new Noise();
		
		GameSettings settings = new GameSettings();
		settings.title = "Noise";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
}
