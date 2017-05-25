package main.planetjumper;

import com.hk.math.vector.Vector2F;

import main.G2D;

public class Planetoid
{
	public final String name;
	public final Vector2F pos;
	public final float mass;
	
	public Planetoid(String name, float x, float y, float mass)
	{
		this.name = name;
		pos = new Vector2F(x, y);
		
		this.mass = mass;
	}
	
	public Vector2F acceleration(Vector2F v)
	{
		float d2 = pos.distanceSquared(v);
		float f = Jumper.G * ((this.mass * 30) / d2);
		return pos.subtract(v).normalizeLocal().multLocal(f);
	}
	
	public void paintPlanet(G2D g2d)
	{
		g2d.drawCircle(pos.x - mass, pos.y - mass, mass);
	}
	
	public String toString()
	{
		return name + "(" + mass + ")";
	}
}
