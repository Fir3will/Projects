package main.beesweeper;

import java.awt.Color;

import main.G2D;

public class Cell
{
	public final Beesweeper game;
	public final int x, y;
	public boolean revealed, flagged;
	public int state;
	
	public Cell(Beesweeper game, int x, int y)
	{
		this.game = game;
		this.x = x;
		this.y = y;
		
		revealed = flagged = false;
		state = 0;
	}
	
	public void paintCell(G2D g2d)
	{
		int s = game.cellSize;
		if(revealed)
		{
			g2d.enable(G2D.G_FILL);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.drawRectangle(x * s, y * s, s, s);
			g2d.setColor(Color.BLACK);
			if(state > 0)
			{
				g2d.enable(G2D.G_CENTER);
				g2d.drawString(state, x * s + s / 2, y * s + s / 2);
				g2d.disable(G2D.G_CENTER);
			}
			else if(state == -1)
			{
				g2d.enable(G2D.G_CENTER);
				g2d.setColor(flagged ? Color.GREEN : Color.RED);
				g2d.drawCircle(x * s + s / 2, y * s + s / 2, s / 2.2F);
				g2d.disable(G2D.G_CENTER);
			}
			g2d.disable(G2D.G_FILL);
		}
		g2d.setColor(Color.BLACK);
		g2d.drawRectangle(x * s, y * s, s, s);
	}
	
	public void calcState()
	{
		state = 0;
		for(int i = -1; i <= 1; i++)
		{
			for(int j = -1; j <= 1; j++)
			{
				Cell c = game.getCell(i + x, j + y);
				if(c != null && c.state == -1)
				{
					state++;
				}
			}
		}
	}
	
	public void reveal()
	{
		revealed = true;
		if(state == 0)
		{
			for(int i = -1; i <= 1; i++)
			{
				for(int j = -1; j <= 1; j++)
				{
					Cell c = game.getCell(i + x, j + y);
					if(c != null && !c.revealed && c.state != -1)
					{
						c.reveal();
					}
				}
			}
		}
	}
}
