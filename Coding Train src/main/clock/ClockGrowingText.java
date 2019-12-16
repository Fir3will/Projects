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

public class ClockGrowingText extends Clock
{
	public ClockGrowingText()
	{
		super("Growing Text Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		Rectangle r = new Rectangle(width / 2, height);

		r.grow(0, (r.width - r.height) / 2F);
		
		g2d.enable(G2D.G_CENTER);
		
		float f = t.mln * 4 - 2;
		f = t.s % 2 == 0 ? f : -f;
		
		g2d.setFontSize(12 + (f * 16 + 16));
		g2d.setColor(Color.RED);
		g2d.drawString(t.hs, r.x + r.width / 2, r.y + r.height / 2);
		
		r.x += r.width;
		
		g2d.setFontSize(12 + (-f * 16 + 16));
		g2d.setColor(Color.GREEN);
		g2d.drawString(t.ms, r.x + r.width / 2, r.y + r.height / 2);
		
		g2d.disable(G2D.G_CENTER);
	}
}
