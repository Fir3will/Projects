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
package main.maze;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.hk.math.Rand;
import com.hk.math.vector.Color3F;
import com.sun.glass.events.KeyEvent;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class Maze extends Game
{
	public static final int SIZE = 20;
	private final Deque<Cell> stack = new ArrayDeque<>();	
	private final Cell[][] cells = new Cell[Main.WIDTH / SIZE][Main.HEIGHT / SIZE];
	private boolean solve, done, justComplete;
	private Cell current;
	private Cell look, start, end;
	
	private final Deque<Cell> correctPath = new ArrayDeque<>();
	private Cell[] pathArray;
	private final boolean[][] wasHere = new boolean[Main.WIDTH / SIZE][Main.HEIGHT / SIZE];
	
	public Maze()
	{
		for(int x = 0; x < cells.length; x++)
		{
			for(int y = 0; y < cells[x].length; y++)
			{
				cells[x][y] = new Cell(x, y);
			}
		}
		current = getCell(0, 0);
	}

	@Override
	public void update(int ticks)
	{
		if(!done && current != null)
		{
			current.visit();
			Cell next = getRandomUnvisitedNeighbor(current);
			if(next != null)
			{
				stack.push(current);
				removeWallBetween(current, next);
				current = next;
			}
			else
			{
				current = stack.isEmpty() ? null : stack.pop();
				if(current == null)
				{
					done = true;
				}
			}
		}
		
		if(done && solve)
		{
			if(look == end)
			{
				solve = false;
				Cell[] path = correctPath.toArray(new Cell[correctPath.size()]);
				pathArray = new Cell[path.length + 1];
				pathArray[0] = end;
				System.arraycopy(path, 0, pathArray, 1, path.length);
				return;
			}
			
			wasHere[look.x][look.y] = true;

			Cell next = getRandomNotSeen(look);
			if(next != null)
			{
				correctPath.push(look);
				look = next;
			}
			else
			{
				look = correctPath.pop();
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		if(justComplete)
		{
			g2d.setFontSize(32F);
			g2d.drawString("Done: " + done, 5, 30);
			g2d.drawString("Solve: " + solve, 5, 60);
			g2d.enable(G2D.G_FILL);
			if(current != null)
			{
				g2d.setColor(Color.BLACK);
				g2d.drawRectangle(current.x * SIZE, current.y * SIZE, SIZE, SIZE);
			}
			if(look != null)
			{
				g2d.setColor(Color.GREEN);
				g2d.drawRectangle(look.x * SIZE, look.y * SIZE, SIZE, SIZE);
			}
			if(start != null)
			{
				g2d.setColor(Color.RED);
				g2d.drawRectangle(start.x * SIZE, start.y * SIZE, SIZE, SIZE);
			}
			if(end != null)
			{
				g2d.setColor(Color.BLUE);
				g2d.drawRectangle(end.x * SIZE, end.y * SIZE, SIZE, SIZE);
			}
			g2d.disable(G2D.G_FILL);
		}
		else
		{
			g2d.setColor(new Color(0x000000));
			g2d.enable(G2D.G_FILL);
			g2d.drawRectangle(0, 0, g2d.width, g2d.height);
			g2d.disable(G2D.G_FILL);
			g2d.setColor(Color.WHITE);
			for(int x = 0; x < cells.length; x++)
			{
				for(int y = 0; y < cells[x].length; y++)
				{
					Cell c = getCell(x, y);
					if(!c.visited())
					{
						g2d.setColor(new Color(1F, 0, 1F, 0.2F));
					}
					else if(start == c)
					{
						g2d.setColor(new Color(1F, 0, 0, 0.6F));
					}
					else if(end == c)
					{
						g2d.setColor(new Color(0, 0, 1F, 0.6F));
					}
					else if(solve && correctPath.contains(c))
					{
						g2d.setColor(new Color(0, 1F, 0, 0.3F));
					}
					else
					{
						g2d.setColor(!done ? Color.DARK_GRAY : Color.BLACK);
					}
					if(c.visited())
					{
						c.drawCell(g2d);
					}
				}
			}
			
			if(pathArray != null && !solve)
			{
				g2d.scale(SIZE / 2F, SIZE / 2F);
				for(int i = 1; i < pathArray.length; i++)
				{
					Cell a = pathArray[i - 1];
					Cell b = pathArray[i];
					Color3F clr = new Color3F(0, 0, 1);
					clr.interpolateLocal(new Color3F(1, 0, 0), (float) i / pathArray.length);
					g2d.setColor(new Color(clr.r, clr.g, clr.b, 0.6F));
					g2d.drawLine((a.x * SIZE + SIZE / 2) / (SIZE / 2F), (a.y * SIZE + SIZE / 2) / (SIZE / 2F), (b.x * SIZE + SIZE / 2) / (SIZE / 2F), (b.y * SIZE + SIZE / 2) / (SIZE / 2F));
				}
			}
		}
	}
	
	public Cell getRandomUnvisitedNeighbor(Cell c)
	{
		List<Cell> unvisitedNeighbors = new ArrayList<>();
		for(Side s : Side.values())
		{
			Cell cl = getCell(c.x + s.xOff, c.y + s.yOff);
			if(cl != null && !cl.visited())
			{
				unvisitedNeighbors.add(cl);
			}
		}
		return unvisitedNeighbors.isEmpty() ? null : unvisitedNeighbors.get(Rand.nextInt(unvisitedNeighbors.size()));
	}
	
	public Cell getRandomNotSeen(Cell c)
	{
		List<Cell> unvisitedNeighbors = new ArrayList<>();
		for(Side s : Side.values())
		{
			Cell cl = getCell(c.x + s.xOff, c.y + s.yOff);
			if(cl != null && !c.hasWallTo(cl) && !wasHere[cl.x][cl.y])
			{
				if(cl == end)
				{
					return cl;
				}
				unvisitedNeighbors.add(cl);
			}
		}
		return unvisitedNeighbors.isEmpty() ? null : unvisitedNeighbors.get(Rand.nextInt(unvisitedNeighbors.size()));
	}
	
	public void removeWallBetween(Cell a, Cell b)
	{
		int x = b.x - a.x;
		if(x == 1)
		{
			a.walls[Side.EAST.ordinal()] = b.walls[Side.WEST.ordinal()] = false;
		}
		else if(x == -1)
		{
			a.walls[Side.WEST.ordinal()] = b.walls[Side.EAST.ordinal()] = false;
		}
		int y = b.y - a.y;
		if(y == 1)
		{
			a.walls[Side.SOUTH.ordinal()] = b.walls[Side.NORTH.ordinal()] = false;
		}
		else if(y == -1)
		{
			a.walls[Side.NORTH.ordinal()] = b.walls[Side.SOUTH.ordinal()] = false;
		}
	}
	
	public Cell getCell(int x, int y)
	{
		if(x >= 0 && x < cells.length)
		{
			return y >= 0 && y < cells[x].length ? cells[x][y] : null;
		}
		return null;
	}
	
	public void beginSolve()
	{
		solve = false;
		correctPath.clear();
		look = start;
		for(int i = 0; i < wasHere.length; i++)
		{
			Arrays.fill(wasHere[i], false);
		}
	}

	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(!pressed && done)
		{
			if(button == MouseEvent.BUTTON1)
			{
				start = getCell((int) (x / SIZE), (int) (y / SIZE));
				
				beginSolve();
				if(start != null && end != null)
				{
					solve = true;
				}
			}
			else if(button == MouseEvent.BUTTON3)
			{
				end = getCell((int) (x / SIZE), (int) (y / SIZE));
				
				beginSolve();
				if(start != null && end != null)
				{
					solve = true;
				}
			}
		}
	}
	
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if(!pressed && keyCode == KeyEvent.VK_SPACE)
		{
			justComplete = !justComplete;
		}
	}
	
	public static void main(String[] args)
	{
		GameSettings settings = new GameSettings();
		settings.title = "Maze";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1080;
		settings.height = 600;
		settings.showFPS = true;
		settings.maxFPS = -1;
		
		Main.initialize(new Maze(), settings);
	}
}
