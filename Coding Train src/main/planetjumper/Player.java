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

import java.awt.Color;

import com.hk.math.vector.Vector2F;

import main.G2D;

public class Player
{
	public final Jumper game;
	public final Vector2F pos, vel, acc;
	public float rot, rotVel;
	
	public Player(Jumper game)
	{
		this.game = game;
		pos = new Vector2F();
		vel = new Vector2F();
		acc = new Vector2F();
		
		rot = 0;
		rotVel = 0;
	}
	
	public void updatePlayer(int ticks)
	{
		vel.addLocal(acc);
		pos.addLocal(vel);
		acc.zero();
		
		rot += rotVel;
		while(rot > 360)
		{
			rot -= 360;
		}
		while(rot < 0)
		{
			rot += 360;
		}
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
