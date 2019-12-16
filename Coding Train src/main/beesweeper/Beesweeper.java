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
package main.beesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;

public class Beesweeper extends GuiScreen
{	
	public final int size = 20;
	public int cellSize;
	private final int amtOfBees = 30;
	private int amtOfBeesCaught = 0;
	private int winState = 0;
	private final Cell[][] cells;
	
	public Beesweeper(Game game)
	{
		super(game);
		
		cellSize = game.width / size;
		cells = new Cell[size][size];
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				cells[i][j] = new Cell(this, i, j);
			}
		}
		reset(amtOfBees);
	}

	@Override
	public void update(double delta)
	{
		
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.drawString(amtOfBeesCaught + " bee" + (amtOfBeesCaught == 1 ? "" : "s") + " caught out of " + amtOfBees, 5, 15);
		String str = (amtOfBeesCaught * 100 / amtOfBees) + "%";
		Rectangle2D w = g2d.getStringBounds(str);
		g2d.drawString(str, g2d.width - w.getWidth() - 5, 15);
		if(winState != 0)
		{
			if(winState == -1)
			{
				g2d.setColor(Color.RED);
				g2d.drawString("You Lost! Press 'R' to try again!", 5, 30);
			}
			else if(winState == 1)
			{
				g2d.setColor(Color.BLUE);
				g2d.drawString("You Won! Press 'R' to play again!", 5, 30);
			}
		}
		g2d.translate(0, 40);
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(18F));
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				cells[i][j].paintCell(g2d);
			}
		}
	}
	
	public Cell getCell(int x, int y)
	{
		return x >= 0 && x < size && y >= 0 && y < size ? cells[x][y] : null;
	}
	
	private void reset(int amtOfBees)
	{
		amtOfBeesCaught = 0;
		winState = 0;
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				cells[i][j].revealed = cells[i][j].flagged = false;
				cells[i][j].state = 0;
			}
		}
		List<Point> ls = new ArrayList<>();
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				ls.add(new Point(i, j));
			}
		}
		for(int i = 0; i < amtOfBees; i++)
		{
			Point p = ls.remove(Rand.nextInt(ls.size()));
			getCell(p.x, p.y).state = -1;
		}

		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				Cell c = getCell(i, j);
				if(c.state != -1)
				{
					c.calcState();
				}
			}
		}
	}
	
	public void mouse(float x, float y, boolean pressed)
	{
		int button = game.handler.getButton();
		if(winState != 0) return;
		
		if(!pressed)
		{
			int x1 = (int) (x / cellSize);
			int y1 = (int) ((y - 40) / cellSize);
			Cell c = getCell(x1, y1);
			if(button == MouseEvent.BUTTON1)
			{
				if(c != null)
				{
					if(c.state == -1 && !c.flagged)
					{
						for(int i = 0; i < size; i++)
						{
							for(int j = 0; j < size; j++)
							{
								getCell(i, j).revealed = true;
							}
						}
						winState = -1;
					}
					else if(c.state != -1)
					{
						c.reveal();
					}
				}
			}
			else if(button == MouseEvent.BUTTON3)
			{
				if(c != null)
				{
					if(c.state == -1)
					{
						c.flagged = true;
						c.revealed = true;
						amtOfBeesCaught++;
						
						if(amtOfBeesCaught == amtOfBees)
						{
							for(int i = 0; i < size; i++)
							{
								for(int j = 0; j < size; j++)
								{
									getCell(i, j).revealed = true;
								}
							}
							winState = 1;
						}
					}
					else
					{
						for(int i = 0; i < size; i++)
						{
							for(int j = 0; j < size; j++)
							{
								getCell(i, j).revealed = true;
							}
						}
						winState = -1;
					}
				}
				// TRY AND FLAG
			}
		}
	}
	
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if(!pressed && keyCode == KeyEvent.VK_R)
		{
			reset(amtOfBees);
		}
	}

	public static void main(String[] args)
	{		
		Settings settings = new Settings();
		settings.title = "Beesweeper";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 601;
		settings.height = 641;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Beesweeper(frame.game));
		frame.launch();
	}
}
