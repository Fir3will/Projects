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
package main.flowfield;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.hk.math.OpenSimplexNoise;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class FlowField extends Game
{
	private final int scale = 40, w, h;
	private final OpenSimplexNoise osn = new OpenSimplexNoise(Rand.nextLong());
	private final Vector2F[][] vecs;
	private final List<Particle> particles;
			
	public FlowField()
	{
		particles = new ArrayList<>();
		w = Main.WIDTH / scale;
		h = Main.HEIGHT / scale;
		
		vecs = new Vector2F[w][h];
		
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				vecs[x][y] = new Vector2F();
			}
		}
		
		for(int i = 0; i < 1000; i++)
		{
			Particle p = new Particle();
			p.pos.x = Rand.nextFloat(w);
			p.pos.y = Rand.nextFloat(h);
			particles.add(p);
		}
	}

	@Override
	public void update(int ticks)
	{
		double z = ticks / 50F;

		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				double angle = osn.eval(x / 10F, y / 10F, z) * Math.PI;
				
				vecs[x][y].set((float) (Math.cos(angle) * (scale / 2D)), (float) (Math.sin(angle) * (scale / 2D)));
			}
		}
		
		for(Particle p : particles)
		{
			p.update();

			if(p.pos.x < 0)
			{
				p.pos.x = w;
			}
			if(p.pos.x > w)
			{
				p.pos.x = 0;
			}
			if(p.pos.y < 0)
			{
				p.pos.y = h;
			}
			if(p.pos.y > h)
			{
				p.pos.y = 0;
			}

			int x = (int) p.pos.x;
			int y = (int) p.pos.y;
			
			if(x >= 0 && x < w && y >= 0 && y < h)
			{
				p.applyForce(vecs[x][y]);
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{				
				g2d.pushMatrix();
				g2d.translate(x * scale + scale / 2D, y * scale + scale / 2D);
				g2d.drawLine(0, 0, vecs[x][y].x, vecs[x][y].y);
				g2d.popMatrix();
			}
		}
		
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		for(Particle p : particles)
		{
			g2d.drawCircle(p.pos.x * scale, p.pos.y * scale, 4);
		}
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);
	}
	
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(button == MouseEvent.BUTTON1)
		{
			System.out.println(pressed);
			if(pressed)
			{
				Particle p = new Particle();
				p.pos.set(x, y);
				p.pos.divideLocal(scale);
				particles.add(p);
			}
		}
		else if(button == MouseEvent.BUTTON3 && !pressed)
		{
			for(int i = 0; i < 1000; i++)
			{
				Particle p = new Particle();
				p.pos.x = Rand.nextFloat(w);
				p.pos.y = Rand.nextFloat(h);
				particles.add(p);
			}
		}
	}

	public static void main(String[] args)
	{
		FlowField game = new FlowField();
		
		GameSettings settings = new GameSettings();
		settings.title = "Noise";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
//		settings.maxFPS = 60;
		settings.maxFPS = 100;
		
		Main.initialize(game, settings);
	}
}
