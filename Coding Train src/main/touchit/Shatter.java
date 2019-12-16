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
package main.touchit;

import com.hk.g2d.G2D;
import com.hk.math.FloatMath;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

class Shatter
{
	private final Vector2F pos, vel;
	private int clr;
	private double life;
	
	public Shatter(Vector2F pos, int clr)
	{
		this.pos = pos;
		this.vel = new Vector2F(Rand.nextFloat(1, 8), 0).rotateAround(Rand.nextFloat(FloatMath.PI * 2), true);
		this.clr = clr;
		life = Rand.nextDouble();
	}
	
	public boolean updateShatter(double dt)
	{
		pos.addLocal(vel.mult((float) dt));

		life += dt;
		return life > 4;
	}

	public void paintShatter(G2D g2d)
	{
		g2d.setColor(Cube.colors[clr], 1 - (float) (life / 4));
		g2d.drawPoint(pos.x, pos.y);
	}
}