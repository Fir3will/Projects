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
package main.towerdefense;

import java.util.List;

import com.hk.math.FloatMath;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class TowerData
{
	public final Spot spot;
	public Enemy target;
	public boolean hasTower = false;
	public int damage = 1;
	public float reach = 4 * 20, rotation;
	public int waitTime;
	
	public TowerData(Spot spot)
	{
		this.spot = spot;
		waitTime = Rand.nextInt(100);
	}
	
	public void update(double delta)
	{
		if(waitTime == 0)
		{
			waitTime = damage == 1 ? 100 : 20;
		}
		waitTime--;
		if(hasTower)
		{
			target = null;
			List<Enemy> enemies = spot.game.enemies;
			Vector2F myPos = new Vector2F(spot.xCoord * 40 + 20, spot.yCoord * 40 + 20);
			int closest = -1;
			int cs = 0;
			for(int i = 0; i < enemies.size(); i++)
			{
				Enemy enemy = enemies.get(i);
				Vector2F e = enemy.position;
				float d = myPos.distance(e);
				if(d <= reach && enemy.currentSpot > cs)
				{
					cs = enemy.currentSpot;
					closest = i;
				}
			}
			
			if(closest != -1)
			{
				target = enemies.get(closest);
				Vector2F e = target.position;
				rotation = -e.subtract(myPos).getAngle() + FloatMath.PI / 2F;
			}
		}
	}
}
