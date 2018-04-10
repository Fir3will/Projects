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
package main.gameoflife;

import java.awt.Color;
import java.awt.event.KeyEvent;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class GameOfLife extends Game
{
	private final int scl, gw, gh;
	private final Cell[][] grid, nextGrid;
			
	public GameOfLife(int scale)
	{
		this.scl = scale;
		gw = Main.WIDTH / scl;
		gh = Main.HEIGHT / scl;
		grid = new Cell[gw][gh];
		for(int x = 0; x < gw; x++)
		{
			for(int y = 0; y < gh; y++)
			{
				grid[x][y] = new Cell(Rand.nextBoolean());
			}
		}
		nextGrid = new Cell[gw][gh];
	}

	@Override
	public void update(int ticks)
	{}

	@Override
	public void paint(G2D g2d)
	{
		for(int x = 0; x < gw; x++)
		{
			for(int y = 0; y < gh; y++)
			{
				nextGrid[x][y] = grid[x][y].update(x, y);
			}
		}
		for(int x = 0; x < gw; x++)
		{
			for(int y = 0; y < gh; y++)
			{
				grid[x][y] = nextGrid[x][y];
				nextGrid[x][y] = null;
			}
		}
		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < gw; x++)
		{
			for(int y = 0; y < gh; y++)
			{
				Cell c = grid[x][y];
				g2d.setColor(c.getRGB());
				g2d.drawRectangle(x * scl, y * scl, scl, scl);
			}
		}
		g2d.disable(G2D.G_FILL);
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		super.key(key, keyChar, pressed);
		
		if(key == KeyEvent.VK_SPACE)
		{
			Vector2F mv = new Vector2F(getHandler().mouseX() / scl, getHandler().mouseY() / scl);
			int radius = 20;
			for(int x = -radius; x <= radius; x++)
			{
				for(int y = -radius; y <= radius; y++)
				{
					float rx = x + mv.x;
					float ry = y + mv.y;
					
					if(mv.distance(rx, ry) <= radius)
					{
						set((int) rx, (int) ry, new Cell(getHandler().isKeyDown(KeyEvent.VK_SHIFT) || Rand.nextBoolean()));
					}
				}
			}
		}
	}
	
	public Cell get(int x, int y)
	{
		while(x < 0)
		{
			x += gw;
		}
		while(y < 0)
		{
			y += gh;
		}
		
		return grid[x % gw][y % gh];
	}
	
	public void set(int x, int y, Cell c)
	{
		while(x < 0)
		{
			x += gw;
		}
		while(y < 0)
		{
			y += gh;
		}
		
		grid[x % gw][y % gh] = c;
	}

	public static void main(String[] args)
	{
		GameSettings settings = new GameSettings();
		settings.title = "Game Of Life (Cellular Automata)";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1600;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = 100;

		System.setProperty("Main.WIDTH", String.valueOf(settings.width));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height));
		
		GameOfLife game = new GameOfLife(4);
		Main.initialize(game, settings);
	}
	
	public class Cell
	{
		private boolean isAlive;
		
		public Cell(boolean isAlive)
		{
			this.isAlive = isAlive;
		}
		
		public int getRGB()
		{
			return isAlive ? 0x000000 : 0xFFFFFF;
		}
		
		public Cell update(int x, int y)
		{
			int nb = 0;
			for(int i = -1; i <= 1; i++)
			{
				for(int j = -1; j <= 1; j++)
				{
					if(i == 0 && j == 0) continue;
					
					Cell c = get(x + i, y + j);
					if(c.isAlive)
					{
						nb++;
					}
				}
			}

			if(!isAlive)
			{
				return nb == 3 ? new Cell(true) : this;
			}
			else
			{
				return nb > 3 || nb < 2 ? new Cell(false) : this;
			}
		}
	}
}
