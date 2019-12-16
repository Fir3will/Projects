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
package main.sudoku;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;

public class Sudoku extends GuiScreen
{
	public int[][] tiles = new int[9][9];
	public boolean[][] stuck = new boolean[9][9];
	
	public Sudoku(Game game)
	{
		super(game);
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				tiles[x][y] = getNumberFor(x, y);
				if(tiles[x][y] <= 0)
				{
					tiles[x][y] = 0;
				}
				else
				{
					stuck[x][y] = true;
				}
			}
		}
	}
	
	public int getNumberFor(int x, int y)
	{
		if(Rand.nextDouble() < 0.60)
		{
			return -1;
		}
		int num = Rand.nextInt(1, 10);
		for(int x1 = 0; x1 < 9; x1++)
		{
			if(tiles[x1][y] == num)
			{
				return getNumberFor(x, y);
			}
		}
		for(int y1 = 0; y1 < 9; y1++)
		{
			if(tiles[x][y1] == num)
			{
				return getNumberFor(x, y);
			}
		}
		int x1 = x / 3 * 3;
		int y1 = y / 3 * 3;
		for(int a = x1; a < x1 + 3; a++)
		{
			for(int b = y1; b < y1 + 3; b++)
			{
				if(tiles[a][b] == num)
				{
					return getNumberFor(x, y);
				}
			}
		}
		return num;
	}

	@Override
	public void update(double delta)
	{

	}

	@Override
	public void paint(G2D g2d)
	{
		double w = g2d.width / 9;
		double h = g2d.height / 9;
		g2d.setFontSize(48F);
		for(int x = 0; x < 9; x++)
		{
			for(int y = 0; y < 9; y++)
			{
				double rx = x * w;
				double ry = y * h;
				g2d.setColor(Color.BLACK);
				g2d.drawRectangle(rx, ry, w, h);
				if(x % 3 == 0 && y % 3 == 0)
				{
					g2d.drawRectangle(rx - 1, ry - 1, w * 3 + 2, h * 3 + 2);
				}
				if(tiles[x][y] > 0)
				{
					g2d.enable(G2D.G_CENTER);
					if(stuck[x][y])
					{
						g2d.setColor(Color.BLUE);
					}
					else
					{
						g2d.setColor(Color.GREEN);
					}
					boolean errored = false;
					for(int x1 = 0; x1 < 9; x1++)
					{
						if(x1 != x && tiles[x1][y] == tiles[x][y])
						{
							errored = true;
						}
					}
					for(int y1 = 0; y1 < 9; y1++)
					{
						if(y1 != y && tiles[x][y1] == tiles[x][y])
						{
							errored = true;
						}
					}
					int x1 = x / 3 * 3;
					int y1 = y / 3 * 3;
					for(int a = x1; a < x1 + 3; a++)
					{
						for(int b = y1; b < y1 + 3; b++)
						{
							if(a != x && y != b && tiles[a][b] == tiles[x][y])
							{
								errored = true;
							}
						}
					}
					if(errored)
					{
						g2d.setColor(Color.RED);
					}
					if(errored && stuck[x][y])
					{
						g2d.setColor(new Color(1F, 0, 1F));
					}
					g2d.drawString(tiles[x][y], rx + w / 2, ry + h / 2);
					g2d.disable(G2D.G_CENTER);
				}
			}
		}
	}
	
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(!pressed)
		{
			int x1 = (int) (x / (game.width / 9));
			int y1 = (int) (y / (game.height / 9));
			if(!stuck[x1][y1])
			{
				tiles[x1][y1] = tiles[x1][y1] + 1;
			
				if(tiles[x1][y1] >= 10)
				{
					tiles[x1][y1] = -1;
				}
			}
		}
	}
	
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		int x1 = (int) (handler.getX() / (game.width / 9));
		int y1 = (int) (handler.getY() / (game.height / 9));
		if(Character.isDigit(keyChar))
		{
			if(!stuck[x1][y1])
			{
				tiles[x1][y1] = Integer.parseInt(String.valueOf(keyChar));
			}
		}
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Sudoku";
		settings.width = 900;
		settings.height = 900;
		settings.quality = Quality.POOR;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Sudoku(frame.game));
		frame.launch();
	}
}
