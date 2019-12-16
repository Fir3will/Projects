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
package main.wingsio;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.FloatMath;
import com.hk.math.Rand;
import com.hk.math.vector.VecMath;
import com.hk.math.vector.Vector2F;

public class WingsIO extends GuiScreen
{
	private final List<Player> players;
	public final Player player;
	private final Vector2F mv;
	public final List<Bullet> bullets;
	private final Vector2F cam;
	
	public WingsIO(Game game)
	{
		super(game);
		players = new ArrayList<>();
		players.add(player = new Player(this, false));
		for(int i = 0; i < 2; i++)
		{
			Player p = new Player(this, true);
			p.pos.set(Rand.nextFloat(-size, size), Rand.nextFloat(-size, size));
			players.add(p);
		}
		mv = new Vector2F();
		bullets = new ArrayList<>();
		cam = new Vector2F();
	}
	
	@Override
	public void update(double delta)
	{
		cam.interpolateLocal(player.pos, (float) delta * 10);

		for(Player player : players)
		{
			if(player.health == 0)
			{
				respawn(player);
			}
			else
			{
				player.updatePlayer(delta);
			}
		}
		Vector2F v = mv.subtract(game.width / 2, game.height / 2);
		boolean good = Float.isFinite(v.lengthSquared());
		if(good)
			player.rot = -v.getAngle() + FloatMath.PI / 2;
		
		if(good && v.length() > 100)
		{
			player.dir = v;
		}
		else
		{
			player.dir = null;
			player.vel.multLocal((float) (1F - delta));
		}
		
		AffineTransform aft = new AffineTransform();
		for(int i = bullets.size() - 1; i >= 0; i--)
		{
			Bullet b = bullets.get(i);
			Vector2F start = b.pos.clone();
			Vector2F end = b.pos.addLocal(b.vel.mult((float) delta));
			if(!start.equals(end))
			{
				for(int j = 0; j < players.size(); j++)
				{
					Player plr = players.get(j);
					if(b.shooter == plr)
						continue;
					
					aft.setToIdentity();
					aft.translate(plr.pos.x, plr.pos.y);
					aft.rotate(plr.rot);
					Shape playerBounds = aft.createTransformedShape(Player.shape);
					Vector2F c = VecMath.closestToSegment(start, end, plr.pos);
					if(playerBounds.contains(c.x, c.y))
					{
						b.hit = plr;
						plr.hit(b.getDamage(plr));
						b.life = 0;
					}
				}
			}
		
			if(b.updateBullet(delta))
			{
				bullets.remove(i);
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.drawString(player.pos, 5, 15);
		
		g2d.pushMatrix();
		g2d.translate(-cam.x + g2d.width / 2, -cam.y + g2d.height / 2);
		float iv = size / 10;
		g2d.translate(-size, -size);

		g2d.setColor(Color.BLACK);
		g2d.enable(G2D.G_FILL);
		g2d.drawRect(-200, -200, size * 2 + 400, size * 2 + 400);
		g2d.disable(G2D.G_FILL);
		
		g2d.setColor(Color.DARK_GRAY);
		for(int x = 1; x < 20; x++)
		{
			g2d.drawLine(x * iv, 0, x * iv, size * 2);
		}
		for(int y = 1; y < 20; y++)
		{
			g2d.drawLine(0, y * iv, size * 2, y * iv);
		}
		g2d.translate(size, size);
		
		g2d.setColor(Color.WHITE);
		g2d.drawRect(-size, -size, size * 2, size * 2);

		float lw = g2d.getLineWidth();
		g2d.setLineWidth(4F);
		for(Player player : players)
		{
			player.paintPlayer(g2d);
		}
		g2d.setLineWidth(3F);
		for(Bullet b : bullets)
		{
			b.paintBullet(g2d);
		}
		g2d.setLineWidth(lw);
				
		g2d.popMatrix();

		g2d.setColor(1F, 1F, 1F, 0.3F);
		lw = g2d.getLineWidth();
		g2d.setLineWidth(3F);
		g2d.drawCircle(g2d.width / 2 - 100, g2d.height / 2 - 100, 100);
		g2d.setLineWidth(lw);
		g2d.enable(G2D.G_FILL);
		g2d.setColor(1F, 1 - (float) player.hitDelay, 1 - (float) player.hitDelay);
		g2d.drawRect(10, g2d.height - 60, 150, 50);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(12, g2d.height - 58, 146, 46);
		
		g2d.setColor(Color.RED);
		g2d.drawRect(12, g2d.height - 58, 146 * (player.health / 100), 46);
		g2d.disable(G2D.G_FILL);

		g2d.enable(G2D.G_CENTER);
		g2d.setColor(Color.WHITE);
		g2d.drawString((int) player.health, 85, g2d.height - 35);
		g2d.disable(G2D.G_CENTER);
	}
	
	public void mouseMoved(float x, float y)
	{
		mv.set(x, y);
	}
	
	public void mouse(float x, float y, boolean pressed)
	{
		player.firing = pressed;
	}

	private void respawn(Player plr)
	{
		plr.pos.set(size, 0);
		plr.pos.rotateAround(Rand.nextFloat(FloatMath.PI), Rand.nextBoolean());
		plr.health = 100;
	}
		
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Wings.io";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = 576;
		settings.showFPS = true;
		settings.background = new Color(127, 0, 0);
		settings.maxFPS = 60;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new WingsIO(frame.game));
		frame.launch();
	}
	
	public static final float size = 1500;
}
