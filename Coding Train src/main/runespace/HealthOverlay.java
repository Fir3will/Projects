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
package main.runespace;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import com.hk.g2d.G2D;
import com.hk.g2d.GuiOverlay;
import com.hk.str.StringUtil;

public class HealthOverlay extends GuiOverlay
{
	private final Runespace game;
	private Entity player;
	
	public HealthOverlay(Runespace game)
	{
		this.game = game;
	}

	@Override
	public void updateOverlay(double delta)
	{
		player = game.player;
	}

	@Override
	public void paintOverlay(G2D g2d)
	{
		if(player == null)
			return;
		
		Rectangle r = new Rectangle(30, g2d.height - 30, 270, 40);
		r.y -= r.height;
		float hlth = (float) player.health / player.maxHealth;
		float stma = (float) (player.stamina / 100);
		
		g2d.enable(G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawRoundRectangle(r.x, r.y, r.width, r.height, 40, 40);
		g2d.setColor(player.sprinting ? Color.GREEN : Color.WHITE);
		g2d.drawCircle(r.x + r.width - 18, r.y - 22, 20);
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRectangle(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 36, 36);
		g2d.drawCircle(r.x + r.width - 16, r.y - 20, 18);
		g2d.setColor(Color.RED);
		g2d.drawRoundRectangle(r.x + 4, r.y + 4, hlth * (r.width - 40) + 32, r.height - 8, 32, 32);
		g2d.setColor(0x00BB00);
		g2d.enable(G2D.G_CENTER);
		g2d.drawCircle(r.x + r.width + 2, r.y - 2, stma * 12.8 + 3.2);
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);

		g2d.enable(G2D.G_CENTER);
		g2d.setColor(Color.WHITE);
		g2d.pushFont();
		g2d.setFontSize(18);
		g2d.drawString(StringUtil.commaFormat(player.health), r.getCenterX(), r.getCenterY() - 2);
		g2d.setFontSize(14);
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));
		g2d.drawString((int) (player.stamina), r.x + r.width + 2, r.y - 4);
		g2d.popFont();
		g2d.disable(G2D.G_CENTER);
	}
}
