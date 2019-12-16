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
import com.hk.math.FloatMath;
import com.hk.math.shape.Rectangle;

public class ClockRegular extends Clock
{
	protected ClockRegular()
	{
		super("Analog Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		g2d.setFontSize(g2d.getFont().getSize2D() * 2F);
		Rectangle r = new Rectangle(width, height);
		r.grow(-5, -5);
		g2d.drawEllipse(r.x, r.y, r.width, r.height);

		r.grow(-15, -15);
		for(int h = 1; h <= 60; h++)
		{
			float a = FloatMath.toRadians(h * 6 - 90);
			float x1 = r.x + r.width / 2F + FloatMath.cos(a) * (r.width / 2F);
			float y1 = r.y + r.height / 2F + FloatMath.sin(a) * (r.height / 2F);
			r.grow(10, 10);
			float x2 = r.x + r.width / 2F + FloatMath.cos(a) * (r.width / 2F);
			float y2 = r.y + r.height / 2F + FloatMath.sin(a) * (r.height / 2F);
			r.grow(-10, -10);
			
			if(h % 5 != 0)
			{
				g2d.drawLine(x1, y1, x2, y2);
			}
			else
			{
				g2d.enable(G2D.G_CENTER);
				g2d.drawString(h / 5, x1, y1 - 7);
				g2d.disable(G2D.G_CENTER);
			}
		}
		
		r.grow(-10, -10);

		g2d.setColor(Color.RED);
		float hn = (t.h * 3600 + t.m * 60 + t.s) / 43200F;
		float hx = r.x + r.width / 2F + FloatMath.cos(FloatMath.toRadians(hn * 360 - 90)) * (r.width / 2F);
		float hy = r.y + r.height / 2F + FloatMath.sin(FloatMath.toRadians(hn * 360 - 90)) * (r.height / 2F);
		g2d.drawLine(r.x + r.width / 2F, r.y + r.height / 2F, hx, hy);

		g2d.setColor(Color.GREEN);
		float mn = (t.m * 60 + t.s) / 3600F;
		float mx = r.x + r.width / 2F + FloatMath.cos(FloatMath.toRadians(mn * 360 - 90)) * (r.width / 2F);
		float my = r.y + r.height / 2F + FloatMath.sin(FloatMath.toRadians(mn * 360 - 90)) * (r.height / 2F);
		g2d.drawLine(r.x + r.width / 2F, r.y + r.height / 2F, mx, my);

		g2d.setColor(Color.BLUE);
		float sn = t.sn;
		float sx = r.x + r.width / 2F + FloatMath.cos(FloatMath.toRadians(sn * 360 - 90)) * (r.width / 2F);
		float sy = r.y + r.height / 2F + FloatMath.sin(FloatMath.toRadians(sn * 360 - 90)) * (r.height / 2F);
		g2d.drawLine(r.x + r.width / 2F, r.y + r.height / 2F, sx, sy);
	}
}
