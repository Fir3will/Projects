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
import com.hk.math.Rand;

public class Enemy extends Entity
{
	public final EnemyType type;
	public boolean flip;
	private final BufferedImage img1, img2, broken;
	private long deadTime = -1;
	private int scr;
	private int ticks = 0;
	
	public Enemy(SpaceInvaders game, EnemyType type)
	{
		super(game, 0, 0);
		this.type = type;
		this.width = type.w * 3;
		this.height = type.h * 3;
		img1 = game.spritesheet.getSubimage(type.getUX1(), type.uy, type.w, type.h);
		img2 = game.spritesheet.getSubimage(type.getUX2(), type.uy, type.w, type.h);
		broken = game.spritesheet.getSubimage(0, 54, 14, 9);
	}
	
	public void update(float step)
	{
		super.update(step);
		flip = !flip;
		
		if(type != EnemyType.BONUS)
		{
			int t = ticks % 21;
			if(t == 10)
			{
				pos.y += 25;
			}
			else 
			{
				if(t < 10)
				{
					pos.x += 7;
				}
				else
				{
					pos.x -= 7;
				}
			}
			
			ticks++;
		}
	}
	
	public void paint(G2D g2d)
	{
		g2d.pushMatrix();
		g2d.translate(pos.x, pos.y);
		if(deadTime != -1)
		{
			g2d.translate(width / 2F - broken.getWidth() * 3 / 2F, height / 2F - broken.getHeight() * 3 / 2F);
			g2d.scale(3, 3);
			g2d.g2d.drawImage(broken, 0, 0, null);
			g2d.popMatrix();

			float time = 1 - (deadTime - System.currentTimeMillis()) / 500F;
			
			g2d.setColor(Color.GREEN);
			g2d.enable(G2D.G_CENTER);
			g2d.drawShadowedString("+" + scr, pos.x + width / 2F, pos.y + height + 12 * time, Color.BLACK);
			g2d.disable(G2D.G_CENTER);
		}
		else
		{
			g2d.scale(3, 3);
			g2d.g2d.drawImage(flip && type != EnemyType.BONUS ? img2 : img1, 0, 0, null);
			g2d.popMatrix();
		}
	}
	
	public void fire(float mod)
	{
		game.enemyBullets++;
		Bullet bullet = new Bullet(game, type.ordinal() % 2 + 1);
		bullet.pos.set(pos.x + width / 2F - bullet.width / 2F, pos.y - bullet.height / 2F);
		bullet.vel.y = 200 * mod;
		game.addEntity(bullet);
	}
	
	public boolean onDeath()
	{
		if(deadTime == -1)
		{
			if(type != EnemyType.BONUS)
				game.amtOfEnemies--;
			game.score += scr = type.getPointReward();
			deadTime = System.currentTimeMillis() + 200;
		}
	
		return System.currentTimeMillis() > deadTime;
	}
	
	public static enum EnemyType
	{
		ENEMY1(0, 16, 14, 8, 30),
		ENEMY2(0, 0, 11, 8, 25),
		ENEMY3(0, 32, 9, 8, 20),
		ENEMY4(0, 8, 8, 8, 15),
		ENEMY5(0, 24, 7, 8, 10),
		BONUS(0, 40, 16, 7, -1);
		
		public final int ux, uy, w, h;
		private final int points;
		
		private EnemyType(int ux, int uy, int w, int h, int points)
		{
			this.ux = ux;
			this.uy = uy;
			this.w = w;
			this.h = h;
			this.points = points;
		}
		
		public int getPointReward()
		{
			if(this == BONUS)
			{
				return Rand.nextInt(50, 151);
			}
			return points;
		}
		
		public int getUX1()
		{
			return ux;
		}
		
		public int getUX2()
		{
			return ux + w;
		}
		
		public int getUX(boolean second)
		{
			return second ? ux : ux + w;
		}
	}
}
