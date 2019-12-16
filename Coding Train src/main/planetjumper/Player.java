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
package main.planetjumper;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;

public class Player
{
	public final Jumper game;
	public final Vector2F pos, vel, acc;
	public float rot, rotVel;
	public int rotSide = 0;
	public boolean boost;
	
	public Player(Jumper game)
	{
		this.game = game;
		pos = new Vector2F();
		vel = new Vector2F();
		acc = new Vector2F();
		
		rot = 0;
		rotVel = 0;
	}
	
	public void updatePlayer(double delta)
	{
		if(boost)
		{
			vel.x += Math.cos(rot * Math.PI / 180) * delta;
			vel.y += Math.sin(rot * Math.PI / 180) * delta;
		}
		vel.x += acc.x * delta;
		vel.y += acc.y * delta;

		pos.x += vel.x * delta;
		pos.y += vel.y * delta;
		acc.zero();
		
		rotSide = MathUtil.sign(rotSide);
		if(rotSide != 0)
		{
			rotVel += 180 * delta * rotSide;
		}
		rot += rotVel * delta;
		rot %= 360;
	}
	
	public void paintPlayer(G2D g2d)
	{
		g2d.pushMatrix();
		g2d.setColor(Color.WHITE);
		g2d.rotate(rot, pos.x, pos.y);
		g2d.drawRectangle(pos.x - 5, pos.y - 5, 10, 10);
		g2d.setColor(Color.RED);
		g2d.drawLine(pos.x - 5, pos.y - 5, pos.x - 5, pos.y + 5);
		g2d.popMatrix();
	}
}
