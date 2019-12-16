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
package main.raycast;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.math.Rand;

public class RayCast extends GuiScreen
{
	private final int size;
	private final Dot[] dots;
	private double angle;

	public RayCast(Game game)
	{
		super(game);
		
		size = 120;
		dots = new Dot[size];
		for(int i = 0; i < size; i++)
		{
			dots[i] = new Dot();
		}
		angle = Math.PI * 3 / 2;
	}
	
	@Override
	public void update(double delta)
	{
		for(int i = 0; i < size; i++)
		{
			Dot dot = dots[i];
			dot.px += dot.vx * delta;
			dot.py += dot.vy * delta;

			if(dot.px < dot.radius)
				dot.vx = Math.abs(dot.vx);
			if(dot.px > game.width - dot.radius)
				dot.vx = -Math.abs(dot.vx);

			if(dot.py < dot.radius)
				dot.vy = Math.abs(dot.vy);
			if(dot.py > game.height - dot.radius)
				dot.vy = -Math.abs(dot.vy);
		}
		
		angle += 0.1 * delta;
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_CENTER);
		g2d.setColor(Color.WHITE);
		for(int i = 0; i < size; i++)
		{
			Dot dot = dots[i];
			
			g2d.drawCircle(dot.px, dot.py, dot.radius);
		}
		g2d.disable(G2D.G_CENTER);
		
		double px = g2d.width / 2;
		double py = g2d.height / 2;
		double nx = px, ny = py;
		g2d.setLineWidth(2);
		int circles = 0;
		while(true)
		{
			g2d.setColor(Color.WHITE);
			g2d.drawLine(px, py, nx, ny);
			
			if(px < 0 || px > g2d.width || py < 0 || py > g2d.height)
				break;
			
			boolean in = false;
			double minLen = Double.MAX_VALUE;
			for(int i = 0; i < size; i++)
			{
				Dot dot = dots[i];
				double dx = px - dot.px;
				double dy = py - dot.py;
				double len = Math.sqrt(dx * dx + dy * dy);
				if(len <= dot.radius)
				{
					in = true;
					break;
				}
				len -= dot.radius;
				minLen = len < minLen ? len : minLen;
			}
			if(in)
				break;

			minLen = Math.max(minLen, 1);

			g2d.setColor(0, 0.5F, 1F, 0.4F);
			for(int i = 0; i < 2; i++)
			{
				if(i == 0)
					g2d.enable(G2D.G_FILL);
				g2d.drawCircle(px - minLen, py - minLen, minLen);
				if(i == 0)
					g2d.disable(G2D.G_FILL);
			}
			
			nx = px;
			ny = py;

			px += Math.cos(angle) * minLen;
			py += Math.sin(angle) * minLen;
			circles++;
		}

		g2d.setColor(Color.WHITE);
		g2d.enable(G2D.G_FILL);
		g2d.drawCircle(px - 5, py - 5, 5);
		g2d.disable(G2D.G_FILL);
		
		g2d.drawString(circles, 5, 15);
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Ray Casting";
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;		

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new RayCast(frame.game));
		frame.launch();
	}
	
	public class Dot
	{
		private double px, py, vx, vy, radius;
		
		public Dot()
		{
			radius = Rand.nextDouble(5, 30);
			px = Rand.nextDouble(radius, game.width - radius);
			py = Rand.nextDouble(radius, game.height - radius);
			double ang = Rand.nextDouble(Math.PI * 2);
			double len = Rand.nextDouble(5, 20);
			vx = Math.cos(ang) * len;
			vy = Math.sin(ang) * len;
		}
	}
}
