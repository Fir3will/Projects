package main.hexgrid;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Main;
import main.gui.GuiScreen;

public class GameScreen extends GuiScreen
{
	private final HexCell[][] grid;
	private int cw = 21, ch = 21;
	private float actualZoom = 1F, zoom = 1F;
	private final Vector2F lastPress, move;
	private final AffineTransform aft;
	private int sx, sy;
	public final HexGrid game;

	public GameScreen(HexGrid game)
	{
		this.game = game;
		
		aft = new AffineTransform();
		lastPress = new Vector2F();
		move = new Vector2F(Main.WIDTH / 2, Main.HEIGHT / 2);
		grid = new HexCell[cw][ch];
		for (int x = 0; x < cw; x++)
		{
			for (int y = 0; y < ch; y++)
			{
				grid[x][y] = new HexCell(x - cw / 2, y - ch / 2);
			}
		}
	}
	
	public void updateScreen(int ticks)
	{
		zoom = MathUtil.lerp(zoom, actualZoom, 0.9F);
	}

	@Override
	public void paintScreen(G2D g2d, float mouseX, float mouseY)
	{
		g2d.pushMatrix();
		double ax = (g2d.width - (g2d.width * zoom)) / 2D;
		double ay = (g2d.height - (g2d.height * zoom)) / 2D;
		g2d.translate(ax, ay);
		g2d.scale(zoom, zoom);
		g2d.translate(move.x, move.y);
		
		aft.setToIdentity();
		aft.translate(ax, ay);
		aft.scale(zoom, zoom);
		aft.translate(move.x, move.y);
		
		g2d.setColor(Color.BLACK);
		for (int x = 0; x < cw; x++)
		{
			for (int y = 0; y < ch; y++)
			{
				HexCell cell = getCell(x, y);
				cell.paintCell(g2d);
			}
		}
		if(sx != -1 && sy != -1)
		{
			g2d.setColor(Color.RED);
			getCell(sx, sy).paintCell(g2d);
			if(getCell(sx + 1, sy) != null)
			{
				getCell(sx + 1, sy);
			}
		}
		g2d.popMatrix();
		
		g2d.setColor(Color.BLACK);
		
		g2d.drawRectangle(0, 0, g2d.width - 1, g2d.height - 1);
	}

	public HexCell getCell(int x, int y)
	{
		return inBounds(x, y) ? grid[x][y] : null;
	}
	
	public boolean inBounds(int x, int y)
	{
		return x >= 0 && x < cw && y >= 0 && y < ch;
	}
	
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(!pressed && button == MouseEvent.BUTTON1)
		{
			Vector2F v = transform(new Vector2F(x, y));
			for (int x1 = 0; x1 < cw; x1++)
			{
				for (int y1 = 0; y1 < ch; y1++)
				{
					if(grid[x1][y1].contains(v.x, v.y))
					{
						sx = x1;
						sy = y1;
					}
				}
			}
		}
		if(pressed && button == MouseEvent.BUTTON3)
		{
			lastPress.set(x, y);
		}
		else if(button == MouseEvent.BUTTON3)
		{
			lastPress.set(-1, -1);
		}
	}
	
	public void mouseMoved(float x, float y)
	{
		if(handler.isButton(MouseEvent.BUTTON3))
		{
			if(lastPress.x != -1 && lastPress.y != -1)
			{
				move.subtractLocal(lastPress.subtract(x, y).divideLocal(zoom));
				lastPress.set(x, y);
			}
		}
	}
	
	public void mouseWheel(int amt)
	{
		actualZoom *= 1 - amt * 0.05F;
		actualZoom = MathUtil.clamp(actualZoom, 5F, 0.25F);
	}
	
	private Vector2F transform(Vector2F v)
	{
		Point2D.Float p = new Point2D.Float(v.x, v.y);
		try
		{
			aft.inverseTransform(p, p);
		}
		catch (NoninvertibleTransformException e)
		{
			throw new RuntimeException(e);
		}
		v.x = p.x;
		v.y = p.y;
		return v;
	}
}
