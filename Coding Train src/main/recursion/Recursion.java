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
package main.recursion;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.FloatMath;
import com.hk.math.vector.Vector2F;

public class Recursion extends GuiScreen
{
	private double time;
	
	public Recursion(Game game)
	{
		super(game);
	}

	@Override
	public void update(double delta)
	{
		time += delta * 3;
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.enable(G2D.G_CENTER);
		drawCircle(g2d, (float) time, g2d.width / 2F, g2d.height / 2F, g2d.height / 2F);
		g2d.disable(G2D.G_CENTER);
	}
		
	public void drawCircle(G2D g2d, float ticks, float x, float y, float radius)
	{
		g2d.drawCircle(x, y, radius);
		
		if(radius > 5)
		{
			Vector2F v = new Vector2F(radius / 2F, 0);
			v.rotateAround(FloatMath.toRadians(ticks % 360), true);

			drawCircle(g2d, ticks * 2, x + v.x, y + v.y, radius / 2);
			drawCircle(g2d, ticks * 2, x - v.x, y - v.y, radius / 2);
		}
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Recursion";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 800;
		settings.height = 800;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Recursion(frame.game));
		frame.launch();
	}
}
