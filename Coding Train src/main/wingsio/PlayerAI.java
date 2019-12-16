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

import com.hk.math.FloatMath;
import com.hk.math.vector.Vector2F;

class PlayerAI
{
	public final Player player;
	public final WingsIO game;
	
	PlayerAI(Player player)
	{
		this.player = player;
		this.game = player.game;
	}
	
	public void updateAI(double dt)
	{
		float dst = player.pos.distance(game.player.pos);
		if(dst > 200)
		{
			Vector2F dir = game.player.pos.subtract(player.pos).normalizeLocal();
			player.dir = player.dir == null ? new Vector2F(dir) : player.dir;
			player.dir.interpolateLocal(dir, (float) dt);
			player.rot = -player.dir.getAngle() + FloatMath.PI / 2;
		}
		player.firing = dst > 200;
	}
}
