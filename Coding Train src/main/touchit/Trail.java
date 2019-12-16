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

import com.hk.math.Rand;

class Trail
{
	private final TouchIt game;
	public final int team;
	public int dir;
	public boolean dead;
	private int cx, cy, lx, ly;
	private double put, turn;
	
	public Trail(TouchIt game, int x, int y, int team)
	{
		this.game = game;
		this.team = team;
		cx = lx = x;
		cy = ly = y;
		dir = Rand.nextInt(4);
		dead = team == 0;
	}

	public void updateTrail(double dt)
	{	
		put += dt;
		while(put > 0.1)
		{
			put -= 0.1;
			advance(dir);
		}
		
		if(team > 1)
		{
			turn -= dt;
			if(turn <= 0)
			{
				turn += Rand.nextDouble(1);
				dir += Rand.nextBoolean() ? 1 : 3;
				dir %= 4;
			}
		}
		
		if(dead)
		{
			game.grid[cx][cy].shatter();
		}
	}
	
	public boolean advance(int dir)
	{
		int vx = dir == 1 ? 1 : dir == 3 ? -1 : 0;
		int vy = dir == 0 ? -1 : dir == 2 ? 1 : 0;
		cx += vx;
		cy += vy;
		boolean inBounds = game.inBounds(cx, cy);
		if((cx != lx || cy != ly) && inBounds)
		{
			if(game.grid[cx][cy].team == team || game.grid[cx][cy].team != 0)
			{
				dead = true;
				cx -= vx;
				cy -= vy;
			}
			else
			{
				game.grid[cx][cy].team = team;
				game.grid[cx][cy].life = 1;
				game.grid[cx][cy].prev = game.grid[lx][ly];
				game.grid[lx][ly].next = game.grid[cx][cy];
				lx = cx;
				ly = cy;
			}
			return true;
		}
		if(!inBounds) dead = true;
		cx -= vx;
		cy -= vy;
		return false;
	}
}
