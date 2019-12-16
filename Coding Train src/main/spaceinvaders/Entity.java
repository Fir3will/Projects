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
package main.spaceinvaders;

import java.awt.Color;
import java.awt.Rectangle;

import com.hk.g2d.G2D;
import com.hk.math.vector.Vector2F;

public class Entity
{
	public final SpaceInvaders game;
	public final Vector2F pos, vel;
	public float width, height;
	public int health;
	public boolean isDead;
	
	public Entity(SpaceInvaders game, float width, float height)
	{
		this.game = game;
		this.pos = new Vector2F();
		this.vel = new Vector2F();
		this.width = width;
		this.height = height;
		health = 1;
	}
	
	public void update(float step)
	{
		pos.addLocal(vel.mult(step));
	}
	
	public void paint(G2D g2d)
	{
		g2d.pushColor();
		g2d.setColor(Color.WHITE);
//		g2d.enable(G2D.G_FILL);
		g2d.drawRectangle(pos.x, pos.y, width, height);
//		g2d.disable(G2D.G_FILL);
		g2d.popColor();
	}
	
	public void onCollide(Entity other)
	{}
	
	public boolean onDeath()
	{
		return true;
	}
	
	public boolean intersects(Entity other)
	{
		a.setRect(pos.x, pos.y, width, height);
		b.setRect(other.pos.x, other.pos.y, other.width, other.height);
		return a.intersects(b);
	}
	
	public boolean isDead()
	{
		return isDead || health <= 0;
	}
	
	private static final Rectangle a = new Rectangle(), b = new Rectangle();
}
