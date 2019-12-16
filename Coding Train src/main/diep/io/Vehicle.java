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
package main.diep.io;

import java.awt.Color;
import java.awt.Shape;

import com.hk.g2d.G2D;
import com.hk.math.vector.Vector2F;

public class Vehicle
{
	public final DiepIO game;
	public final Vector2F lookingAt;
	public TankType type;
	public double px, py, vx, vy, health, regenDelay;
	public int move, xp;
	public boolean firing, damaged;
	private Barrel[] barrels;
	private double lastHealth;

	public Vehicle(DiepIO game, double x, double y, TankType type)
	{
		this.game = game;
		px = x;
		py = y;
		vx = vy = 0;
		move = 0;
		lookingAt = new Vector2F(1, 0);
		firing = false;
		health = MAX_HEALTH;
		setType(type);
	}
	
	public void updateVehicle(double delta)
	{
		if(lastHealth > health)
		{
			
		}
		
		damaged = health < MAX_HEALTH;
		if(damaged)
		{
			regenDelay -= delta;
			
			if(regenDelay <= 0)
			{
				health = Math.min(health + (MAX_HEALTH / 10) * delta, MAX_HEALTH);
				regenDelay = 0;
			}
		}
		
		lastHealth = health;

		double dvx = 0, dvy = 0;
		if((move & 15) != 0)
		{
			if((move & 1) != 0) dvx++;
			if((move & 2) != 0) dvy++;
			if((move & 4) != 0) dvx--;
			if((move & 8) != 0) dvy--;
			double len = Math.sqrt(dvx * dvx + dvy * dvy);
			
			dvx = dvx / len * MAX_SPEED;
			dvy = dvy / len * MAX_SPEED;

			double d = delta * SLOWDOWN * 3;
			vx = vx * (1 - d) + dvx * d;
			vy = vy * (1 - d) + dvy * d;
		}
		
		double speed = Math.sqrt(vx * vx + vy * vy);
		
		if(speed != 0 && speed > MAX_SPEED)
		{
			vx = vx / speed * MAX_SPEED;
			vy = vy / speed * MAX_SPEED;
		}
		
		px += vx * delta;
		py += vy * delta;

		vx *= 1 - (delta * SLOWDOWN * 3);
		vy *= 1 - (delta * SLOWDOWN * 3);
		
		for(Barrel barrel : barrels)
			barrel.updateBarrel(delta);
		
		if(firing)
			fire();
	}

	public void paintVehicle(G2D g2d)
	{
		double a = Math.toDegrees(lookingAt.subtract((float) px, (float) py).getAngle()) - 90;
		g2d.pushMatrix();

		g2d.translate(px, py);
		g2d.rotate(a);
		for(Barrel barrel : barrels)
		{
			Shape shape = barrel.getShape();
			g2d.setColor(Color.GRAY);
			g2d.enable(G2D.G_FILL);
			g2d.drawShape(shape);
			g2d.disable(G2D.G_FILL);
			
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawShape(shape);
		}

		g2d.setColor(isPlayer() ? Shot.BLUE_TEAM : Shot.RED_TEAM);
		g2d.enable(G2D.G_FILL);
		g2d.drawCircle(-RADIUS, -RADIUS, RADIUS);
		g2d.disable(G2D.G_FILL);
		
		g2d.setColor(isPlayer() ? Shot.DK_BLUE_TEAM : Shot.DK_RED_TEAM);
		g2d.drawCircle(-RADIUS, -RADIUS, RADIUS);
		
		g2d.popMatrix();
		
	}
	
	public void fire()
	{
		double angle = Math.toDegrees(lookingAt.subtract((float) px, (float) py).getAngle()) - 90;
		for(Barrel barrel : barrels)
			barrel.fire(angle);
	}
	
	public void setType(TankType type)
	{
		if(this.type != type)
		{
			this.type = type;
			
			Barrel[] bs = type.getBarrels();
			if(bs == null)
				bs = new Barrel[0];
			int len = bs.length;
			barrels = new Barrel[len];
			for(int i = 0; i < len; i++)
			{
				barrels[i] = bs[i].copy(this);
			}
		}
	}
	
	public void shoot(double x, double y, double vx, double vy, double radius)
	{
		Shot shot = new Shot(this, radius);
		shot.px = px + x;
		shot.py = py + y;
		shot.vx = vx;
		shot.vy = vy;
		shoot(shot);
	}
	
	public void shoot(Shot shot)
	{
		game.shoot(shot);
	}
	
	public double getSpeed()
	{
		return Math.sqrt(vx * vx + vy * vy);
	}
	
	public Barrel[] getBarrels()
	{
		return barrels;
	}
	
	public boolean isPlayer()
	{
		return this == game.player;
	}
	
	public static int getLevelForXp(int xp)
	{
		int i = 0;
		while(xp > levels[i])
			i++;
		return i + 1;
	}
	
	public static int getXpForLevel(int lvl)
	{
		return levels[lvl - 1];
	}
	
	public static int getXpBetween(int lvl1, int lvl2)
	{
		lvl1--;
		lvl2--;
		int a = Math.max(lvl1, lvl2);
		int b = Math.min(lvl1, lvl2);
		
		return levels[a] - levels[b];
	}
	
	private static final int[] levels;
	
	static
	{
		levels = new int[45];
		int sum1, sum2;
		sum1 = 5;
		sum2 = 0;
		for(int i = 1; i <= levels.length; i++)
		{
			levels[i - 1] = sum2;
			sum1 += i * 5 / 3;
			sum2 += sum1;
		}
	}
	
	public static final double RADIUS = 25;
	public static final double MAX_SPEED = 300;
	public static final double SLOWDOWN = 0.9;
	public static final double RELOAD_DELAY = 2D / 3D;
	public static final double SHOT_SPEED = 550;
	public static final double MAX_HEALTH = 100;
	public static final double REGEN_DELAY = 2.5;
}
