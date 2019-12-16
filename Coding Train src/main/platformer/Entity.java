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
package main.platformer;

import com.hk.math.shape.Rectangle;
import com.hk.math.vector.Vector2F;

public class Entity
{
	public final World world;
	public final Vector2F pos, prevPos, vel, prevVel;
	public final Rectangle bounds;
	
	public Entity(World world)
	{
		this.world = world;
		pos = new Vector2F();
		prevPos = new Vector2F();
		vel = new Vector2F();
		prevVel = new Vector2F();
		
		bounds = new Rectangle(1, 1.8F);
	}
	
	public void updateEntity(double delta)
	{
		prevPos.set(pos);
		prevVel.set(vel);

		vel.y += 9.81 * delta;
//		pos.x += vel.x * delta;
//		pos.y += vel.y * delta;
		
		checkCollisions(delta);
		
		bounds.x = pos.x;
		bounds.y = pos.y;
	}
	
	private void checkCollisions(double delta)
	{
		Vector2F v = vel.mult((float) delta);
		float len = v.length();
		int amt = (int) Math.ceil(len);
		if(amt == 0)
			return;
		
		Vector2F dir = v.divide(amt);
		
		while(amt-- > 0)
		{
			for(int i = 0; i < 2; i++)
			{
				pos.setIndx(i, pos.getIndx(i) + dir.getIndx(i));
			}
		}
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
}
