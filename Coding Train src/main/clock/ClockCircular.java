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

public class ClockCircular extends Clock
{
	protected ClockCircular()
	{
		super("Circular Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		Rectangle r = new Rectangle(width, height);

		g2d.enable(G2D.G_FILL);

		g2d.setColor(Color.RED);
		r.grow(-40, -40);
		drawArc(g2d, r, t.hn);

		g2d.setColor(Color.GREEN);
		r.grow(-40, -40);
		drawArc(g2d, r, t.mn);

		g2d.setColor(Color.BLUE);
		r.grow(-40, -40);
		drawArc(g2d, r, t.sn);

		g2d.setColor(Color.YELLOW);
		r.grow(-40, -40);
		drawArc(g2d, r, t.mln);

		g2d.disable(G2D.G_FILL);
	}
	
	private void drawArc(G2D g2d, Rectangle r, float a)
	{
		int deg = (int) (a * 360);
		for(float i = 0; i < deg; i += 1.5F)
		{
			float angle = FloatMath.toRadians(i);
			float nx = r.x + (r.width - 14) / 2F + FloatMath.cos(angle) * ((r.width - 14) / 2F);
			float ny = r.y + (r.height - 14) / 2F + FloatMath.sin(angle) * ((r.height - 14) / 2F);
			
			g2d.drawCircle(nx, ny, 7);
		}
	}
}
