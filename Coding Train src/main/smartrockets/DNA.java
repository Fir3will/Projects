package main.smartrockets;

import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class DNA
{
	public final Vector2F[] velocities;
	
	public DNA()
	{
		velocities = new Vector2F[SmartRockets.MAX];
		for(int i = 0; i < velocities.length; i++)
		{
			velocities[i] = Vector2F.randUnitVector().multLocal(0.2F);
		}
	}
	
	private DNA(Vector2F[] vels)
	{
		velocities = vels;
	}
	
	public static DNA child(DNA a, DNA b)
	{
		Vector2F[] vs = new Vector2F[SmartRockets.MAX];
		int mid = Rand.nextInt(vs.length);
		for(int i = 0; i < vs.length; i++)
		{
			if(Rand.nextFloat() < 0.01F)
			{
				vs[i] = Vector2F.randUnitVector().multLocal(0.2F);
			}
			else
			{
				vs[i] = i < mid ? a.velocities[i] : b.velocities[i];
			}
		}
		return new DNA(vs);
	}
}
