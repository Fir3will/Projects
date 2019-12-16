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
package main.runespace;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;

public class Runespace extends GuiScreen
{
	public final World world;
	public final Entity player;

	public Runespace(Game game)
	{
		super(game);

		world = new World(this);
//		for(int i = 0; i < 25; i++)
//		{
//			Entity e = new Entity(world, Model.get(Rand.nextInt(Model.size())));
//			e.pos.x = Rand.nextInt(game.width / 54);
//			e.pos.y = Rand.nextInt(game.height / 54);
//			e.direction = Direction.get(Rand.nextInt(Direction.size()));
//			e.moving = i % 2 == 0;
//			world.addEntity(e);
//		}
		Entity e = new Entity(world, Model.TEMPLATE);
		e.xCoord = 1;
		e.yCoord = (int) (game.height / 108F);
		e.direction = Direction.EAST;
//		e.moving = true;
		player = e;
		world.addEntity(e);

		addOverlay(new ModelOverlay(this));
		addOverlay(new HealthOverlay(this));
	}

	@Override
	public void update(double delta)
	{
		world.updateWorld(delta);
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
//		for(int i = 0; i < game.width; i += 20)
//		{
//			for(int j = 0; j < game.height; j += 20)
//			{
//				g2d.drawRect(i, j, 20, 20);
//			}
//		}
//		for(int i = 0; i < game.width; i += 54)
//		{
//			g2d.drawLine(i, 0, i, game.height - 1);
//		}
//		for(int j = 0; j < game.height; j += 54)
//		{
//			g2d.drawLine(0, j, game.width - 1, j);
//		}
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		boolean keyW = key == KeyEvent.VK_W;
		boolean keyA = key == KeyEvent.VK_A;
		boolean keyS = key == KeyEvent.VK_S;
		boolean keyD = key == KeyEvent.VK_D;
		boolean keyCtrl = key == KeyEvent.VK_CONTROL;
		boolean keyShft = key == KeyEvent.VK_SHIFT;
		
		if(keyW)
		{
			player.direction = Direction.NORTH;
			player.moving = pressed;
		}
		if(keyA)
		{
			player.direction = Direction.WEST;
			player.moving = pressed;
		}
		if(keyS)
		{
			player.direction = Direction.SOUTH;
			player.moving = pressed;
		}
		if(keyD)
		{
			player.direction = Direction.EAST;
			player.moving = pressed;
		}
		if(keyCtrl)
		{
			player.still = pressed;
		}
		if(keyShft)
		{
			player.sprinting = pressed;
		}
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Runespace";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Runespace(frame.game));
		frame.launch();
	}
}
