package main.flowfield;

import com.hk.math.vector.Vector2F;

public class Particle
{
	public final Vector2F pos, vel;
	
	public Particle()
	{
		pos = new Vector2F();
		vel = Vector2F.randUnitVector().multLocal(0.1F);
	}
	
	public void update()
	{
		pos.addLocal(vel);
	}
	
	public void applyForce(Vector2F force)
	{
		vel.addLocal(force);
		vel.normalizeLocal().multLocal(0.1F);
	}
}
