package main.planetjumper;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.hk.math.FloatMath;
import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;
import com.sun.glass.events.KeyEvent;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class Jumper extends Game
{
	public static final float G = 0.1F;
	public final List<Planetoid> planets;
	public final Player player;
	private final Vector2F[] path;
	private boolean sas, prograde, retrograde;
	
	public Jumper()
	{
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
	}
	
	@Override
	public void update(int ticks)
	{
		player.updatePlayer(ticks);
		
		for(int i = 0; i < planets.size(); i++)
		{
			player.acc.addLocal(planets.get(i).acceleration(player.pos));
		}
		
		Vector2F pos = player.pos.clone();
		Vector2F vel = player.vel.clone();

		int amt = 20;
//		int amt = 2;
		for(int i = 0; i < path.length * amt; i++)
		{
			for(int j = 0; j < planets.size(); j++)
			{
				vel.addLocal(planets.get(j).acceleration(pos));
			}
			pos.addLocal(vel);
			if(i % amt == 0)
			{
				path[i / amt].set(pos);
			}
		}
		
		if(sas)
		{
			player.rotVel += -MathUtil.sign(player.rotVel) / 50F;
			if(prograde || retrograde)
			{
				float ang = FloatMath.toDegrees(-player.vel.getAngle()) + 180F;
				float f = (prograde ? ang : ang + 180F) - player.rot;
				f  = MathUtil.clamp(f, 10F, -10F);
				player.rotVel = MathUtil.lerp(player.rotVel, f, 0.9F);
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
		for(int i = 0; i < path.length - 1; i++)
		{
			Vector2F a = path[i];
			Vector2F b = path[i + 1];
			g2d.drawLine(a.x, a.y, b.x, b.y);
		}
		
		g2d.setColor(Color.WHITE);
		for(int i = 0; i < planets.size(); i++)
		{
			Planetoid p = planets.get(i);
			p.paintPlanet(g2d);
		}
		
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
	}
	
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if(pressed)
		{
			if(keyCode == KeyEvent.VK_A)
			{
				player.rotVel += 0.05F;
			}
			else if(keyCode == KeyEvent.VK_D)
			{
				player.rotVel -= 0.05F;
			}
			else if(keyCode == KeyEvent.VK_SPACE)
			{
				Vector2F v = new Vector2F(0.01F, 0F);
				v.rotateAround(FloatMath.toRadians(player.rot), true);
				player.acc.addLocal(v);
			}
		}
		else
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
		Jumper game = new Jumper();
		
		GameSettings settings = new GameSettings();
		settings.title = "Planet Jumper";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1600;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		System.setProperty("Main.WIDTH", String.valueOf(settings.width));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height));
		Main.initialize(game, settings);
	}
}
