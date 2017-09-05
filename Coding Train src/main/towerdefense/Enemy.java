/**************************************************************************
 *
 * [2017] Fir3will, All Rights Reserved.
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

import com.hk.math.vector.Vector2F;

import main.G2D;

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
		position = new Vector2F(x * 20 + 10, y * 20 + 10);
		currentSpot = 0;
		this.health = this.maxHealth = maxHealth;
	}
	
	public void update()
	{
		Point nextSpot = game.path.get(currentSpot);
		Vector2F dir = new Vector2F(nextSpot.x * 20 + 10, nextSpot.y * 20 + 10).subtractLocal(position);
		float dst = dir.length();
		dir.normalizeLocal().multLocal(speed);
		if(dst < speed)
		{
			position.set(nextSpot.x * 20 + 10, nextSpot.y * 20 + 10);
			currentSpot++;
			
			if(currentSpot == game.path.size())
			{
				game.health--;
				isDead = true;
			}
		}
		else
		{
			position.addLocal(dir);
		}
	}
	
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawCircle(position.x, position.y, 5);
		g2d.setColor(Color.BLACK);
		float f = (health / (float) maxHealth) * 4F;
		g2d.drawCircle(position.x, position.y, f);
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);
	}
	
	public boolean isDead()
	{
		return isDead || health <= 0;
	}
}
