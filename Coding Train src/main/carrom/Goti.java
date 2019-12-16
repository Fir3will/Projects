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
package main.carrom;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.math.FloatMath;
import com.hk.math.vector.Vector2F;

public class Goti
{
	public final Type type;
	public final Vector2F pos, vel;
	public final float radius, mass;
	
	public Goti(Type type, float x, float y)
	{
		this.type = type;
		radius = type == Type.STEGGAR ? 20 : 10;
		mass = type == Type.STEGGAR ? 3 : 1;
		pos = new Vector2F(x, y);
		vel = new Vector2F();
	}
	
	public Goti(Goti cpy)
	{
		this.type = cpy.type;
		radius = cpy.radius;
		mass = cpy.mass;
		pos = new Vector2F(cpy.pos);
		vel = new Vector2F(cpy.vel);
	}
	
	public void updateGoti(double dt)
	{
		vel.multLocal(1F - ((float) dt * (type == Type.STEGGAR ? 2F : 0.85F)));
		pos.addLocal(vel.mult((float) dt));
	}
	
	public void paintGoti(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		g2d.setColor(type.clr);
		g2d.drawCircle(pos.x - radius, pos.y - radius, radius);
		g2d.disable(G2D.G_FILL);
		g2d.setColor(Color.GRAY);
		g2d.drawCircle(pos.x - radius, pos.y - radius, radius);
	}
	
	public float leastDist(Vector2F[] vs)
	{
		if(vs.length == 0)
			return 0;
		float lsq = Float.MAX_VALUE;
		for(int i = 0; i < vs.length; i++)
		{
			float dst = pos.distanceSquared(vs[i]);
			if(dst < lsq)
			{
				lsq = dst;
			}
		}
		return FloatMath.sqrt(lsq);
	}
	
	public boolean collided(Goti other)
	{
		return pos.distance(other.pos) < radius + other.radius;
	}
	
	public void resolveCollision(Goti ball)
	{		
		// get the mtd
	    Vector2F delta = pos.subtract(ball.pos);
	    float d = delta.length();
	    // minimum translation distance to push balls apart after intersecting
	    Vector2F mtd = delta.mult(((radius + ball.radius) - d) / d); 


	    // resolve intersection
	    // inverse mass quantities
	    float im1 = 1 / mass; 
	    float im2 = 1 / ball.mass;

	    // push-pull them apart based off their mass
	    pos.addLocal(mtd.mult(im1 / (im1 + im2)));
	    ball.pos.subtractLocal(mtd.mult(im2 / (im1 + im2)));

	    // impact speed
	    float vn = vel.subtract(ball.vel).dot(mtd.normalizeLocal());

	    // sphere intersecting but moving away from each other already
	    if (vn > 0F) return;

	    // collision impulse
	    float i = (-(1 + RESTITUTION) * vn) / (im1 + im2);
	    Vector2F impulse = mtd.mult(i);

	    // change in momentum
	    vel.addLocal(impulse.mult(im1));
	    ball.vel.subtractLocal(impulse.mult(im2));
	}
	
	public static enum Type
	{
		WHITE(Color.WHITE, 10),
		BLACK(Color.BLACK, 5),
		QUEEN(Color.RED, 50),
		STEGGAR(Color.YELLOW, -1);
		
		public final Color clr;
		public final int reward;
		
		private Type(Color clr, int reward)
		{
			this.clr = clr;
			this.reward = reward;
		}
	}

	private static float RESTITUTION = 0.95F;
}
