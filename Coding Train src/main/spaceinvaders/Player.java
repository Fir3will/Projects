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

public class Player extends Entity
{
	private final BufferedImage img;
	private final BufferedImage[] broken;
	private Entity bullet;
	private long deadTime = -1;
	
	public Player(SpaceInvaders game)
	{
		super(game, 11 * 3, 7 * 3);
		img = game.spritesheet.getSubimage(0, 47, 11, 7);
		broken = new BufferedImage[2];
		broken[0] = game.spritesheet.getSubimage(11, 47, 11, 7);
		broken[1] = game.spritesheet.getSubimage(22, 47, 11, 7);
	}
	
	public void reset()
	{
		health = 1;
		bullet = null;
		deadTime = -1;
	}
	
	public void update(float step)
	{
		super.update(step);
		
		if(bullet != null && bullet.isDead())
		{
			bullet = null;
		}
	}
	
	public void paint(G2D g2d)
	{
		g2d.pushMatrix();
		g2d.translate(pos.x, pos.y);
		g2d.scale(3, 3);
		if(health == 0)
		{
			boolean flip = (System.currentTimeMillis() % 200) >= 100;
			
			g2d.g2d.drawImage(flip ? broken[1] : broken[0], 0, 0, null);
		}
		else
			g2d.g2d.drawImage(img, 0, 0, null);
		g2d.popMatrix();
	}
	
	public boolean onDeath()
	{
		if(deadTime == -1)
		{
			deadTime = System.currentTimeMillis() + 1500;
		}
		return System.currentTimeMillis() > deadTime;
	}
	
	public void fire()
	{
		if(bullet == null)
		{
			bullet = new Bullet(game, 0);
			bullet.pos.set(pos.x + width / 2F - bullet.width / 2F, pos.y - bullet.height / 2F);
			bullet.vel.y = -500;
			game.addEntity(bullet);
		}
	}
}
