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

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public abstract class Barrel
{
	protected Vehicle vehicle;
	
	protected Barrel(Vehicle vehicle)
	{
		this.vehicle = vehicle;
	}
	
	public final void updateBarrel(double delta)
	{
		if(isTemplate())
			throw new NullPointerException();
		
		update(delta);
	}
	
	protected abstract void update(double delta);
	
	public abstract void fire(double angle);
	
	public abstract Shape getShape();
	
	public abstract Barrel copy(Vehicle vehicle);
	
	public final boolean isTemplate()
	{
		return vehicle == null;
	}
	
	public static Barrel create(double width, double height, double radius, double angle)
	{
		return create(width, height, radius, angle, 0, 0);
	}
	
	public static Barrel create(double width, double height, double radius, double angle, double xOff, double yOff)
	{
		RectBarrel rb = new RectBarrel(null);
		rb.width = width;
		rb.height = height;
		rb.radius = radius;
		rb.angle = angle;
		rb.xOff = xOff;
		rb.yOff = yOff;
		return rb;
	}
	
	private static class RectBarrel extends Barrel
	{
		private double width, height, radius, angle, xOff, yOff;
		private double fireDelay;
		
		private RectBarrel(Vehicle vehicle)
		{
			super(vehicle);
		}
		
		@Override
		public void update(double delta)
		{
			fireDelay = Math.max(0, fireDelay - delta);
		}

		@Override
		public void fire(double angle)
		{
			if(isTemplate())
				throw new NullPointerException();
			
			if(fireDelay <= 0)
			{
				fireDelay = Vehicle.RELOAD_DELAY;
				
				double r = Math.toRadians(this.angle + angle);
				double ac = Math.cos(r);
				double as = Math.sin(r);
				r = (radius + width / 2);
				double x = ac * r + xOff;
				double y = as * r + yOff;
				double vx = ac * Vehicle.SHOT_SPEED;
				double vy = as * Vehicle.SHOT_SPEED;

				vehicle.vx -= vx;
				vehicle.vy -= vy;
				
				vehicle.shoot(x, y, vx, vy, height / 2);
			}
		}
		
		@Override
		public Shape getShape()
		{
			double d = fireDelay / Vehicle.RELOAD_DELAY;
			d = (d > 0.5 ? 1 - d : d) * radius / 5;
			Shape shape = new Rectangle2D.Double(width / -2 + radius - d + xOff, height / -2 + yOff, width, height);
			shape = AffineTransform.getRotateInstance(Math.toRadians(angle)).createTransformedShape(shape);
			return shape;
		}

		@Override
		public Barrel copy(Vehicle vehicle)
		{
			RectBarrel rb = new RectBarrel(vehicle);
			rb.width = width;
			rb.height = height;
			rb.radius = radius;
			rb.angle = angle;
			rb.xOff = xOff;
			rb.yOff = yOff;
			return rb;
		}
	}
}
