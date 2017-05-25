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
