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
package main.smartrockets;

import java.awt.Color;

import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Main;

public class Rocket
{
	public final Vector2F pos, vel;
	private final Vector2F acc;
	public final DNA dna;
	public boolean dead, complete;
	public float worth;
	
	public Rocket(DNA dna)
	{
		pos = new Vector2F(Main.WIDTH / 2F, Main.HEIGHT  - 10);
		vel = new Vector2F();
		acc = new Vector2F();
		
		this.dna = dna == null ? new DNA() : dna;
	}
	
	public void applyForce(Vector2F force)
	{
		acc.addLocal(force);
	}
	
	public void update()
	{
		if(!dead && !complete)
		{
			vel.addLocal(acc);
			
			if(vel.lengthSquared() > 16)
			{
				vel.normalizeLocal().mult(16F);
			}
			pos.addLocal(vel);
			acc.zero();
		}
	}
	
	public void draw(G2D g2d)
	{
		if(!dead && !complete)
		{
			g2d.pushMatrix();
			g2d.enable(G2D.G_CENTER | G2D.G_FILL);
			g2d.setColor(new Color(1F, 1F, 1F, 0.7F));
			g2d.rotateR(-vel.getAngle(), pos.x, pos.y);
			g2d.drawRectangle(pos.x, pos.y - 40, 20, 40);
			g2d.disable(G2D.G_CENTER | G2D.G_FILL);
			g2d.popMatrix();
		}
	}
}
