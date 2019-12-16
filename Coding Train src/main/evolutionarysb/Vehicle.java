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
package main.evolutionarysb;

import java.awt.Color;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class Vehicle implements Cloneable
{
	private final EvolutionarySB game;
	public final float mutationRate = 0.01F;
	
	public final Vector2F pos, vel, acc;
	public final float[] dna = new float[7];
	public float health = 1F;
	public int ticksExisted = 0;
	
	public Vehicle(EvolutionarySB game, float[] dna)
	{
		this.game = game;
		
		if(dna == null)
		{
			this.dna[0] = Rand.nextFloat(-2, 2);
			this.dna[1] = Rand.nextFloat(-2, 2);
			this.dna[2] = Rand.nextFloat(100);
			this.dna[3] = Rand.nextFloat(100);
			this.dna[4] = Rand.nextFloat(3, 7);
			this.dna[5] = Rand.nextFloat(0.3F, 0.7F);
			this.dna[6] = Rand.nextFloat(0.3F, 0.7F);
		}
		else
		{
			this.dna[0] = dna[0] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(0.2F) - 0.1F : 0F);
			this.dna[1] = dna[1] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(0.2F) - 0.1F : 0F);
			this.dna[2] = dna[2] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(20) - 10 : 0F);
			this.dna[3] = dna[3] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(20) - 10 : 0F);
			this.dna[4] = dna[4] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(2) - 1 : 0F);
			this.dna[5] = dna[5] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(0.2F) - 0.1F : 0F);
		}
		pos = new Vector2F();
		vel = Vector2F.randUnitVector().multLocal(this.dna[4]);
		acc = new Vector2F();
	}
	
	public void update()
	{
		ticksExisted++;
		health -= 0.0025F;
		
		if(acc.length() > dna[5])
		{
			acc.normalizeLocal().multLocal(dna[5]);
		}
		vel.addLocal(acc);
		if(vel.length() > dna[4])
		{
			vel.normalizeLocal().multLocal(dna[4]);
		}
		pos.addLocal(vel);
		acc.zero();
	}
	
	public void paint(G2D g2d, boolean debug)
	{
		g2d.enable(G2D.G_FILL | G2D.G_CENTER);

		g2d.setColor(1F - health, health, 0F);
		
		double ang = -vel.getAngle() + Math.PI / 2F;
		g2d.rotateR(ang, pos.x, pos.y);
		g2d.drawRectangle(pos.x, pos.y, 30, 15);

		g2d.disable(G2D.G_FILL | G2D.G_CENTER);

		if(debug)
		{
			g2d.setColor(Color.GREEN);
			g2d.drawLine(pos.x, pos.y, pos.x + dna[0] * 20, pos.y);
			g2d.setColor(Color.RED);
			g2d.drawLine(pos.x, pos.y, pos.x + dna[1] * 20, pos.y);
		}

		g2d.rotateR(-ang, pos.x, pos.y);
		
		if(debug)
		{
			g2d.enable(G2D.G_CENTER);
	
			g2d.setColor(Color.GREEN);
			g2d.drawCircle(pos.x, pos.y, dna[2]);
			
			g2d.setColor(Color.RED);
			g2d.drawCircle(pos.x, pos.y, dna[3]);
	
			g2d.disable(G2D.G_CENTER);
		}
	}
	
	public void act(List<Vector2F> good, List<Vector2F> bad)
	{
		Vector2F steerG = eat(good, 0.2F, dna[2]).multLocal(dna[0]);
		Vector2F steerB = eat(bad, -0.5F, dna[3]).multLocal(dna[1]);

		acc.addLocal(steerG);
		acc.addLocal(steerB);
	}
	
	public Vector2F eat(List<Vector2F> lst, float nutrition, float perception)
	{
		float minDst = Float.MAX_VALUE;
		Vector2F closest = null;
		
		for(int i = 0; i < lst.size(); i++)
		{
			Vector2F v = lst.get(i);
			float dst = v.distance(pos);
			
			if(dst < dna[4])
			{
				lst.remove(i);
				i--;
				health = MathUtil.between(0F, health + nutrition, 1F);
			}
			else if(dst < minDst && dst < perception)
			{
				minDst = dst;
				closest = v;
			}
		}
		
		if(closest != null)
		{
			return seek(closest);
		}
		
		return new Vector2F();
	}
	
	public Vector2F seek(Vector2F target)
	{
		Vector2F desired = target.subtract(pos);
		if(desired.length() > dna[4])
		{
			desired.normalizeLocal().multLocal(dna[4]);
		}
		desired.subtractLocal(vel);
		if(desired.length() > dna[5])
		{
			desired.normalizeLocal().multLocal(dna[5]);
		}
		return desired;
	}
	
	public void checkBoundaries()
	{
		float thresh = -5;
		Vector2F desired = null;
		
		if(pos.x < thresh)
		{
			desired = new Vector2F(dna[4], vel.y);
		}
		else if(pos.x > game.game.width - thresh)
		{
			desired = new Vector2F(-dna[4], vel.y);
		}

		if(pos.y < thresh)
		{
			desired = new Vector2F(vel.x, dna[4]);
		}
		else if(pos.y > game.game.height - thresh)
		{
			desired = new Vector2F(vel.x, -dna[4]);
		}
		
		if(desired != null)
		{
			desired.normalizeLocal().multLocal(dna[4]);
			acc.addLocal(desired.subtractLocal(vel));
		}
	}
	
	public boolean isDead()
	{
		return health <= 0F;
	}
	
	public Vehicle clone()
	{
		Vehicle v = new Vehicle(game, dna);
		v.pos.set(pos);
		return v;
	}
}
