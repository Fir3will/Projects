package main.evolutionarysb;

import java.awt.Color;
import java.util.List;

import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Main;

public class Vehicle implements Cloneable
{
	public final float mutationRate = 0.01F;
	public final float maxSpeed = 5F;
	public final float maxForce = 0.5F;
	
	public final Vector2F pos, vel, acc;
	public final float[] dna = new float[4];
	public float health = 1F;
	
	public Vehicle(float[] dna)
	{
		pos = new Vector2F();
		vel = Vector2F.randUnitVector().multLocal(maxSpeed);
		acc = new Vector2F();
		if(dna == null)
		{
			this.dna[0] = Rand.nextFloat(4) - 2;
			this.dna[1] = Rand.nextFloat(4) - 2;
			this.dna[2] = Rand.nextFloat(100);
			this.dna[3] = Rand.nextFloat(100);
		}
		else
		{
			this.dna[0] = dna[0] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(0.2F) - 0.1F : 0F);
			this.dna[1] = dna[1] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(0.2F) - 0.1F : 0F);
			this.dna[2] = dna[2] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(20) - 10 : 0F);
			this.dna[3] = dna[3] + (Rand.nextFloat() < mutationRate ? Rand.nextFloat(20) - 10 : 0F);
		}
	}
	
	public void update()
	{
		health -= 0.005F;
		
		if(acc.length() > maxForce)
		{
			acc.normalizeLocal().multLocal(maxForce);
		}
		vel.addLocal(acc);
		if(vel.length() > maxSpeed)
		{
			vel.normalizeLocal().multLocal(maxSpeed);
		}
		pos.addLocal(vel);
		acc.zero();
	}
	
	public void paint(G2D g2d, boolean debug)
	{
		g2d.pushMatrix();
		g2d.enable(G2D.G_FILL);

		g2d.setColor(1F - health, health, 0F);
		g2d.rotateR(-vel.getAngle() + Math.PI / 2F, pos.x, pos.y);
		g2d.drawRectangle(pos.x - 10, pos.y - 5, 20, 10);

		g2d.disable(G2D.G_FILL);

		if(debug)
		{
			g2d.setColor(Color.GREEN);
			g2d.drawLine(pos.x, pos.y, pos.x + dna[0] * 20, pos.y);
			g2d.setColor(Color.RED);
			g2d.drawLine(pos.x, pos.y, pos.x + dna[1] * 20, pos.y);
		}
	
		g2d.popMatrix();
		
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
			
			if(dst < maxSpeed)
			{
				lst.remove(i);
				i--;
				health = MathUtil.clamp(health + nutrition, 1F, 0F);
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
		if(desired.length() > maxSpeed)
		{
			desired.normalizeLocal().multLocal(maxSpeed);
		}
		desired.subtractLocal(vel);
		if(desired.length() > maxForce)
		{
			desired.normalizeLocal().multLocal(maxForce);
		}
		return desired;
	}
	
	public void checkBoundaries()
	{
		float thresh = 25;
		Vector2F desired = null;
		
		if(pos.x < thresh)
		{
			desired = new Vector2F(maxSpeed, vel.y);
		}
		else if(pos.x > Main.WIDTH - thresh)
		{
			desired = new Vector2F(-maxSpeed, vel.y);
		}

		if(pos.y < thresh)
		{
			desired = new Vector2F(vel.x, maxSpeed);
		}
		else if(pos.y > Main.HEIGHT - thresh)
		{
			desired = new Vector2F(vel.x, -maxSpeed);
		}
		
		if(desired != null)
		{
			desired.normalizeLocal().multLocal(maxSpeed);
			acc.addLocal(desired.subtractLocal(vel));
		}
	}
	
	public boolean isDead()
	{
		return health <= 0F;
	}
	
	public Vehicle clone()
	{
		Vehicle v = new Vehicle(dna);
		v.pos.set(pos);
		return v;
	}
}
