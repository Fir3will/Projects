package main.sudoku;

import java.awt.Color;

import com.hk.math.Rand;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.Main;
import main.GameSettings.Quality;

public class Sudoku extends Game
{
	public int[][] tiles = new int[9][9];
	public boolean[][] stuck = new boolean[9][9];
	
	public Sudoku()
	{
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
	public void update(int ticks)
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
			int x1 = (int) (x / (Main.WIDTH / 9));
			int y1 = (int) (y / (Main.HEIGHT / 9));
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
		int x1 = (int) (getHandler().mouseX() / (Main.WIDTH / 9));
		int y1 = (int) (getHandler().mouseY() / (Main.HEIGHT / 9));
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
		GameSettings settings = new GameSettings();
		settings.title = "Sudoku";
		settings.width = 900;
		settings.height = 900;
		settings.quality = Quality.POOR;
		
		Main.initialize(new Sudoku(), settings);
	}
}
