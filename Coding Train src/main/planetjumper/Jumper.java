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
package main.planetjumper;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.FloatMath;
import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;

public class Jumper extends GuiScreen
{
	public static final float G = 0.1F;
	public final List<Planetoid> planets;
	public final Player player;
	private final Vector2F[] path;
	private boolean sas, prograde, retrograde;
	private int speed, limit;
	
	public Jumper(Game game)
	{
		super(game);
		
		planets = new ArrayList<>();
		player = new Player(this);
		player.pos.set(-200, 0);
		player.rot = 90;
//		player.vel.set(0, -1.225F);
		player.vel.set(0, -0.864F);
		
		planets.add(new Planetoid("Schtuff", 0, 0, 50F));
//		planets.add(new Planetoid("Schtuff", -200, 0, 50F));
		
		path = new Vector2F[500];
		for(int i = 0; i < path.length; i++)
		{
			path[i] = new Vector2F();
		}
		speed = 1;
	}
	
	@Override
	public void update(double delta)
	{
		for(int i = 0; i < speed; i++)
		{
			update2(delta);
		}
		
		Vector2F pos = player.pos.clone();
		Vector2F vel = player.vel.clone();

		limit = path.length;
		int amt = 20;
		int max = path.length * amt;
		int i = 0;
		for(; i < max; i++)
		{
			for(int j = 0; j < planets.size(); j++)
			{
				vel.addLocal(planets.get(j).acceleration(pos));
			}
			pos.addLocal(vel);
			if(i > 20 && pos.distanceSquared(player.pos) < 4)
			{
				max = Math.min(max, i + 20);
				break;
			}
			if(i % amt == 0)
			{
				path[i / amt].set(pos);
			}
		}
		limit = i / amt;
	}
	
	public void update2(double delta)
	{
		player.updatePlayer(delta);
		
		for(int i = 0; i < planets.size(); i++)
		{
			player.acc.addLocal(planets.get(i).acceleration(player.pos));
		}
		
		if(sas && player.rotSide == 0)
		{
			player.rotVel *= 0.05 * delta + 0.95;
			if(prograde || retrograde)
			{
				float ang = FloatMath.toDegrees(-player.vel.getAngle()) + 180F;
				float f = (prograde ? ang + 90 : ang - 90) - player.rot;
				f  = MathUtil.between(-150F, f, 150F);
				player.rotVel = (float) MathUtil.lerp(player.rotVel, f, 0.1F * delta + 0.9F);
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.DARK_GRAY);
		int offX = (int) (-player.pos.x % 50);
		int offY = (int) (-player.pos.y % 50);
		for(int i = -50; i < g2d.width + 50; i += 50)
		{
			for(int j = -50; j < g2d.height + 50; j += 50)
			{
				g2d.drawRectangle(i + offX, j + offY, 50, 50);
			}
		}
		
		g2d.pushMatrix();
		g2d.translate(g2d.width / 2D - player.pos.x, g2d.height / 2D - player.pos.y);
		
		g2d.setColor(0, 0, 100);
		g2d.setLineWidth(2);
		for(int i = 0; i < Math.min(limit, path.length - 1); i++)
		{
			Vector2F a = path[i];
			Vector2F b = i == limit - 1 ? path[0] : path[i + 1];
			g2d.drawLine(a.x, a.y, b.x, b.y);
		}
		
		g2d.setColor(Color.WHITE);
		g2d.setLineWidth(4);
		for(int i = 0; i < planets.size(); i++)
		{
			Planetoid p = planets.get(i);
			p.paintPlanet(g2d);
		}
		g2d.setLineWidth(1);
		
		player.paintPlayer(g2d);
		g2d.popMatrix();
		
		g2d.setColor(Color.BLACK);
		String s = "Velocity: " + player.vel.length();
		Rectangle2D r = g2d.getStringBounds(s);
		
		g2d.enable(G2D.G_FILL);
		g2d.drawRectangle(0, 0, r.getWidth() + 10, 20);
		g2d.disable(G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawRectangle(0, 0, r.getWidth() + 10, 20);
		g2d.drawString(s, 5, 15);
		if(sas)
		{
			String str = "SAS";
			if(prograde)
			{
				str += ", PROGRADE";
			}
			if(retrograde)
			{
				str += ", RETROGRADE";
			}
			g2d.setColor(Color.GREEN);
			g2d.drawString(str, 5, 35);
		}
		g2d.drawString("Rot: " + player.rot + ", Vel: " + player.rotVel, 5, 50);
		g2d.drawString(FloatMath.toDegrees(-player.vel.getAngle()) + 180F, 5, 65);

		g2d.drawString(speed + "x", 5, game.height - 5);
	}
	
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if(keyCode == KeyEvent.VK_1)
		{
			speed = 1;
		}
		if(keyCode == KeyEvent.VK_2)
		{
			speed = 5;
		}
		if(keyCode == KeyEvent.VK_3)
		{
			speed = 10;
		}
		if(keyCode == KeyEvent.VK_4)
		{
			speed = 25;
		}
		if(keyCode == KeyEvent.VK_5)
		{
			speed = 100;
		}
		
		if(keyCode == KeyEvent.VK_SPACE)
		{
			player.boost = pressed;
		}
		else if(keyCode == KeyEvent.VK_A)
		{
			player.rotSide = pressed ? 1 : 0;
		}
		else if(keyCode == KeyEvent.VK_D)
		{
			player.rotSide = pressed ? -1 : 0;
		}
		if(!pressed)
		{
			if(keyCode == KeyEvent.VK_S)
			{
				sas = !sas;
				if(!sas)
				{
					prograde = retrograde = false;
				}
			}
			if(sas)
			{
				if(keyCode == KeyEvent.VK_Q)
				{
					prograde = !prograde;
					retrograde = false;
				}
				else if(keyCode == KeyEvent.VK_E)
				{
					prograde = false;
					retrograde = !retrograde;
				}
			}
		}
	}
	
	public static void main(String[] args)
	{		
		Settings settings = new Settings();
		settings.title = "Planet Jumper";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Jumper(frame.game));
		frame.launch();
	}
}
