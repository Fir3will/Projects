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
package main.clock;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.math.shape.Rectangle;

import main.clock.Clocks.Clock;

public class ClockBinary extends Clock
{
	protected ClockBinary()
	{
		super("Binary Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		Rectangle r = new Rectangle(width, height / 4F);
		r.grow(-5, -5);


		for(int i = 0; i < 4; i++)
		{
			g2d.setColor((t.h & (1 << (3 - i))) != 0 ? Color.RED : Color.RED.darker());
			g2d.enable(G2D.G_FILL).drawRectangle(r.x + i * (r.width / 4F), r.y, r.width / 4F, r.height).disable(G2D.G_FILL);
			g2d.setColor(Color.BLACK);
			g2d.drawRectangle(r.x + i * (r.width / 4F), r.y, r.width / 4F, r.height);
		}
		r.y += height / 4F;

		for(int i = 0; i < 6; i++)
		{
			g2d.setColor((t.m & (1 << (5 - i))) != 0 ? Color.GREEN : Color.GREEN.darker());
			g2d.enable(G2D.G_FILL).drawRectangle(r.x + i * (r.width / 6F), r.y, r.width / 6F, r.height).disable(G2D.G_FILL);
			g2d.setColor(Color.BLACK);
			g2d.drawRectangle(r.x + i * (r.width / 6F), r.y, r.width / 6F, r.height);
		}
		r.y += height / 4F;

		for(int i = 0; i < 6; i++)
		{
			g2d.setColor((t.s & (1 << (5 - i))) != 0 ? Color.BLUE : Color.BLUE.darker());
			g2d.enable(G2D.G_FILL).drawRectangle(r.x + i * (r.width / 6F), r.y, r.width / 6F, r.height).disable(G2D.G_FILL);
			g2d.setColor(Color.BLACK);
			g2d.drawRectangle(r.x + i * (r.width / 6F), r.y, r.width / 6F, r.height);
		}
		r.y += height / 4F;

		for(int i = 0; i < 10; i++)
		{
			g2d.setColor((t.ml & (1 << (9 - i))) != 0 ? Color.YELLOW : Color.YELLOW.darker());
			g2d.enable(G2D.G_FILL).drawRectangle(r.x + i * (r.width / 10F), r.y, r.width / 10F, r.height).disable(G2D.G_FILL);
			g2d.setColor(Color.BLACK);
			g2d.drawRectangle(r.x + i * (r.width / 10F), r.y, r.width / 10F, r.height);
		}

		g2d.disable(G2D.G_FILL);
	}
}
