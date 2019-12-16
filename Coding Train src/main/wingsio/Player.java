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
import java.awt.Polygon;

import com.hk.g2d.G2D;
import com.hk.math.FloatMath;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

class Player
{
	public final WingsIO game;
	public final Vector2F pos, vel;
	public final PlayerAI ai;
	public Vector2F dir;
	public float rot, health;
	public Weapon equipped;
	public boolean firing, abandoning;
	private double trailDelay, fireDelay;
	public double hitDelay;
	public static final Polygon shape = new Polygon();
	
	Player(WingsIO game, boolean hasAI)
	{
		this.game = game;
		this.ai = hasAI ? new PlayerAI(this) : null;
		health = 100;
		pos = new Vector2F();
		vel = new Vector2F();
		
		equipped = Weapon.BASIC;
	}
	
	public void updatePlayer(double dt)
	{
		hitDelay = Math.max(hitDelay - dt * 2, 0);
		
		if(dir != null)
		{
			vel.set(dir);
			vel.normalizeLocal();
			vel.multLocal(150);
		}
		pos.addLocal(vel.mult((float) dt));
		abandoning = pos.x < -1700 || pos.x > 1700 || pos.y < -1700 || pos.y > 1700;
		if(abandoning)
		{
			hit((float) (dt * 25));
		}
		
		trailDelay += dt * 1000;
		while(trailDelay > 30)
		{
			trailDelay -= Rand.nextInt(30) + 1;
			Vector2F v = new Vector2F(vel.length() > 140 ? 80 : -20, 0).rotateAround(rot, true);
			v.rotateAround(FloatMath.toRadians(Rand.nextInt(15)), Rand.nextBoolean());
			Vector2F p = new Vector2F(15, Rand.nextFloat(-6, 6));
			p.rotateAround(rot + FloatMath.PI, true);
			p.addLocal(pos);
			game.bullets.add(new Trail(p, v, this));
		}
		fireDelay = Math.min(fireDelay + dt, equipped.delay);
		while(firing && fireDelay >= equipped.delay)
		{
			fireDelay -= equipped.delay;
			Vector2F p = new Vector2F(15, Rand.nextFloat(-3, 3));
			p.rotateAround(rot, true);
			p.addLocal(pos);
			game.bullets.add(new Bullet(game, p, new Vector2F(1250, 0).rotateAround(rot, true), this, equipped));
		}
		
		if(ai != null)
		{
			ai.updateAI(dt);
		}
	}
	
	public void hit(float dmg)
	{
		health = Math.max(health - dmg, 0);
		hitDelay = 1;
	}
	
	public void paintPlayer(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		g2d.pushMatrix();
		g2d.translate(pos.x, pos.y);
		g2d.rotateR(rot);
		g2d.setColor(Color.GRAY);
		g2d.drawShape(shape);
		g2d.disable(G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawShape(shape);
		g2d.setColor(Color.YELLOW);
		g2d.drawLine(-15, 10, -15, -10);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(15, 3, 15, -3);

		g2d.enable(G2D.G_FILL);
		g2d.setColor(1F, 0F, 0F, (float) hitDelay  * 0.8F);
		g2d.scale(1.1, 1.1);
		g2d.drawShape(shape);
		g2d.scale(1 / 1.1, 1 / 1.1);
		g2d.disable(G2D.G_FILL);
		g2d.popMatrix();
	}
	
	static
	{
		shape.addPoint(-15, 10);
		shape.addPoint(15, 3);
		shape.addPoint(15, -3);
		shape.addPoint(-15, -10);
	}
}
