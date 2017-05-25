package main.planetjumper;

import java.awt.Color;

import com.hk.math.vector.Vector2F;

import main.G2D;

public class Player
{
	public final Jumper game;
	public final Vector2F pos, vel, acc;
	public float rot, rotVel;
	
	public Player(Jumper game)
	{
		this.game = game;
		pos = new Vector2F();
		vel = new Vector2F();
		acc = new Vector2F();
		
		rot = 0;
		rotVel = 0;
	}
	
	public void updatePlayer(int ticks)
	{
		vel.addLocal(acc);
		pos.addLocal(vel);
		acc.zero();
		
		rot += rotVel;
		while(rot > 360)
		{
			rot -= 360;
		}
		while(rot < 0)
		{
			rot += 360;
		}
	}
	
	public void paintPlayer(G2D g2d)
	{
		g2d.pushMatrix();
		g2d.setColor(Color.WHITE);
		g2d.rotate(rot, pos.x, pos.y);
		g2d.drawRectangle(pos.x - 5, pos.y - 5, 10, 10);
		g2d.setColor(Color.RED);
		g2d.drawLine(pos.x - 5, pos.y - 5, pos.x - 5, pos.y + 5);
		g2d.popMatrix();
	}
}
