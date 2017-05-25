/**************************************************************************
 *
 * [2017] Fir3will, All Rights Reserved.
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
