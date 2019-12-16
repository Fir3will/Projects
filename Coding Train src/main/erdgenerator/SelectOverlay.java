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
import java.awt.geom.Point2D;

import com.hk.g2d.G2D;
import com.hk.g2d.GuiOverlay;
import com.hk.math.MathUtil;
import com.sun.glass.events.KeyEvent;

public class SelectOverlay extends GuiOverlay
{
	public final ERDGenerator game;
	private float tx, ty, gx, gy, hx, hy;
	private final int gw, gh;
	private boolean held, pressed;

	public SelectOverlay(ERDGenerator game, int tx, int ty)
	{
		this.game = game;
		this.tx = tx;
		this.ty = ty;
		gw = 400;
		gh = 50;
	}

	@Override
	public void updateOverlay(double delta)
	{	
		if(tx < game.game.width / 2)
			gx = tx - 6;
		else
			gx = tx - gw + 6;
		
		if(ty < game.game.height / 2)
			gy = ty - 6;
		else
			gy = ty - gh + 6;
	}

	@Override
	public void paintOverlay(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawRoundRectangle(gx, gy, gw, gh, 12, 12);
		g2d.disable(G2D.G_FILL);
		
		g2d.setColor(Color.GRAY);
		g2d.drawCircle(tx - 6, ty - 6, 6);
		
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRectangle(gx, gy, gw, gh, 8, 8);

		String[] labels = { "Entity [1]", "Attribute [2]", "Relationship [3]" };
		for(int i = 0; i < labels.length; i++)
		{
			g2d.enable(G2D.G_CENTER);
			g2d.setColor(Color.BLACK);
			g2d.drawString(labels[i], gx + gw * (i + 1D) / (labels.length + 1D), gy + gh * 0.666);
			
			g2d.enable(G2D.G_FILL);
			g2d.setColor(game.selected == i ? Color.GREEN : Color.RED);
			g2d.drawCircle(gx + gw * (i + 1D) / (labels.length + 1D), gy + gh * 0.333, 5);
			g2d.disable(G2D.G_CENTER | G2D.G_FILL);
		}
	}

	public boolean mouse(float x, float y, boolean pressed)
	{
		if(pressed && !this.pressed)
		{
			if(Point2D.distance(x, y, tx, ty) <= 6)
			{
				held = true;
				hx = x;
				hy = y;
			}
		}
		this.pressed = pressed;
		held = held && pressed;
		return held;
	}
	
	public boolean mouseMoved(float x, float y)
	{
		if(held)
		{
			tx = MathUtil.between(6, tx + x - hx, game.game.width - 6);
			ty = MathUtil.between(6, ty + y - hy, game.game.height - 6);
			hx = x;
			hy = y;
		}
		return held;
	}

	public boolean key(int key, char keyChar, boolean pressed)
	{
		if(!pressed)
		{
			if(key == KeyEvent.VK_1)
			{
				game.selected = game.selected == 0 ? -1 : 0;
			}
			else if(key == KeyEvent.VK_2)
			{
				game.selected = game.selected == 1 ? -1 : 1;
			}
			else if(key == KeyEvent.VK_3)
			{
				game.selected = game.selected == 2 ? -1 : 2;
			}
			else
				return false;
			return true;
		}
		return false;
	}
}
