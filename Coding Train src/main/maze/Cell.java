package main.maze;

import java.awt.Color;
import java.util.Arrays;

import main.G2D;

public class Cell
{
	public final int x, y;
	public final boolean[] walls = new boolean[4];
	private boolean visited;
	
	public Cell(int x, int y)
	{
		this.x = x;
		this.y = y;
		Arrays.fill(walls, true);
	}
	
	public void drawCell(G2D g2d)
	{
		int x = this.x * Maze.SIZE;
		int y = this.y * Maze.SIZE;
		g2d.enable(G2D.G_FILL);
		g2d.drawRectangle(x, y, Maze.SIZE, Maze.SIZE);
		g2d.disable(G2D.G_FILL);
		
		g2d.setColor(Color.WHITE);
		if(walls[Side.NORTH.ordinal()]) g2d.drawLine(x, y, x + Maze.SIZE, y);
		if(walls[Side.EAST.ordinal()]) g2d.drawLine(x + Maze.SIZE, y, x + Maze.SIZE, y + Maze.SIZE);
		if(walls[Side.SOUTH.ordinal()]) g2d.drawLine(x + Maze.SIZE, y + Maze.SIZE, x, y + Maze.SIZE);
		if(walls[Side.WEST.ordinal()]) g2d.drawLine(x, y + Maze.SIZE, x, y);
	}
	
	public boolean hasWallTo(Cell c)
	{
		int x1 = c.x - x;
		if(x1 == 1)
		{
			return walls[Side.EAST.ordinal()] || c.walls[Side.WEST.ordinal()];
		}
		else if(x1 == -1)
		{
			return walls[Side.WEST.ordinal()] || c.walls[Side.EAST.ordinal()];
		}
		int y1 = c.y - y;
		if(y1 == 1)
		{
			return walls[Side.SOUTH.ordinal()] || c.walls[Side.NORTH.ordinal()];
		}
		else if(y1 == -1)
		{
			return walls[Side.NORTH.ordinal()] || c.walls[Side.SOUTH.ordinal()];
		}
		return true;
	}
	
	public void visit()
	{
		this.visited = true;
	}
	
	public boolean visited()
	{
		return visited;
	}
}
