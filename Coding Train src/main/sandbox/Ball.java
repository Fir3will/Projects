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
package main.sandbox;

import java.awt.Color;

import com.hk.g2d.Game;
import com.hk.math.vector.Color3F;
import com.hk.math.vector.Vector2F;

public class Ball
{
	public final Game game;
	public final Vector2F pos, vel;
	private final Color3F origClr;
	public final Color3F clr;
	public final float radius, mass;
	
	public Ball(Game game, float radius, float hue)
	{
		this.game = game;
		this.radius = radius;
		mass = radius / 30F;
		pos = new Vector2F();
		vel = new Vector2F();
		
		Color c = Color.getHSBColor(hue, 0.98F, 0.98F);
		clr = new Color3F(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F);
		origClr = new Color3F(clr);
	}
	
	public void update(double delta, boolean applyGravity)
	{
		if(applyGravity)
		{
			vel.y += 9.81 * delta;
		}
		pos.addLocal(vel);

		boolean onGround = false;
		if(pos.x < radius)
		{
			pos.x = radius;
			vel.x = Math.abs(vel.x);
			onGround = true;
		}
		if(pos.x > game.width - radius)
		{
			pos.x = game.width - radius;
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
		if(pos.y > game.height - radius)
		{
			pos.y = game.height - radius;
			vel.y = -Math.abs(vel.y);
			onGround = true;
		}
		
		if(onGround)
		{
			vel.multLocal(DAMPING);
		}
		clr.interpolateLocal(origClr, 0.1F);
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
		ball.clr.set(clr.interpolateLocal(ball.clr, 0.5F));
		
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

	private static float DAMPING = 0.9F;
	private static float RESTITUTION = 0.5F;
}
