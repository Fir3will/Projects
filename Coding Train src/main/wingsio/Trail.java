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
package main.wingsio;

import com.hk.g2d.G2D;
import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

class Trail extends Bullet
{	
	Trail(Vector2F pos, Vector2F vel, Player shooter)
	{
		super(shooter.game, pos, vel, shooter, null);
		max = life = Rand.nextDouble(1, 3);
	}
	
	public boolean updateBullet(double dt)
	{
		return (life -= dt) < 0;
	}
	
	public void paintBullet(G2D g2d)
	{
		float f = (float) (life / max);
		g2d.setColor(1F, MathUtil.between(0, f, 1), 0F);

		float lw = g2d.getLineWidth();
		g2d.setLineWidth(2F);
		g2d.drawPoint(pos.x, pos.y);
		g2d.setLineWidth(lw);
	}
	
	public float getDamage(Player against)
	{
		return (float) (life / max) / 10F;
	}
}