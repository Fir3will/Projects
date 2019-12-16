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
package main.wawlpaiper;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.FloatMath;
import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.math.vector.Color3F;

public class Wawlpaiper extends GuiScreen
{
	public final int scl = 10, w, h;
	public final Color3F[] grid;
	private final Trail[] trails;
	
	public Wawlpaiper(Game game)
	{
		super(game);
		this.w = game.width /  scl;
		this.h = game.height /  scl;
		grid = new Color3F[w * h];
		for(int i = 0; i < grid.length; i++)
		{
			grid[i] = new Color3F();
		}
		trails = new Trail[clrs.length * 50];
		for(int i = 0; i < trails.length; i++)
		{
			trails[i] = new Trail(clrs[i % clrs.length]);
		}
	}
	
	@Override
	public void update(double delta)
	{}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		for(int i = 0; i < grid.length; i++)
		{
			int x = i % w;
			int y = i / w;
			Color3F c = grid[i].clamp();
			g2d.setColor(c.r, c.g, c.b);
			g2d.drawRectangle(x * scl, y * scl, scl, scl);
			c.subtractLocal(0.001F);
		}
		g2d.disable(G2D.G_FILL);
		
		for(int i = 0; i < trails.length; i++)
		{
			Trail t = trails[i];
			t.px += t.vx;
			t.py += t.vy;

			if(t.px < 0)
			{
				t.px = 0;
				t.vx = Math.abs(t.vx);
			}
			if(t.px >= g2d.width)
			{
				t.px = g2d.width - 1;
				t.vx = -Math.abs(t.vx);
			}
			if(t.py < 0)
			{
				t.py = 0;
				t.vy = Math.abs(t.vy);
			}
			if(t.py >= g2d.height)
			{
				t.py = g2d.height - 1;
				t.vy = -Math.abs(t.vy);
			}

			int x = (int) (t.px / scl);
			int y = (int) (t.py / scl);
			grid[x + y * w].addLocal(t.clr);
		}
	}
	
	public void mouse(float mx, float my, boolean pressed)
	{
		if(pressed)
		{
			int rad = 20;
			int x = (int) (mx / scl);
			int y = (int) (my / scl);
			for(int i = Math.max(0, x - rad); i <= Math.min(w - 1, x + rad); i++)
			{
				for(int j = Math.max(0, y - rad); j <= Math.min(h - 1, y + rad); j++)
				{
					if(MathUtil.hypot(i - x, j - y) <= rad)
					{
						grid[i + j * w].multLocal(0.9F);
					}
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Wawlpaiper";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1600;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Wawlpaiper(frame.game));
		frame.launch();
	}
	
	private class Trail
	{
		private final Color3F clr;
		private float px, py, vx, vy;
		
		private Trail(Color3F clr)
		{
			this.clr = new Color3F(clr);
			float ang = Rand.nextFloat(FloatMath.PI * 2);
			px = Rand.nextFloat(game.width);
			py = Rand.nextFloat(game.height);
			vx = FloatMath.cos(ang) * scl;
			vy = FloatMath.sin(ang) * scl;
		}
	}
	
	private final Color3F[] clrs = { Color3F.RED, Color3F.GREEN, Color3F.BLUE, Color3F.YELLOW, Color3F.CYAN, Color3F.PURPLE };
//	private final Color3F[] clrs = { Color3F.RED, Color3F.GREEN, Color3F.BLUE };
}
