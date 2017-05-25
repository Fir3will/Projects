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
package main.diffusionlimitedagregation;

import java.awt.Color;

import com.hk.math.Rand;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class DiffusionLA extends Game
{
	public final int scl = 5, width = Main.WIDTH / scl, height = Main.HEIGHT / scl;
	public final int[][] grid;
	public int set = 2;
	
	public DiffusionLA()
	{
		grid = new int[width][height];	
		grid[width / 2][height / 2] = set;
		int loop = 10;

		for(int x = 0; x < width; x++)
		{
			for(int i = 0; i < loop; i++)
			{
				grid[x][i] = 1;
				grid[x][height - i - 1] = 1;
			}
		}
		for(int y = 0; y < height; y++)
		{
			for(int i = 0; i < loop; i++)
			{
				grid[i][y] = 1;
				grid[width - i - 1][y] = 1;
			}
		}
	}
	
	@Override
	public void update(int ticks)
	{
		for(int x1 = 0; x1 < width; x1++)
		{
			for(int y1 = 0; y1 < height; y1++)
			{
				int x = ticks % 2 == 0 ? x1 : width - x1 - 1;
				int y = ticks % 2 == 0 ? y1 : height - y1 - 1;
				if(grid[x][y] == 1)
				{
					boolean move = true;
					label1:
					for(int a = -1; a <= 1; a++)
					{
						for(int b = -1; b <= 1; b++)
						{
							int rx = x + a;
							int ry = y + b;
							
							if(inBounds(rx, ry))
							{
								if(grid[rx][ry] > 1)
								{
									grid[x][y] = set++;
									move = false;
									break label1;
								}
							}
						}
					}
					
					if(move)
					{
						int tries = 0;
						while(tries < 10)
						{
							tries++;
							int rx = x + (Rand.nextBoolean() ? 1 : -1);
							int ry = y + (Rand.nextBoolean() ? 1 : -1);
							
							if(inBounds(rx, ry) && grid[rx][ry] == 0)
							{
								grid[x][y] = 0;
								grid[rx][ry] = 1;
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				if(grid[x][y] != 0)
				{
					if(grid[x][y] == 1)
					{
						g2d.setColor(Color.WHITE);
					}
					if(grid[x][y] > 1)
					{
						float hue = grid[x][y] / (float) set / 2F;
						g2d.setColor(new Color(Color.HSBtoRGB(hue, 1F, 1F)));
					}
					g2d.drawRectangle(x * scl, y * scl, scl, scl);
				}
			}
		}
		g2d.disable(G2D.G_FILL);
	}
	
	public boolean inBounds(int x, int y)
	{
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	public static void main(String[] args)
	{
		DiffusionLA game = new DiffusionLA();
		
		GameSettings settings = new GameSettings();
		settings.title = "Diffusion-Limited Aggregation";
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
