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

import java.awt.image.BufferedImage;

import com.hk.g2d.G2D;

public class Block extends Entity
{
	private final BufferedImage[] imgs;
	
	public Block(SpaceInvaders game, boolean flipped)
	{
		super(game, 11 * 3, 11 * 3);
		imgs = new BufferedImage[3];
		health = 3;
		for(int i = 0; i < imgs.length; i++)
		{
			imgs[i] = game.spritesheet.getSubimage(i * 11, 63 + (flipped ? 11 : 0), 11, 11);
		}
	}
	
	public void paint(G2D g2d)
	{
		if(health > 0 && health <= imgs.length)
		{
			g2d.pushMatrix();
			g2d.translate(pos.x, pos.y);
			g2d.scale(3, 3);
			g2d.g2d.drawImage(imgs[3 - health], 0, 0, null);
			g2d.popMatrix();
		}
	}
}
