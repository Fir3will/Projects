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

import main.clock.Clocks.Clock;

import com.hk.g2d.G2D;
import com.hk.math.shape.Rectangle;

public class ClockTextWall extends Clock
{
	public ClockTextWall()
	{
		super("Text Wall Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		g2d.setFontSize(g2d.getFont().getSize2D() * 2F);
		Rectangle r = new Rectangle(width, height);
		r.grow(-5, -5);
		
		for(int x1 = 0; x1 < 6; x1++)
		{
			for(int y1 = 0; y1 < 10; y1++)
			{
				int i = x1 + y1 * 6;
				float x = r.x + x1 / 6F * r.width;
				float y = r.y + y1 / 10F * r.height;
				float w = r.width / 6F;
				float h = r.height / 10F;
				String s = (i < 10 ? "0" : "") + String.valueOf(i);

				int amt = 0;
				if(t.h == i) amt++;
				if(t.m == i) amt++;
				if(t.s == i) amt++;

				if(amt > 0)
				{
					g2d.enable(G2D.G_FILL);
					float tx = x;
					w = w / amt;

					if(t.h == i)
					{
						g2d.setColor(Color.RED);
						g2d.drawRectangle(tx, y, w, h);
						tx += w;
					}
					if(t.m == i)
					{
						g2d.setColor(Color.GREEN);
						g2d.drawRectangle(tx, y, w, h);
						tx += w;
					}
					if(t.s == i)
					{
						g2d.setColor(Color.BLUE);
						g2d.drawRectangle(tx, y, w, h);
					}
					g2d.disable(G2D.G_FILL);
				}
				g2d.setColor(Color.WHITE);
				g2d.enable(G2D.G_CENTER).drawString(s, x + r.width / 6F / 2, y + h / 2).disable(G2D.G_CENTER);
			}
		}
	}
}
