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
import java.awt.geom.Rectangle2D;

import com.hk.g2d.G2D;

import main.clock.Clocks.Clock;

public class ClockColored extends Clock
{
	public ClockColored()
	{
		super("Simple Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		g2d.setFontSize(g2d.getFont().getSize2D() * 4F);
		g2d.setColor(Color.WHITE);
		g2d.drawRectangle(0, 0, width, height);
		
		String s = t.hs + ":" + t.ms + ":" + t.ss + "." + t.mls;
		Rectangle2D bounds = g2d.getStringBounds(s);
		float x = (float) (width / 2D - bounds.getWidth() / 2D);
		float y = (float) (height / 2D + bounds.getHeight() / 2D);

		g2d.setColor(Color.RED);
		g2d.drawString(t.hs, x, y - bounds.getHeight() / 2);
		g2d.drawString("HH", x, y + bounds.getHeight() / 2);
		x += g2d.getStringBounds(t.hs).getWidth();

		g2d.setColor(Color.WHITE);
		g2d.drawString(":", x, y - bounds.getHeight() / 2);
		g2d.drawString(":", x, y + bounds.getHeight() / 2);
		x += g2d.getStringBounds(":").getWidth();
		
		g2d.setColor(Color.GREEN);
		g2d.drawString(t.ms, x, y - bounds.getHeight() / 2);
		g2d.drawString("MM", x, y + bounds.getHeight() / 2);
		x += g2d.getStringBounds(t.ms).getWidth();

		g2d.setColor(Color.WHITE);
		g2d.drawString(":", x, y - bounds.getHeight() / 2);
		g2d.drawString(":", x, y + bounds.getHeight() / 2);
		x += g2d.getStringBounds(":").getWidth();
		
		g2d.setColor(Color.BLUE);
		g2d.drawString(t.ss, x, y - bounds.getHeight() / 2);
		g2d.drawString("SS", x, y + bounds.getHeight() / 2);
		x += g2d.getStringBounds(t.ss).getWidth();

		g2d.setColor(Color.WHITE);
		g2d.drawString(".", x, y - bounds.getHeight() / 2);
		g2d.drawString(".", x, y + bounds.getHeight() / 2);
		x += g2d.getStringBounds(".").getWidth();
		
		g2d.setColor(Color.YELLOW);
		g2d.drawString(t.mls, x, y - bounds.getHeight() / 2);
		g2d.drawString("MLS", x, y + bounds.getHeight() / 2);
		x += g2d.getStringBounds(t.mls).getWidth();
	}
}
