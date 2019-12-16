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
package main.spaceinvaders;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.hk.g2d.G2D;

public class Bullet extends Entity
{
	private final boolean enemy;
	private final BufferedImage img1, img2;
	
	public Bullet(SpaceInvaders game, int type)
	{
		super(game, 3 * 2, 7 * 2);
  		this.enemy = type > 0;
		BufferedImage i = null, i2 = null;
		if(this.enemy)
		{
			int x = type == 1 ? 14 : 20;
			i = game.spritesheet.getSubimage(x, 54, 3, 7);
			i2 = game.spritesheet.getSubimage(x + 3, 54, 3, 7);
		}
		img1 = i;
		img2 = i2;
	}
	
	public void update(float step)
	{
		super.update(step);
	
		if(pos.y < -height || pos.y > game.game.width)
		{
			isDead = true;
		}
	}
	
	public void paint(G2D g2d)
	{
		if(enemy)
		{
			g2d.pushMatrix();
			g2d.translate(pos.x, pos.y);
			g2d.scale(2, 2);
			g2d.g2d.drawImage(game.last % 200 > 100 ? img1 : img2, 0, 0, null);
			g2d.popMatrix();
		}
		else
		{
			g2d.enable(G2D.G_FILL);
			g2d.setColor(Color.WHITE);
			g2d.drawRoundRectangle(pos.x, pos.y, width, height, width / 2F, width / 2F);
			g2d.disable(G2D.G_FILL);
		}
	}
	
	public boolean onDeath()
	{
		if(enemy)
			game.enemyBullets--;
		
		return true;
	}
	
	public void onCollide(Entity other)
	{
		Entity e = null;
		if(other instanceof Block)
		{
			e = other;
		}
		else if(enemy)
		{
			if(other instanceof Player)
			{
				e = other;
			}
		}
		else if(other instanceof Enemy)
		{
			e = other;
		}
		
		if(e != null)
		{
			e.health--;
			isDead = true;
		}
	}
}
