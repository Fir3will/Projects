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
package main.arrows;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.FloatMath;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class Arrows extends GuiScreen
{
	private final List<Arrow> goodArrows, evilArrows;
	private final Polygon arrow = new Polygon();
	private long last = 0;
	
	public Arrows(com.hk.g2d.Game game)
	{
		super(game);
		goodArrows = new ArrayList<>();
		evilArrows = new ArrayList<>();

		arrow.addPoint(-5, 0);
		arrow.addPoint(0, -8);
		arrow.addPoint(5, 0);
		arrow.addPoint(2, 0);
		arrow.addPoint(2, 8);
		arrow.addPoint(-2, 8);
		arrow.addPoint(-2, 0);
		
		for(int i = 0; i < 10; i++)
		{
			Arrow a = new Arrow(false);
			a.pos.set(game.width * Rand.nextFloat(), game.height * Rand.nextFloat());
			goodArrows.add(a);

			a = new Arrow(true);
			a.pos.set(game.width * Rand.nextFloat(), game.height * Rand.nextFloat());
			evilArrows.add(a);
		}
	}
	
	@Override
	public void update(double delta)
	{
		Vector2F mx = game.handler.get();
		for(int i = 0; i < goodArrows.size(); i++)
		{
			Arrow good = goodArrows.get(i);
			if(!good.updateArrow())
			{
				goodArrows.remove(i);
				i--;
				continue;
			}
			good.rot = -mx.subtract(good.pos).getAngle() + FloatMath.PI;
			good.vel.set(mx.subtract(good.pos).normalizeLocal());
		}
		
		for(int i = 0; i < evilArrows.size(); i++)
		{
			Arrow evil = evilArrows.get(i);
			if(!evil.updateArrow())
			{
				evilArrows.remove(i);
				i--;
				continue;
			}

			Arrow closest = null;
			float dst = 0;
			for(int j = 0; j < goodArrows.size(); j++)
			{
				Arrow good = goodArrows.get(j);
				float nd = evil.pos.distance(good.pos);
				if(closest == null || nd < dst)
				{
					dst = nd;
					closest = good;
				}
			}
			if(closest != null)
			{
				if(dst > 1)
				{
					evil.rot = -closest.pos.subtract(evil.pos).getAngle() + FloatMath.PI;
					evil.vel.addLocal(closest.pos.subtract(evil.pos).normalizeLocal().divideLocal(100F));
				}
				else
				{
					evil.health -= 0.05F;
					closest.health -= 0.5F;
				}
			}
		}
		
		if(game.handler.isPressed())
		{
			long time = System.currentTimeMillis();
			if(time - last > 5)
			{
				boolean evil = game.handler.isKeyDown(KeyEvent.VK_SPACE);
				last = time;
				Arrow a = new Arrow(evil);
				a.pos.set(mx);
				if(evil) evilArrows.add(a);
				else goodArrows.add(a);
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		for(int i = 0; i < goodArrows.size(); i++)
		{
			goodArrows.get(i).paintArrow(g2d);
		}
		
		for(int i = 0; i < evilArrows.size(); i++)
		{
			evilArrows.get(i).paintArrow(g2d);
		}
		g2d.disable(G2D.G_FILL);

		float x = 5;
		int sum = goodArrows.size() + evilArrows.size();
		float gp = goodArrows.size() * 1000 / sum / 10F;
		float ep = evilArrows.size() * 1000 / sum / 10F;
		g2d.setColor(Color.BLACK);
		g2d.drawString(sum + "|", x, 15);
		x += g2d.getStringBounds(sum + "|").getWidth();
		g2d.setColor(Color.GREEN);
		g2d.drawString(gp + "%", x, 15);
		x += g2d.getStringBounds(gp + "%").getWidth();
		g2d.setColor(Color.BLACK);
		g2d.drawString("|", x, 15);
		x += g2d.getStringBounds("|").getWidth();
		g2d.setColor(Color.RED);
		g2d.drawString(ep + "%", x, 15);
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed)
		{
			if(key == KeyEvent.VK_SPACE)
			{
				for(int i = 0; i < 100; i++)
				{
					Arrow a = new Arrow(i % 2 == 0);
					a.pos.set(game.width * Rand.nextFloat(), game.height * Rand.nextFloat());
					
					if(a.evil) evilArrows.add(a);
					else goodArrows.add(a);
				}
			}
			else if(key == KeyEvent.VK_Q)
			{
				for(int i = 0; i < goodArrows.size(); i++)
					goodArrows.get(i).vel.zero();
				for(int i = 0; i < evilArrows.size(); i++)
					evilArrows.get(i).vel.zero();
			}
			else if(key == KeyEvent.VK_W)
			{
				for(int i = 0; i < goodArrows.size(); i++)
				{
					goodArrows.get(i).evil = true;
				}
				for(int i = 0; i < evilArrows.size(); i++)
				{
					evilArrows.get(i).evil = false;
				}
				ArrayList<Arrow> temp = new ArrayList<>(goodArrows);
				goodArrows.clear();
				goodArrows.addAll(evilArrows);
				evilArrows.clear();
				evilArrows.addAll(temp);
			}
		}
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Arrows";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;
		
		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Arrows(frame.game));
		frame.launch();
	}
	
	private class Arrow
	{
		public boolean evil;
		public final Vector2F pos, vel;
		public float rot, health;
		private Color clr;
		
		private Arrow(boolean evil)
		{
			this.evil = evil;
			pos = new Vector2F();
			vel = new Vector2F();
			
			clr = Color.getHSBColor(Rand.nextFloat() * FloatMath.PI * 2, 1, 1);
			health = 1F;
		}
		
		public boolean updateArrow()
		{
			if(vel.lengthSquared() > 1) vel.normalizeLocal();
			if(!evil) vel.multLocal(0.95F * health);
			pos.addLocal(vel);
			
			return health > 0;
		}
		
		public void paintArrow(G2D g2d)
		{
			g2d.setColor(evil ? Color.BLACK : clr);
			g2d.translate(pos.x, pos.y);
			g2d.rotateR(rot);
			g2d.drawShape(arrow);
			g2d.rotateR(-rot);
			g2d.translate(-pos.x, -pos.y);
		}
	}
}
