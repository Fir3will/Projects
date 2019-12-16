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
package main.carrom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import main.carrom.Goti.Type;

import com.hk.g2d.G2D;
import com.hk.math.FloatMath;
import com.hk.math.vector.Vector2F;

public class Board
{
	public final List<Goti> gotis;
	public final Goti steggar;
	public final Vector2F[] holes;

	public Board()
	{
		gotis = new ArrayList<>();
		holes = new Vector2F[4];
		holes[0] = new Vector2F(15, 15);
		holes[1] = new Vector2F(Carrom.bw - 15, 15);
		holes[2] = new Vector2F(15, Carrom.bh - 15);
		holes[3] = new Vector2F(Carrom.bw - 15, Carrom.bh - 15);
		steggar = new Goti(Type.STEGGAR, Carrom.bw / 2, Carrom.bh - 90);
	}
	
	public Board(Board cpy)
	{
		gotis = new ArrayList<>();
		holes = cpy.holes;
		steggar = new Goti(cpy.steggar);

		for(int i = 0; i < cpy.gotis.size(); i++)
		{
			Goti g = cpy.gotis.get(i);
			if(g.type == Type.STEGGAR)
			{
				gotis.add(steggar);
			}
			else
			{
				gotis.add(new Goti(g));
			}
		}
	}
	
	public void setupBoard()
	{
		float cx = Carrom.bw / 2;
		float cy = Carrom.bh / 2;
		gotis.clear();
		gotis.add(new Goti(Type.QUEEN, cx, cy));
		for(int i = 0; i < 6; i++)
		{
			float ang = FloatMath.toRadians(i * 60 - 15);
			float x = cx + FloatMath.cos(ang) * 20;
			float y = cy + FloatMath.sin(ang) * 20;
			gotis.add(new Goti(Type.values()[i % 2], x, y));
		}
		for(int i = 0; i < 12; i++)
		{
			float ang = FloatMath.toRadians(i * 30 - 15);
			float x = cx + FloatMath.cos(ang) * 40 * (i % 2 == 0 ? 1 : 0.8F);
			float y = cy + FloatMath.sin(ang) * 40 * (i % 2 == 0 ? 1 : 0.8F);
			gotis.add(new Goti(Type.values()[i % 2], x, y));
		}
	}
	
	public int updateBoard(double dt)
	{
		int score = 0;
		for(int i = 0; i < gotis.size(); i++)
		{
			Goti g = gotis.get(i);
			g.updateGoti(dt);

			if(g.pos.x - g.radius < 0)
			{
				g.vel.x *= -0.8F;
				g.pos.x = g.radius;
			}
			if(g.pos.y - g.radius < 0)
			{
				g.vel.y *= -0.8F;
				g.pos.y = g.radius;
			}
			if(g.pos.x + g.radius > Carrom.bw)
			{
				g.vel.x *= -0.8F;
				g.pos.x = Carrom.bw - g.radius;
			}
			if(g.pos.y + g.radius > Carrom.bh)
			{
				g.vel.y *= -0.8F;
				g.pos.y = Carrom.bh - g.radius;
			}
			
			for(Vector2F hole : holes)
			{
				if(g.type.reward >= 0 && g.pos.distance(hole) < 10)
				{
					score += g.type.reward;
					gotis.remove(i);
					i--;
				}
			}
		}

		for(int i = 0; i < gotis.size(); i++)
		{
			for(int j = i + 1; j < gotis.size(); j++)
			{
				Goti a = gotis.get(i);
				Goti b = gotis.get(j);
				if(a.collided(b))
				{
					a.resolveCollision(b);
				}
			}
		}
		return score;
	}
	
	public void paintGotis(G2D g2d, float bx, float by)
	{
		g2d.translate(bx, by);
		for(Goti g : gotis)
		{
			g.paintGoti(g2d);
		}
		g2d.translate(-bx, -by);
	}
	
	public void paintBoard(G2D g2d, float bx, float by, Color cc)
	{
		g2d.pushMatrix();
		g2d.translate(bx, by);
		g2d.enable(G2D.G_FILL);
		for(int i = 0; i < 2; i++)
		{
			if(i == 0) g2d.setColor(0xA52A2A);
			g2d.drawRoundRectangle(-50, -50, Carrom.bw + 100, Carrom.bh + 100, 60, 60);
			if(i == 0) g2d.setColor(0xF5DEB3);
			g2d.drawRoundRectangle(0, 0, Carrom.bw, Carrom.bh, 40, 40);
			g2d.setColor(Color.BLACK);
			g2d.disable(G2D.G_FILL);
		}
		
		g2d.pushMatrix();
		for(int i = 0; i < 360; i += 90)
		{
			g2d.rotate(90, Carrom.bw / 2, Carrom.bh / 2);
			g2d.enable(G2D.G_CENTER);
			g2d.drawCircle(90, 70, 10);
			g2d.drawCircle(Carrom.bw - 90, 70, 10);
			g2d.enable(G2D.G_FILL);
			g2d.drawCircle(90, 70, 6);
			g2d.drawCircle(Carrom.bw - 90, 70, 6);

			g2d.drawCircle(15, 15, 15);
			g2d.disable(G2D.G_FILL);
			g2d.setColor(cc);
			g2d.drawCircle(15, 15, 15);
			g2d.drawCircle(90, 70, 6);
			g2d.drawCircle(Carrom.bw - 90, 70, 6);
			g2d.disable(G2D.G_CENTER);

			g2d.setColor(Color.BLACK);
			g2d.drawLine(90, 60, Carrom.bw - 90, 60);
			g2d.drawLine(100, 65, Carrom.bw - 100, 65);
			g2d.drawLine(90, 80, Carrom.bw - 90, 80);

			g2d.drawLine(45, 45, 160, 160);
			
//			g2d.enable(G2D.G_CENTER);
//			g2d.drawString("Points: " + StringUtil.commaFormat(score), bx + bw / 2, by + bh + 25);
//			g2d.disable(G2D.G_CENTER);
		}
		g2d.popMatrix();

		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		g2d.setColor(Color.BLACK);
		g2d.drawCircle(Carrom.bw / 2, Carrom.bh / 2, 60);
		g2d.setColor(0xF5DEB3);
		g2d.drawCircle(Carrom.bw / 2, Carrom.bh / 2, 50);
		g2d.setColor(Color.BLACK);
		g2d.drawCircle(Carrom.bw / 2, Carrom.bh / 2, 20);
		g2d.disable(G2D.G_FILL);
		g2d.setColor(cc);
		g2d.drawCircle(Carrom.bw / 2, Carrom.bh / 2, 60);
		g2d.drawCircle(Carrom.bw / 2, Carrom.bh / 2, 50);
		g2d.drawCircle(Carrom.bw / 2, Carrom.bh / 2, 20);
		g2d.disable(G2D.G_CENTER);

		g2d.popMatrix();
	}
	
	public void removeSteggar()
	{
		gotis.remove(steggar);
		steggar.pos.set(Carrom.bw / 2, Carrom.bh - 90);
	}
	
	public void playShot()
	{
		gotis.add(steggar);
	}
	
	public boolean canMoveOn()
	{
		for(int i = 0; i < gotis.size(); i++)
		{
			Goti g = gotis.get(i);
			if(g.vel.lengthSquared() > 1)
			{
				return false;
			}
		}
		return true;
	}
}