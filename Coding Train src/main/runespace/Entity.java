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
package main.runespace;

public class Entity
{
	public final World world;
	private final Model mdl;
	public int xCoord, yCoord, dx, dy;
	public Direction direction;
	public boolean moving, still, sprinting;
	protected boolean toRemove;
	private double moveDelay;
	public int maxHealth, health;
	public double stamina;

	public Entity(World world, Model mdl)
	{
		this.world = world;
		this.mdl = mdl;
		maxHealth = health = 100;
		stamina = 100;

		moving = still = false;
		direction = Direction.NORTH;
	}

	public void updateEntity(double delta)
	{
		if(!sprinting || !moving)
			stamina = Math.min(stamina + delta, 100);
		
		health -= 1;
		if(health <= 0)
			health = maxHealth;
		
		moveDelay = Math.max(0, moveDelay - delta);
		if(!still && moving && moveDelay <= 0)
		{
			if(sprinting)
			{
				stamina = Math.max(stamina - delta * 5, 0);
				if(stamina == 0)
					sprinting = false;
			}
			
			if(direction.xOff < 0)
			{
				dx -= sprinting ? 2 : 1;
				while(dx < 0)
				{
					xCoord--;
					dx += 20;
				}
			}
			else if(direction.xOff > 0)
			{
				dx +=  sprinting ? 2 : 1;
				while(dx > 19)
				{
					xCoord++;
					dx -= 20;
				}
			}

			if(direction.yOff < 0)
			{
				dy -=  sprinting ? 2 : 1;
				while(dy < 0)
				{
					yCoord--;
					dy += 20;
				}
			}
			else if(direction.yOff > 0)
			{
				dy +=  sprinting ? 2 : 1;
				while(dy > 19)
				{
					yCoord++;
					dy -= 20;
				}
			}
			moveDelay += 0.03125;
		}
		
//		if(!moving)
//		{
//			if(dx > 9)
//			{
//				xCoord++;
//			}
//			dx = 0;
//			if(dy > 9)
//			{
//				yCoord++;
//			}
//			dy = 0;
//		}
	}

	public boolean toRemove()
	{
		return toRemove;
	}

	public Model getModel()
	{
		return mdl;
	}
}
