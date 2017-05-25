/**************************************************************************
 *
 * [2017] Fir3will, All Rights Reserved.
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
package main.gravity;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import com.hk.math.FloatMath;
import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class Gravity extends Game
{
//	public static final float G = 6.67E-11F;
	public static final float G = 0.1F;
	private float zoom = 1F;
	private Vector2F lastPress, move;
	public final List<Planet> planets;
	private boolean paused, debugInfo;
	private final AffineTransform aft;
	private final PlanetList lst;
	
	public Gravity()
	{
		lastPress = new Vector2F();
		move = new Vector2F();
		aft = new AffineTransform();
		
		planets = new ArrayList<>();
		lst = new PlanetList(this);
		paused = true;
		
		
		Planet sun = new Planet("Sun", 512, 384, 150);
		planets.add(sun);
		
		for(int i = 0; i < 360; i += 45)
		{
			Vector2F v = new Vector2F(200, 0);
			v.rotateAround(FloatMath.toRadians(i), true);
			
			Planet p = new Planet("P" + i, v.x + sun.pos.x, v.y + sun.pos.y, 1);
//			v.rotateAround((90F + 45F) / 2F, true);
			v.set(-v.y, v.x);
			v.normalizeLocal().multLocal(0.275F);
			p.vel.set(v);
			planets.add(p);
		}
//		planets.add(new Planet("P1", 212, 384, 1).setVelocity(0, 0.2F));
//		planets.add(new Planet("P2", 812, 384, 1).setVelocity(0, -0.2F));
//		planets.add(new Planet("P3", 512, 84, 1).setVelocity(-0.2F, 0));
//		planets.add(new Planet("P4", 512, 684, 1).setVelocity(0.2F, 0));
		
//		planets.add(new Planet("M1", 905, 450, 0.001F).setVelocity(0, 0.3F));
//		planets.add(new Planet("M2", 305, 450, 0.001F).setVelocity(0, -0.3F));
//		planets.add(new Planet("M3", 600, 155, 0.001F).setVelocity(0.3F, 0));
//		planets.add(new Planet("M4", 600, 755, 0.001F).setVelocity(-0.3F, 0));
		lst.refreshList();
	}
	
	@Override
	public void update(int ticks)
	{
		if(!paused)
		{
			for(int i = 0; i < planets.size(); i++)
			{
				planets.get(i).updatePlanet(ticks);
			}
			
			for(int i = 0; i < planets.size(); i++)
			{
				for(int j = 0; j < planets.size(); j++)
				{
					if(i != j)
					{
						planets.get(i).acc.addLocal(planets.get(j).acceleration(planets.get(i).pos));
					}
				}
			}
		}
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
		
		for(int i = 0; i < planets.size(); i++)
		{
			g2d.setColor(Color.BLUE);
			Planet p = planets.get(i);
			p.paintPlanet(g2d, debugInfo);

			if(debugInfo)
			{
				g2d.setColor(0F, 0F, 0.7F);
				g2d.drawLine(p.pos.x, p.pos.y, p.pos.x + p.vel.x * 500, p.pos.y + p.vel.y * 500);
				g2d.setColor(0.7F, 0F, 0F);
				g2d.drawLine(p.pos.x, p.pos.y, p.pos.x + p.acc.x * 3000, p.pos.y + p.acc.y * 3000);
			}
		}
		g2d.popMatrix();
		
		if(paused)
		{
			g2d.setColor(Color.RED);
			g2d.drawString("PAUSED", 5, 15);
		}
	}
	
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(pressed && button == MouseEvent.BUTTON2)
		{
			lastPress.set(x, y);
		}
		else if(button == MouseEvent.BUTTON2)
		{
			lastPress.set(-1, -1);
		}
	}
	
	public void mouseMoved(float x, float y)
	{
		if(getHandler().isButton(MouseEvent.BUTTON2))
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
		zoom *= 1 - amt * 0.01F;
		zoom = MathUtil.clamp(zoom, 100F, 0.001F);
	}
	
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if(!pressed)
		{
			if(keyCode == KeyEvent.VK_X)
			{
				paused = !paused;
				if(paused)
				{
					lst.refresh();
				}
				else
				{
					lst.setCompsEnabled(false);
				}
			}
			if(keyCode == KeyEvent.VK_SPACE)
			{
				planets.clear();
				lst.refreshList();
			}
			if(keyCode == KeyEvent.VK_Q)
			{
				for(int i = 0; i < planets.size(); i++)
				{
					planets.get(i).trails.clear();
				}
			}
			if(keyCode == KeyEvent.VK_W)
			{
				debugInfo = !debugInfo;
			}
		}
	}
	
	public static void main(String[] args)
	{
		Gravity game = new Gravity();
		
		GameSettings settings = new GameSettings();
		settings.title = "Gravity";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		System.setProperty("Main.WIDTH", String.valueOf(settings.width));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height));
		Main.initialize(game, settings);
		game.lst.setVisible(true);
	}
}
