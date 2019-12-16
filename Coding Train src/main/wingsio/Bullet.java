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

import com.hk.g2d.G2D;
import com.hk.math.FloatMath;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

class Bullet
{
	public final WingsIO game;
	public final Weapon type;
	public final Vector2F pos, vel;
	public final Player shooter;
	public Player hit;
	protected double max;
	protected double life;

	Bullet(WingsIO game, Vector2F pos, Vector2F vel, Player shooter, Weapon type)
	{
		this.game = game;
		this.pos = pos;
		this.vel = vel;
		this.shooter = shooter;
		this.type = type;
		max = life = 1;
	}
	
	public boolean updateBullet(double dt)
	{
		if(life <= 0)
		{
			pos.subtractLocal(vel.mult((float) dt));
		}
		life -= dt;
		
		return hit == null ? life < 0 : life < -0.3;
	}
	
	public void paintBullet(G2D g2d)
	{	
		Vector2F v = vel.normalize().multLocal(3);
		
		if(life < 0)
		{
			float tot = 0;
			v.rotateAround(FloatMath.PI, true);
			float f = (float) (-life * 30);
			g2d.setColor(Color.WHITE);
			while(tot < FloatMath.PI)
			{
				float r = Rand.nextFloat(FloatMath.PI);
				tot += r;
				v.rotateAround(r, false);
				g2d.drawPoint(pos.x - v.x * Rand.nextFloat(f), pos.y - v.y * Rand.nextFloat(f));
			}
		}
		else
		{
			g2d.setColor(1F, 1F, 0F / 3F);
			g2d.drawLine(pos.x, pos.y, pos.x - v.x * 4, pos.y - v.y * 4);
			g2d.setColor(1F, 1F, 1F / 3F);
			g2d.drawLine(pos.x, pos.y, pos.x - v.x * 3, pos.y - v.y * 3);
			g2d.setColor(1F, 1F, 2F / 3F);
			g2d.drawLine(pos.x, pos.y, pos.x - v.x * 2, pos.y - v.y * 2);
			g2d.setColor(1F, 1F, 3F / 3F);
			g2d.drawLine(pos.x, pos.y, pos.x - v.x * 1, pos.y - v.y * 1);
		}
	}
	
	public float getDamage(Player against)
	{
		return life <= 0 ? 0 : type.damage;
	}
}
