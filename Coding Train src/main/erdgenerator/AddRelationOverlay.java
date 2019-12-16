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
package main.erdgenerator;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.GuiOverlay;

public class AddRelationOverlay extends GuiOverlay
{
	public final ERDGenerator game;
	public final Entity entity;
	private Entity hoverEntity;
	private float mx, my;
	
	public AddRelationOverlay(ERDGenerator game, Entity entity, float mx, float my)
	{
		this.game = game;
		this.entity = entity;
		this.mx = mx;
		this.my = my;
	}

	@Override
	public void updateOverlay(double delta)
	{

	}

	@Override
	public void paintOverlay(G2D g2d)
	{
//		g2d.drawRect(g2d.width / 3, g2d.height / 3, g2d.width / 3, g2d.height / 3);
		float x = mx;
		float y = my;
		g2d.setColor(Color.RED);
		if(hoverEntity != null)
		{
			g2d.setColor(Color.GREEN);
			x = hoverEntity.gx;
			y = hoverEntity.gy;
		}
		g2d.drawLine(entity.gx, entity.gy, x, y);
	}

	public boolean mouse(float x, float y, boolean pressed)
	{
		if(!pressed)
		{
			if(hoverEntity != null)
			{
				System.out.print("Add relationship between '");
				System.out.print(entity.name);
				System.out.print("' and '");
				System.out.print(hoverEntity.name);
				System.out.println("'.");
			}
			game.selected = -1;
			removeSelf();
		}
		return true;
	}
	
	public boolean mouseMoved(float x, float y)
	{
		mx = x;
		my = y;
		hoverEntity = null;
		for(Entity entity : game.entities)
		{
			if(entity.contains(mx, my))
			{
				hoverEntity = entity;
			}
		}
		return true;
	}
}
