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

import static main.touchit.TouchIt.SCL;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.math.vector.Vector2F;

class Cube
{
	public final TouchIt game;
	public final int x, y;
	public Cube next, prev;
	public int team;
	public double life;
	private boolean shatter;
	
	public Cube(TouchIt game, int x, int y)
	{
		this.game = game;
		this.x = x;
		this.y = y;
		life = 0;
	}
	
	public boolean updateCube(double dt)
	{	
		if(shatter)
		{
			shatter = false;
			for(int i = 0; i < (int) (10 * life); i++)
			{
				game.shatters.add(new Shatter(new Vector2F(x, y).addLocal(0.5F).mult(SCL), team));
			}
			team = 0;
			if(prev != null)
				prev.shatter();
			if(next != null)
				next.shatter();
			next = prev = null;
		}
		
		if(team > 0)
		{
			life -= dt / 3;
		}
		else
		{
			next = prev = null;
			life = 0;
		}
		return team != 0 && life <= 0;
	}
	
	public void paintCube(G2D g2d)
	{
		g2d.setColor(colors[team]);
		float x1 = x * SCL;
		float y1 = y * SCL;
		float sh = (float) (1 - life) * (SCL / 2);
		g2d.drawRectangle(x1 + sh, y1 + sh, SCL - sh * 2, SCL - sh * 2);
	}
	
	public void shatter()
	{
		shatter = true;
	}
	
	public static final Color[] colors = { Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.PINK };
}
