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
package main.towerdefense;

import java.awt.Color;
import java.awt.Point;

import com.hk.g2d.G2D;
import com.hk.math.vector.Vector2F;

public class Enemy
{
	public final Vector2F position;
	public final TowerDefense game;
	private boolean isDead = false;
	public int currentSpot;
	public final int maxHealth;
	public int health;
	
	public float speed = 0.2F;
	
	public Enemy(TowerDefense game, float x, float y, int maxHealth)
	{
		this.game = game;
		position = new Vector2F(x * 40 + 20, y * 40 + 20);
		currentSpot = 0;
		this.health = this.maxHealth = maxHealth;
	}
	
	public void update()
	{
		Point nextSpot = game.path.get(currentSpot);
		Vector2F dir = new Vector2F(nextSpot.x * 40 + 20, nextSpot.y * 40 + 20).subtractLocal(position);
		float dst = dir.length();
		dir.normalizeLocal().multLocal(speed);
		if(dst < speed)
		{
			position.set(nextSpot.x * 40 + 20, nextSpot.y * 40 + 20);
			currentSpot++;
			
			if(currentSpot == game.path.size())
			{
				game.health = Math.max(game.health - 1, 0);
				isDead = true;
			}
		}
		else
		{
			position.addLocal(dir);
		}
	}
	
	public void paint(G2D g2d, boolean hoverOver)
	{
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawCircle(position.x, position.y, 10);
		g2d.setColor(Color.BLACK);
		float f = (health / (float) maxHealth) * 9.6F;
		g2d.drawCircle(position.x, position.y, f);
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);
		
		if(hoverOver)
		{
			float dlx = Math.max(0, Math.min(g2d.width - 40, position.x));
			float dly = Math.max(0, Math.min(g2d.height - 24, position.y + 24));
			
			g2d.enable(G2D.G_CENTER | G2D.G_FILL);
			
			g2d.setColor(Color.BLACK);
			g2d.drawRectangle(dlx, dly, 40, 24);
			float w = 30 * (health / (float) maxHealth);
			
			g2d.disable(G2D.G_CENTER);
			g2d.setColor(Color.RED);
			g2d.drawRectangle(dlx - 15, dly - 5, w, 10);
			
			g2d.disable(G2D.G_FILL);
			g2d.enable(G2D.G_CENTER);
			g2d.setColor(Color.WHITE);
			g2d.drawRectangle(dlx, dly, 40, 24);
			g2d.drawRectangle(dlx, dly, 30, 10);

			g2d.disable(G2D.G_CENTER);
		}
	}
	
	public boolean isDead()
	{
		return isDead || health <= 0;
	}
}
