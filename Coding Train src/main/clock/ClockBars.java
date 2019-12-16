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

public class ClockBars extends Clock
{
	protected ClockBars()
	{
		super("Bar Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		Rectangle r = new Rectangle(width / 4F, height);
		r.grow(-5, -5);

		g2d.enable(G2D.G_FILL);

		g2d.setColor(Color.RED);
		g2d.drawRectangle(r.x, r.y, r.width, r.height);
		g2d.setColor(Color.RED.darker());
		g2d.drawRectangle(r.x, r.y, r.width, r.height * (1 - t.hn));
		r.x += width / 4F;

		g2d.setColor(Color.GREEN);
		g2d.drawRectangle(r.x, r.y, r.width, r.height);
		g2d.setColor(Color.GREEN.darker());
		g2d.drawRectangle(r.x, r.y, r.width, r.height * (1 - t.mn));
		r.x += width / 4F;

		g2d.setColor(Color.BLUE);
		g2d.drawRectangle(r.x, r.y, r.width, r.height);
		g2d.setColor(Color.BLUE.darker());
		g2d.drawRectangle(r.x, r.y, r.width, r.height * (1 - t.sn));
		r.x += width / 4F;

		g2d.setColor(Color.YELLOW);
		g2d.drawRectangle(r.x, r.y, r.width, r.height);
		g2d.setColor(Color.YELLOW.darker());
		g2d.drawRectangle(r.x, r.y, r.width, r.height * (1 - t.mln));

		g2d.disable(G2D.G_FILL);
	}
}
