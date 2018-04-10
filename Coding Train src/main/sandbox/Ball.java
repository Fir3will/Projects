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
	public final float radius, mass;
	
	public Ball(float radius)
	{
		this.radius = radius;
		mass = radius / 30F;
		pos = new Vector2F();
		vel = new Vector2F();
		
		clr = Color.getHSBColor(Rand.nextFloat(), 0.98F, 0.98F);
	}
	
	public void update(boolean applyGravity)
	{
		if(applyGravity)
		{
			vel.y += 0.01F;
		}
		pos.addLocal(vel);

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
		// COMMENT OUT FOR NO ROOF
		if(pos.y < radius)
		{
			pos.y = radius;
			vel.y = Math.abs(vel.y);
			onGround = true;
		}
		// COMMENT OUT FOR NO ROOF
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
	
	public boolean inBounds(float x, float y)
	{
		return pos.distanceSquared(x, y) < radius * radius;
	}
	
	public boolean collided(Ball other)
	{
		return pos.distance(other.pos) < radius + other.radius;
	}
	
	public void resolveCollision(Ball ball)
	{
		// get the mtd
	    Vector2F delta = pos.subtract(ball.pos);
	    float d = delta.length();
	    // minimum translation distance to push balls apart after intersecting
	    Vector2F mtd = delta.mult(((radius + ball.radius) - d) / d); 


	    // resolve intersection --
	    // inverse mass quantities
	    float im1 = 1 / mass; 
	    float im2 = 1 / ball.mass;

	    // push-pull them apart based off their mass
	    pos.addLocal(mtd.mult(im1 / (im1 + im2)));
	    ball.pos.subtractLocal(mtd.mult(im2 / (im1 + im2)));

	    // impact speed
	    Vector2F v = vel.subtract(ball.vel);
	    float vn = v.dot(mtd.normalizeLocal());

	    // sphere intersecting but moving away from each other already
	    if (vn > 0F) return;

	    // collision impulse
	    float i = (-(1 + RESTITUTION) * vn) / (im1 + im2);
	    Vector2F impulse = mtd.mult(i);

	    // change in momentum
	    vel.addLocal(impulse.mult(im1));
	    ball.vel.subtractLocal(impulse.mult(im2));
	}

	private static float DAMPING = 0.9F;
	private static float RESTITUTION = 0.5F;
}