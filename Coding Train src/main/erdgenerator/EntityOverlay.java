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

public class EntityOverlay extends GuiOverlay
{
	public final ERDGenerator game;
	public final Entity entity;
	
	public EntityOverlay(ERDGenerator game)
	{
		this.game = game;
		this.entity = game.selectedEntity;
	}

	@Override
	public void updateOverlay(double delta)
	{

	}

	@Override
	public void paintOverlay(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		g2d.setColor(0, 0, 0, 0.3F);
		g2d.drawRect(0, 0, g2d.width, g2d.height);
		g2d.disable(G2D.G_FILL);
		
		g2d.setColor(Color.BLACK);
		entity.paintEntity(g2d);
	}

	public boolean mouse(float x, float y, boolean pressed)
	{
		if(!pressed)
		{
			game.selectedEntity = null;
			removeSelf();
		}
		return true;
	}
	
	public boolean mouseMoved(float x, float y)
	{
		return true;
	}

	public boolean mouseWheel(int amt)
	{
		return true;
	}

	public boolean key(int key, char keyChar, boolean pressed)
	{
		return true;
	}
}
