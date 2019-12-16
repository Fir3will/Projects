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
package main.games;

import java.awt.Color;
import java.io.File;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.shape.Rectangle;

import main.games.GameCase.GameScreen;

public class ModdingEngine extends GuiScreen
{
	public final Loader loader;
	public final Rectangle screen;
	private GameCase selected = null;
	private GameScreen currGame;
	
	public ModdingEngine(Game game, Loader loader)
	{
		super(game);
		
		this.loader = loader;
		screen = new Rectangle(50, 80, 500, 500);
	}

	@Override
	public void update(double delta)
	{
		if(selected != null)
		{
			currGame.update(delta);
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setFontSize(24);
		
		if(selected == null)
		{
			g2d.enable(G2D.G_CENTER);
			g2d.setColor(Color.GRAY);
			g2d.drawString("Pick a game from the right", screen.getCenterX(), g2d.height / 2);
			g2d.disable(G2D.G_CENTER);
		}
		else
		{
			g2d.setColor(Color.WHITE);
			
			g2d.enable(G2D.G_CENTER);
			g2d.drawString(selected.name, 300, 40);
			g2d.disable(G2D.G_CENTER);

			g2d.g2d.setClip((int) screen.x, (int) screen.y, (int) screen.width, (int) screen.height);
			g2d.pushMatrix();
			g2d.translate(screen.x, screen.y);
			currGame.paint(g2d);
			g2d.popMatrix();
			g2d.g2d.setClip(0, 0, g2d.width, g2d.height);
			
			g2d.setColor(Color.GRAY);
			g2d.drawRect(screen.x, screen.y, screen.width, screen.height);
		}

		g2d.setColor(Color.WHITE);
		g2d.enable(G2D.G_FILL);
		g2d.drawRect(600, 0, 200, g2d.height);
		g2d.disable(G2D.G_FILL);

		g2d.setColor(Color.BLACK);
		Rectangle r = new Rectangle(605, 35, 190, 25);
		for(int i = 0; i < loader.size(); i++)
		{
			GameCase game = loader.getGame(i);
			
			if(selected == game)
			{
				g2d.setColor(Color.GREEN);
				g2d.enable(G2D.G_FILL);
				g2d.drawCircle(r.x + 5, r.y + r.height / 2 - 5, 5);
				g2d.disable(G2D.G_FILL);
				g2d.setColor(Color.BLACK);
			}
			g2d.drawCircle(r.x + 5, r.y + r.height / 2 - 5, 5);

			g2d.drawString(game.name, 625, r.y + r.height - 5);
			r.y += r.height + 5;
		}
		g2d.setColor(Color.BLACK);
		g2d.enable(G2D.G_CENTER);
		g2d.drawString("Games", 700, 10);
		g2d.drawLine(650, 30, 750, 30);
		g2d.disable(G2D.G_CENTER);
	}
	
	@Override
	public void mouse(float x, float y, boolean pressed)
	{
		if(selected != null && screen.contains(x, y))
		{
			x -= screen.x;
			y -= screen.y;
			
			currGame.mouse(x, y, pressed, game.handler.getButton());
			return;
		}
		
		if(pressed)
			return;
		
		if(x >= 600 && x <= 800)
		{
			selected = null;
			Rectangle r = new Rectangle(605, 35, 190, 25);
			for(int i = 0; i < loader.size(); i++)
			{
				if(r.contains(x, y))
				{
					selected = loader.getGame(i);
					try
					{
						currGame = selected.createGame();
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}
					break;
				}
				r.y += r.height + 5;
			}
		}
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		if(selected != null)
		{
			currGame.key(key, keyChar, pressed);
		}
	}

	public static void main(String[] args)
	{		
		Settings settings = new Settings();
		settings.title = "Arcade Games";
		settings.quality = Quality.GOOD;
		settings.width = 800;
		settings.height = 600;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		ModdingEngine engine = new ModdingEngine(frame.game, new Loader(new File("games")));
		frame.game.setCurrentScreen(engine);
		frame.launch();
	}
}
