package main.gravity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.hk.math.vector.Vector2F;

import main.G2D;

public class Planet
{
	public final String name;
	public final Vector2F pos, vel, acc;
	public final float mass;
	public final List<Vector2F> trails;
	private int lastTick;
	
	public Planet(String name, float x, float y, float mass)
	{
		this.name = name;
		pos = new Vector2F(x, y);
		vel = new Vector2F();
		acc = new Vector2F();
		
		this.mass = mass;
		trails = new ArrayList<>();
	}
	
	public void updatePlanet(int ticks)
	{
		vel.addLocal(acc);
		pos.addLocal(vel);
		acc.zero();

		int amt = (int) Math.max(1F, 60F - vel.length() * 100F);
		if(ticks - lastTick > amt)
		{
			lastTick = ticks;
			trails.add(pos.clone());
			if(trails.size() > 500)
			{
				trails.remove(0);
			}
		}
	}
	
	public Planet setVelocity(float x, float y)
	{
		vel.set(x, y);
		return this;
	}
	
	public Vector2F acceleration(Vector2F v)
	{
		float d2 = pos.distanceSquared(v);
		float f = Gravity.G * (this.mass / d2);
		return pos.subtract(v).normalizeLocal().multLocal(f);
	}
	
	public void paintPlanet(G2D g2d, boolean debugInfo)
	{
		g2d.setColor(Color.WHITE);
		g2d.drawCircle(pos.x - mass, pos.y - mass, mass);
		
		int s = !debugInfo ? Math.max(trails.size() - 100, 0) : 0;
		for(int i = s; i < trails.size() - 1; i++)
		{
			Vector2F a = trails.get(i);
			Vector2F b = trails.get(i + 1);
			
			float f = (i - s) / ((trails.size() - s) - 1F);
			g2d.setColor(f, f, 0F);
			g2d.drawLine(a.x, a.y, b.x, b.y);
		}
	}
	
	public String toString()
	{
		return name + "(" + mass + ")";
	}
}
