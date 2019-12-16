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
package main.powered;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.MathUtil;

public class Powered extends GuiScreen
{	
	public final World world;
	public final int scl = 10;
	private double zoom, moveX, moveY, lastX, lastY;
	private boolean paused, dragging;
	private int selected;
	private long msgTime = -1;
	private String msg;
	private boolean grid;
	
	public Powered(Game game)
	{
		super(game);
		world = new World(this, 31);
		reset();
		grid = true;
	}

	private void reset()
	{
		zoom = 2;
		moveX = game.width / 2 - (world.size * scl) / 2;
		moveY = game.height / 2 - (world.size * scl) / 2;

		selected = 1;
		setMessage("Selected Wire", 2000);
	}

	@Override
	public void update(double delta)
	{
		float mx = handler.getX();
		float my = handler.getY();
		if(handler.isPressed())
		{
			double dx = mx - lastX;
			double dy = my - lastY;
			moveX += dx / zoom;
			moveY += dy / zoom;
			
			dragging = dragging || MathUtil.hypot(dx, dy) > 2;
		}
		else dragging = false;
		lastX = mx;
		lastY = my;
		
		if(!paused) world.updateWorld();
		
		if(msgTime < System.currentTimeMillis())
		{
			msgTime = -1;
			msg = null;
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		float mouseX = handler.getX();
		float mouseY = handler.getY();
		g2d.pushMatrix();
		
		g2d.setColor(Color.WHITE);
		g2d.enable(G2D.G_FILL);
		g2d.drawRectangle(0, 0, g2d.width, g2d.height);
		g2d.disable(G2D.G_FILL);
		
		AffineTransform aft = applyCameraTransform(null);
		g2d.g2d.transform(aft);
		aft = invert(aft);
		Point2D.Float mi = new Point2D.Float(mouseX, mouseY);
		aft.transform(mi, mi);
		
		Rectangle2D viewBounds = new Rectangle2D.Double(0, 0, game.width, game.height);
		viewBounds = aft.createTransformedShape(viewBounds).getBounds2D();
		int startX = (int) Math.floor(viewBounds.getMinX() / scl);
		int startY = (int) Math.floor(viewBounds.getMinY() / scl);
		int endX = (int) Math.ceil(viewBounds.getMaxX() / scl);
		int endY = (int) Math.ceil(viewBounds.getMaxY() / scl);

		startX = MathUtil.between(0, startX, world.size);
		startY = MathUtil.between(0, startY, world.size);
		endX = MathUtil.between(0, endX, world.size);
		endY = MathUtil.between(0, endY, world.size);
		
		g2d.setColor(0, 0, 0, 60);
		drawGridLines(g2d, 0, 0, world.size, world.size);

		g2d.setColor(Color.RED);
		for(int x = startX; x < endX; x++)
		{
			for(int y = startY; y < endY; y++)
			{
				Piece p = world.getPiece(x, y);
				if(!p.isAir())
				{
					p.paintPiece(g2d, world, x * 10, y * 10, x, y);
				}
			}
		}
		
		int selX = (int) Math.floor(mi.x / scl);
		int selY = (int) Math.floor(mi.y / scl);
		g2d.setColor(world.getPiece(selX, selY).isAir() ? Color.BLUE : Color.RED);
		g2d.drawRectangle(selX * scl, selY * scl, scl, scl);
		
		g2d.popMatrix();
		
		g2d.setColor(Color.BLACK);
		g2d.drawString(selX + ", " + selY, 5, 15);
		
		if(msg != null)
		{
			g2d.setColor(Color.BLACK);
			g2d.setFontSize(24F);
			Rectangle2D b = g2d.getStringBounds(msg);
			
			g2d.clearRect(0, g2d.height - b.getHeight() - 5, b.getWidth() + 10, b.getHeight() + 5);
			g2d.drawRectangle(0, g2d.height - b.getHeight() - 5, b.getWidth() + 10, b.getHeight() + 5);
			
			g2d.drawString(msg, 5, g2d.height - 5);
		}
		g2d.setFontSize(12F);
		g2d.drawString("Use the number keys to change pieces", g2d.width - 220, g2d.height - 20);
		g2d.drawString("Mouse buttons to place and remove", g2d.width - 200, g2d.height - 5);
	}
	
	public void setMessage(String message, long millis)
	{
		msgTime = System.currentTimeMillis() + millis;
		msg = message;
	}
	
	private AffineTransform invert(AffineTransform aft)
	{
		aft = new AffineTransform(aft);
		try { aft.invert(); }
		catch (NoninvertibleTransformException e) { e.printStackTrace(); }
		return aft;
	}

	private AffineTransform applyCameraTransform(AffineTransform aft)
	{
		if(aft == null)
		{
			aft = new AffineTransform();
		}

        double zoomWidth = game.width * zoom;
        double zoomHeight = game.height * zoom;

        double anchorx = (game.width - zoomWidth) / 2;
        double anchory = (game.height - zoomHeight) / 2;

        aft.translate(anchorx, anchory);
        aft.scale(zoom, zoom);
        aft.translate(moveX, moveY);
		
		return aft;
	}

	private void drawGridLines(G2D g2d, int startX, int startY, int endX, int endY)
	{
		if(grid)
		{
			for(int i = startX; i <= endX; i++)
			{
				g2d.drawLine(i * scl, startY * scl, i * scl, endY * scl);
			}
			for(int i = startY; i <= endY; i++)
			{
				g2d.drawLine(startX * scl, i * scl, endX * scl, i * scl);
			}
		}
		else
		{
			g2d.drawLine(0, 0, world.size * scl, 0);
			g2d.drawLine(world.size * scl, 0, world.size * scl, world.size * scl);
			
			g2d.drawLine(0, 0, 0, world.size * scl);
			g2d.drawLine(0, world.size * scl, world.size * scl, world.size * scl);
		}
	}

	@Override
	public void mouse(float x, float y, boolean pressed)
	{
		if(pressed || dragging) return;
		int button = handler.getButton();
		
		Point2D.Float mi = new Point2D.Float(x, y);
		invert(applyCameraTransform(null)).transform(mi, mi);
		int px = (int) Math.floor(mi.x / scl);
		int py = (int) Math.floor(mi.y / scl);
		Piece p = world.getPiece(px, py);
		if(button == MouseEvent.BUTTON1)
		{
			if(p.isAir())
			{
				world.setPiece(px, py, Piece.all[selected], true);
			}
			else
			{
				p.onInteract(world, px, py);
			}
		}
		else if(button == MouseEvent.BUTTON3)
		{
			world.setToAir(px, py, true);
		}
	}

	@Override
	public void mouseWheel(int amt)
	{
		zoom *= 1 - amt / 10.0;
		zoom = MathUtil.between(1, zoom, 10);
	}

	@Override
	public void key(int key, char keyChar, boolean pressed)
	{
		if(pressed) return;

		if(key == KeyEvent.VK_X)
		{
			paused = !paused;
		}
		else if(key == KeyEvent.VK_C)
		{
			reset();
		}
		else if(key == KeyEvent.VK_G)
		{
			grid = !grid;
		}
		else
		{
			int n = -1;
			try
			{
				n = Integer.parseInt(String.valueOf(keyChar));
				if(n == 0) n = 10;
			}
			catch(NumberFormatException e)
			{}
			
			if(n != -1)
			{
				selected = MathUtil.between(0, n, Piece.all.length - 1);
				setMessage("Selected " + Piece.all[selected].name, 2000);
			}
		}
	}
	
	public static void main(String[] args)
	{	
		Settings settings = new Settings();
		settings.title = "Powered";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = 60;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Powered(frame.game));
		frame.launch();
	}
}
