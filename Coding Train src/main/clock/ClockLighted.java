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

public class ClockLighted extends Clock
{
	public ClockLighted()
	{
		super("Lights Clock");
	}

	@Override
	public void drawClock(G2D g2d, Time t, float width, float height)
	{
		Rectangle r = new Rectangle(width, height);
		r.grow(-20, -20);

		Rectangle hb = r.clone();
		hb.width /= 2;
		hb.height = hb.height / 4 - 1;
		g2d.setColor(1, 0, 0, t.hn);
		g2d.enable(G2D.G_FILL).drawEllipse(hb.x, hb.y, hb.width, hb.height).disable(G2D.G_FILL);
		g2d.setColor(Color.RED);
		g2d.drawEllipse(hb.x, hb.y, hb.width, hb.height);

		Rectangle mb = r.clone();
		mb.width /= 2;
		mb.height = mb.height / 4 - 1;
		mb.x += mb.width;
		mb.y += mb.height;
		g2d.setColor(0, 1, 0, t.mn);
		g2d.enable(G2D.G_FILL).drawEllipse(mb.x, mb.y, mb.width, mb.height).disable(G2D.G_FILL);
		g2d.setColor(Color.GREEN);
		g2d.drawEllipse(mb.x, mb.y, mb.width, mb.height);
		
		Rectangle sb = r.clone();
		sb.width /= 2;
		sb.height = sb.height / 4 - 1;
		sb.y += sb.height * 2;
		g2d.setColor(0, 0, 1, t.sn);
		g2d.enable(G2D.G_FILL).drawEllipse(sb.x, sb.y, sb.width, sb.height).disable(G2D.G_FILL);
		g2d.setColor(Color.BLUE);
		g2d.drawEllipse(sb.x, sb.y, sb.width, sb.height);

		Rectangle mlb = r.clone();
		mlb.width /= 2;
		mlb.height = mlb.height / 4 - 1;
		mlb.x += mlb.width;
		mlb.y += mlb.height * 3;
		g2d.setColor(1, 1, 0, t.mln);
		g2d.enable(G2D.G_FILL).drawEllipse(mlb.x, mlb.y, mlb.width, mlb.height).disable(G2D.G_FILL);
		g2d.setColor(Color.YELLOW);
		g2d.drawEllipse(mlb.x, mlb.y, mlb.width, mlb.height);
	}
}
