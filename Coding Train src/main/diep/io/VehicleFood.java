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

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import com.hk.g2d.G2D;
import com.hk.math.Rand;

public class VehicleFood extends Vehicle
{
	public final VehicleFoodType aiType;
	private boolean cannotSet = false;
	private final double fvx, fvy;
	private double rot, vrot;
	
	public VehicleFood(DiepIO game, double x, double y, VehicleFoodType aiType)
	{
		super(game, x, y, TankType.AI);
		cannotSet = true;
		this.aiType = aiType;
		rot = Rand.nextDouble(360);
		vrot = Rand.nextDouble(5, 15) * (Rand.nextBoolean() ? 1 : -1);
		
		double ang = Rand.nextDouble(Math.PI * 2);
		fvx = vx = Math.cos(ang) * 12;
		fvy = vy = Math.sin(ang) * 12;
	}

	@Override
	public void updateVehicle(double delta)
	{
		double slowdown = SLOWDOWN + (1 - SLOWDOWN) * delta;
		vx = vx * slowdown + fvx * (1 - slowdown);
		vy = vy * slowdown + fvy * (1 - slowdown);
		super.updateVehicle(delta);
		
		rot += vrot * delta;
	}

	@Override
	public void paintVehicle(G2D g2d)
	{
		float r = aiType.r, g = aiType.g, b = aiType.b;
		g2d.pushMatrix();

		g2d.translate(px, py);
		g2d.rotate(rot);

		g2d.setColor(r, g, b);
		g2d.enable(G2D.G_FILL);
		g2d.drawShape(aiType.shape);
		g2d.disable(G2D.G_FILL);
		
		r -= 0.3F;
		g -= 0.3F;
		b -= 0.3F;
		g2d.setColor(r, g, b);
		g2d.drawShape(aiType.shape);

		g2d.popMatrix();
	}

	@Override
	public void fire()
	{
		throw new UnsupportedOperationException("Cannot fire");
	}

	@Override
	public void setType(TankType type)
	{
		if(cannotSet)
			throw new UnsupportedOperationException("Cannot set type");
		else
			super.setType(type);
	}

	@Override
	public void shoot(Shot shot)
	{
		throw new UnsupportedOperationException("Cannot shoot");
	}

	@Override
	public boolean isPlayer()
	{
		return false;
	}
	
	public static enum VehicleFoodType
	{
		SQUARE(10, 0.8F, 0.8F, 0.3F, 4),
		TRIANGLE(25, 0.9F, 0.4F, 0.4F, 3),
		PENTAGON(130, 0.4F, 0.4F, 0.9F, 5);
		
		public final int xp;
		public final Shape shape;
		public final float r, g, b;
		
		private VehicleFoodType(int xp, float r, float g, float b, int verts)
		{
			this.xp = xp;
			this.r = r;
			this.g = g;
			this.b = b;
			shape = getShape(verts);
		}

		private Shape getShape(int verts)
		{
			Polygon p = new Polygon();
			double x, y, a;
			for(int i = 0; i < verts; i++)
			{
				a = i * Math.PI * 2 / verts;
				x = Math.cos(a) * verts * 30;
				y = Math.sin(a) * verts * 30;
				p.addPoint((int) x, (int) y);
			}
			double third = 1D / 4D;
			return AffineTransform.getScaleInstance(third, third).createTransformedShape(p);
		}
		
		public static VehicleFoodType getOnChance()
		{
			int r = Rand.nextInt(6);
			if(r < 3)
			{
				return SQUARE;
			}
			else if(r < 5)
			{
				return TRIANGLE;
			}
			else
			{
				return PENTAGON;
			}
		}
	}
}
