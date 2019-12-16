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
package main.diep.io;

import java.awt.Color;

import com.hk.g2d.G2D;

public class Shot
{
	public final Vehicle vehicle;
	public final double radius;
	public double px, py, vx, vy;
	private double life;

	public Shot(Vehicle vehicle, double radius)
	{
		this.vehicle = vehicle;
		this.radius = radius;
		px = vehicle.px;
		py = vehicle.py;
		life = 2.5;
	}
	
	public boolean updateShot(double delta)
	{
		double speed = Math.sqrt(vx * vx + vy * vy);
		
		if(speed != 0 && speed > Vehicle.SHOT_SPEED)
		{
			vx = vx / speed * Vehicle.SHOT_SPEED;
			vy = vy / speed * Vehicle.SHOT_SPEED;
		}
		
		px += vx * delta;
		py += vy * delta;

		speed = 1 - (delta / 100);
		vx *= speed;
		vy *= speed;
		
		life -= delta;
		
		return life <= 0;
	}
	
	public void paintShot(G2D g2d)
	{
		g2d.setColor(vehicle.isPlayer() ? BLUE_TEAM : RED_TEAM);
		g2d.enable(G2D.G_FILL | G2D.G_CENTER);
		g2d.drawCircle(px, py, radius);
		g2d.disable(G2D.G_FILL);

		g2d.setColor(vehicle.isPlayer() ? DK_BLUE_TEAM : DK_RED_TEAM);
		g2d.drawCircle(px, py, radius);
		g2d.disable(G2D.G_CENTER);
	}

	public static final Color BLUE_TEAM = new Color(85, 205, 255);
	public static final Color DK_BLUE_TEAM = new Color(0, 120, 170);

	public static final Color RED_TEAM = new Color(255, 85, 85);
	public static final Color DK_RED_TEAM = new Color(170, 0, 0);
}
