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
package main.towerdefense;

import java.awt.Color;
import java.util.Objects;

import main.G2D;

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
		int rx = xCoord * 20;
		int ry = yCoord * 20;
		int rw = 20;
		int rh = 20;
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
			g2d.setColor(Color.RED);
			break;
		case PLATFORM:
			rx += 2;
			ry += 2;
			rw -= 4;
			rh -= 4;
			g2d.setColor(hoverOver ? Color.GREEN : Color.WHITE);
			break;
		default:
			throw new AssertionError(type);
		}
		g2d.drawRectangle(rx, ry, rw, rh);
	}
	
	public void clickedOn()
	{
		if(type.isPlatform())
		{
			
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
