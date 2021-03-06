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
package main.flowfield;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.OpenSimplexNoise;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class FlowField extends GuiScreen
{
	private final int scale = 20, w, h;
	private final OpenSimplexNoise osn = new OpenSimplexNoise(Rand.nextLong());
	private final Vector2F[][] vecs;
	private final List<Particle> particles;
	private double time;
			
	public FlowField(Game game)
	{
		super(game);
		particles = new ArrayList<>();
		w = game.width / scale;
		h = game.height / scale;
		
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
			particles.add(new Particle(Rand.nextFloat(w), Rand.nextFloat(h)));
		}
		time = 0;
	}

	@Override
	public void update(double delta)
	{
		time += delta * 0.25F;
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				double angle = osn.eval(x / 10F, y / 10F, time) * Math.PI * 2;
				
				vecs[x][y].set(scale / 2F, 0);
				vecs[x][y].rotateAround((float) angle, false);
//				vecs[x][y].set((float) (Math.cos(angle) * (scale / 2D)), (float) (Math.sin(angle) * (scale / 2D)));
			}
		}
		
		for(Particle p : particles)
		{
			p.update();

			if(p.pos.x < 0 || p.pos.x >= w || p.pos.y < 0 || p.pos.y >= h)
			{
				p.pos.set(Rand.nextFloat(w), Rand.nextFloat(h));
			}
			else
			{
				p.applyForce(vecs[(int) p.pos.x][(int) p.pos.y]);
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE, 0.5F);
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
		
		g2d.setColor(Color.WHITE, 0.2F);
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		for(Particle p : particles)
		{
			g2d.drawCircle(p.pos.x * scale, p.pos.y * scale, 4);
		}
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);
	}
	
	public void mouse(float x, float y, boolean pressed)
	{
		int button = game.handler.getButton();
		if(button == MouseEvent.BUTTON1)
		{
			System.out.println(pressed);
			if(pressed)
			{
				Particle p = new Particle(x, y);
				p.pos.divideLocal(scale);
				particles.add(p);
			}
		}
		else if(button == MouseEvent.BUTTON3 && !pressed)
		{
			for(int i = 0; i < 1000; i++)
			{
				particles.add(new Particle(Rand.nextFloat(w), Rand.nextFloat(h)));
			}
		}
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Noise";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.BLACK;
//		settings.maxFPS = 60;
		settings.maxFPS = 100;
		
		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new FlowField(frame.game));
		frame.launch();
	}
}
