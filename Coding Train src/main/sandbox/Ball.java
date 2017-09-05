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
package main.sandbox;

import java.awt.Color;

import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.Main;

public class Ball
{
	public final Vector2F pos, vel;
	public final Color clr;
	public final float radius;
	
	public Ball(float radius)
	{
		this.radius = radius;
		pos = new Vector2F();
		vel = new Vector2F();
		
		clr = Color.getHSBColor(Rand.nextFloat(), 0.98F, 0.98F);
	}
	
	public void update(float df)
	{
		vel.y += 9.81F * df;
		pos.addLocal(vel.x * df, vel.y * df);

		boolean onGround = false;
		if(pos.x < radius)
		{
			pos.x = radius;
			vel.x = Math.abs(vel.x);
			onGround = true;
		}
		if(pos.x > Main.WIDTH - radius)
		{
			pos.x = Main.WIDTH - radius;
			vel.x = -Math.abs(vel.x);
			onGround = true;
		}
		if(pos.y < radius)
		{
			pos.y = radius;
			vel.y = Math.abs(vel.y);
			onGround = true;
		}
		if(pos.y > Main.HEIGHT - radius)
		{
			pos.y = Main.HEIGHT - radius;
			vel.y = -Math.abs(vel.y);
			onGround = true;
		}
		
		if(onGround)
		{
			vel.multLocal(DAMPING);
		}
	}
	
	public boolean collided(Ball other)
	{
		return pos.distance(other.pos) < radius + other.radius;
	}
	
	public void resolveCollision(float df, Vector2F otherPos, Vector2F otherVel, float otherRadius)
	{
		// get the mtd
	    Vector2F delta = (pos.subtract(otherPos));
	    float d = delta.length();
	    // minimum translation distance to push balls apart after intersecting
	    Vector2F mtd = delta.mult(((radius + otherRadius) - d) / d); 

	    // resolve intersection --
	    // inverse mass quantities
	    float im1 = 1 / 1; 
	    float im2 = 1 / 1;

	    // push-pull them apart based off their mass
	    pos.addLocal(mtd.mult(im1 / (im1 + im2)).mult(df));

	    // impact speed
	    Vector2F v = (vel.subtract(otherVel));
	    float vn = v.dot(mtd.normalize());

	    // sphere intersecting but moving away from each other already
	    if (vn > 0F) return;

	    // collision impulse
	    float i = (-(1F + RESTITUTION) * vn) / (im1 + im2);
	    Vector2F impulse = mtd.mult(i);

	    // change in momentum
	    this.vel.addLocal(impulse.mult(im1).mult(df));
	}

	private static float DAMPING = 0.9F;
	private static float RESTITUTION = 0.85F;
}
