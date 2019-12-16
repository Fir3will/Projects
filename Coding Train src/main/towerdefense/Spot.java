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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Objects;

import com.hk.g2d.G2D;
import com.hk.math.FloatMath;

public class Spot
{
	public final TowerDefense game;
	public final SpotType type;
	public final int xCoord, yCoord;
	public final TowerData data;

	public Spot(TowerDefense game, int xCoord, int yCoord, SpotType type)
	{
		this.game = game;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.type = Objects.requireNonNull(type);
		data = this.type.isPlatform() ? new TowerData(this) : null;
	}
	
	public void paint(G2D g2d, boolean hoverOver)
	{
		if(type.isNothing()) return;

		g2d.enable(G2D.G_FILL);
		int ox = xCoord * 40;
		int oy = yCoord * 40;
		int rx = ox;
		int ry = oy;
		int rw = 40;
		int rh = 40;
		switch(type)
		{
		case NOTHING:
			g2d.setColor(Color.BLACK);
			break;
		case PATH:
			rx--;
			ry--;
			rw += 2;
			rh += 2;
			g2d.setColor(new Color(0xAAFFFFFF, true));
			break;
		case PLATFORM:
			rx += 2;
			ry += 2;
			rw -= 4;
			rh -= 4;
			g2d.setColor(hoverOver ? Color.GREEN : Color.WHITE);
			break;
		}
		g2d.drawRectangle(rx, ry, rw, rh);
		
		g2d.disable(G2D.G_FILL);
		if(data != null && data.hasTower)
		{
			g2d.setColor(Color.BLACK);
			if(data.damage == 1)
			{
				g2d.drawCircle(ox + 10, oy + 10, 10);
			}
			else if(data.damage == 2)
			{
				g2d.drawRectangle(ox + 10, oy + 10, 20, 20);
			}
			float x1 = ox + 20;
			float y1 = oy + 20;
			float x2 = FloatMath.cos(data.rotation) * 10F + x1;
			float y2 = FloatMath.sin(data.rotation) * 10F + y1;
			g2d.setColor(data.damage == 1 ? Color.GREEN : Color.RED);
			g2d.drawLine(x1, y1, x2, y2);
			
			if(data.waitTime < 10 && data.target != null)
			{
				g2d.drawLine(x1, y1, data.target.position.x, data.target.position.y);
				if(data.waitTime == 0) data.target.health -= data.damage;
			}
		}
	}
	
	public void clickedOn(int button)
	{
		if(type.isPlatform())
		{
			if(button == MouseEvent.BUTTON1)
			{
				if(!data.hasTower && game.money >= 20)
				{
					game.money -= 20;
					data.hasTower = true;
				}
				else if(data.damage == 1 && data.hasTower && game.money >= 40)
				{
					game.money -= 40;
					data.damage = 2;
					data.reach *= 2;
				}
			}
			else if(button == MouseEvent.BUTTON3 && data.hasTower)
			{
				if(data.damage == 1)
				{
					game.money += 17;
					data.hasTower = false;
				}
				else if(data.damage == 2)
				{
					data.reach /= 2;
					game.money += 34;
					data.damage = 1;
				}
			}
		}
	}
	
	public static enum SpotType
	{
		PLATFORM,
		PATH,
		NOTHING;

		public boolean isPlatform()
		{
			return this == PLATFORM;
		}
		
		public boolean isPath()
		{
			return this == PATH;
		}
		
		public boolean isNothing()
		{
			return this == NOTHING;
		}
	}
}
