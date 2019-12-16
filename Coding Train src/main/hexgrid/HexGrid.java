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
package main.hexgrid;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;

public class HexGrid extends GuiScreen
{
	private final HexCell[][] grid;
	private int cw = 21, ch = 21;
	private float actualZoom = 1F, zoom = 1F;
	private final Vector2F lastPress, move;
	private final AffineTransform aft;
	private int sx, sy;
	private boolean down;

	public HexGrid(Game game)
	{
		super(game);
		
		aft = new AffineTransform();
		lastPress = new Vector2F();
		move = new Vector2F(game.width / 2, game.height / 2);
		grid = new HexCell[cw][ch];
		for (int x = 0; x < cw; x++)
		{
			for (int y = 0; y < ch; y++)
			{
				grid[x][y] = new HexCell(x - cw / 2, y - ch / 2);
			}
		}
	}
	
	public void update(double delta)
	{
		zoom = MathUtil.lerp(zoom, actualZoom, 0.9F);
	}

	@Override
	public void paint(G2D g2d)
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
				cell.paintCell(g2d, false);
			}
		}
		g2d.setLineWidth(1.5F);
		if(sx != -1 && sy != -1)
		{
			if(getCell(sx, sy + 1) != null)
			{
				g2d.setColor(0x00DD00);
				getCell(sx, sy + 1).paintCell(g2d, true);
			}
			if(getCell(sx, sy - 1) != null)
			{
				g2d.setColor(0x22FF22);
				getCell(sx, sy - 1).paintCell(g2d, true);
			}

			if(getCell(sx + 1, sy) != null)
			{
				g2d.setColor(0x0000DD);
				getCell(sx + 1, sy).paintCell(g2d, true);
			}
			if(getCell(sx - 1, sy) != null)
			{
				g2d.setColor(0x2222FF);
				getCell(sx - 1, sy).paintCell(g2d, true);
			}
			int off = sx % 2 == 0 ? -1 : 1;

			if(getCell(sx + 1, sy + off) != null)
			{
				g2d.setColor(0xDD0000);
				getCell(sx + 1, sy + off).paintCell(g2d, true);
			}
			if(getCell(sx - 1, sy + off) != null)
			{
				g2d.setColor(0xFF2222);
				getCell(sx - 1, sy + off).paintCell(g2d, true);
			}
			g2d.setColor(Color.ORANGE);
			getCell(sx, sy).paintCell(g2d, true);
		}
		g2d.setLineWidth(1F);
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
	
	public void mouse(float x, float y, boolean pressed)
	{
		int button = handler.getButton();
		
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
		if(button == MouseEvent.BUTTON3)
		{
			if(pressed)
				lastPress.set(x, y);
			else
				lastPress.set(-1, -1);
			down = pressed;
		}
	}
	
	public void mouseMoved(float x, float y)
	{
		if(down)
		{
			move.subtractLocal(lastPress.subtract(x, y).divideLocal(zoom));
			lastPress.set(x, y);
		}
	}
	
	public void mouseWheel(int amt)
	{
		actualZoom *= 1 - amt * 0.05F;
		actualZoom = MathUtil.between(0.25F, actualZoom, 5F);
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
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Hexagonal Grid";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1200;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new HexGrid(frame.game));
		frame.launch();
	}
}
