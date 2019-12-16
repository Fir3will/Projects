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
package main.platformer;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class Platformer extends GuiScreen
{
	public final World world;
	private final Camera camera;
	private final AffineTransform aft;
	private boolean debug;
	private Vector2F lastPress;
	private boolean down;
	
	public Platformer(Game game)
	{
		super(game);
		
		camera = new Camera(this, 1F);
//		camera = new Camera(this, 3.75F);
		aft = new AffineTransform();

//		https://opengameart.org/users/rottingpixels
//		https://opengameart.org/content/18x20-characters-walkattackcast-spritesheet
		world = new World(80, 45);
//		world = new World(40, 22);

		world.setPrimary(3, 5, Block.BRICK);
		world.setPrimary(4, 5, Block.BRICK);
		world.setPrimary(4, 6, Block.BRICK);
		world.setPrimary(4, 7, Block.BRICK);
		world.setPrimary(4, 9, Block.BRICK);
		world.setPrimary(5, 5, Block.BRICK);

		for(int i = 10; i < 17; i++)
		{
			for(int j = 10; j < 14; j++)
			{
				world.setPrimary(i, j, Block.GRASS);
			}
		}

		world.setPrimary(12, 12, Block.CAVE);
		world.setSecondary(14, 12, Block.GEM, 0);
		

		for(int i = 30; i < 70; i++)
		{
			for(int j = 12; j < 35; j++)
			{
				world.setPrimary(i, j, Block.GRASS);
			}
		}

		for(int i = 10; i < 55; i++)
		{
			for(int j = 37; j < 44; j++)
			{
				world.setPrimary(i, j, Block.BRICK);
			}
		}
		
		int amt = 70;
		while(amt-- >= 0)
		{
			int x = Rand.nextInt(world.width);
			int y = Rand.nextInt(world.height);
			world.setSecondary(x, y, Block.GEM, Rand.nextInt(4));
		}
		
		world.addEntity(new Entity(world));
	}
	
	@Override
	public void update(double delta)
	{
		if(down)
		{
			Vector2F mouse = handler.get();
			camera.move.subtractLocal(lastPress.subtractLocal(mouse).divideLocal(camera.zoom));
			lastPress = mouse;
		}
		
		world.updateWorld(delta);
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.pushMatrix();
		AffineTransform aft = g2d.getMatrix();
		camera.apply(aft);
		g2d.setMatrix(aft);

		this.aft.setToIdentity();
		camera.apply(this.aft);

		int sx = 0;
		int ex = 80;
		int sy = 0;
		int ey = 45;
		try
		{
			Point2D p = new Point2D.Double(0, 0);
			this.aft.inverseTransform(p, p);
			sx = (int) (p.getX() / 16);
			sy = (int) (p.getY() / 16);
			p.setLocation(g2d.width, g2d.height);
			this.aft.inverseTransform(p, p);
			ex = (int) (p.getX() / 16) + 1;
			ey = (int) (p.getY() / 16) + 1;
		}
		catch (NoninvertibleTransformException e)
		{
			throw new RuntimeException(e);
		}
		world.renderWorld(g2d, sx, ex, sy, ey, debug);
		g2d.popMatrix();
	}
	
	public void mouse(float x, float y, boolean pressed)
	{
		if(pressed)
		{
			if(lastPress == null)
				lastPress = new Vector2F(x, y);
		}
		else
		{
			lastPress = null;
		}
		down = pressed;
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		if(key == KeyEvent.VK_SPACE && !pressed)
		{
			debug = !debug;
		}
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Platformer";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = new Color(12, 155, 255);
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Platformer(frame.game));
		frame.launch();
	}
}
