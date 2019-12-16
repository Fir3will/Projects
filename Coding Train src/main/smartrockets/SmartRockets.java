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
package main.smartrockets;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.vector.Vector2F;
import com.hk.str.StringUtil;

public class SmartRockets extends GuiScreen
{
	private final int size = 500, frames = 500;
	private final Car[] cars = new Car[size];
	private final int scl = 10;
	private final boolean[] grid;
	private int frame = 0, gen, btn;
	private float avgFitness = 0, tx, ty;
	private boolean paused, speed;
			
	public SmartRockets(Game game)
	{
		super(game);
		grid = new boolean[(game.width / scl) * (game.height / scl)];
		reset();
		btn = -1;
	}
	
	private void reset()
	{
		speed = false;
		paused = false;
		gen = 0;
		frame = 0;
		Arrays.fill(grid, false);
		for(int i = 0; i < size; i++)
		{
			cars[i] = new Car(null);
		}
	}
	
	private void tick()
	{
		for(int i = 0; i < size; i++)
		{
			cars[i].updateCar(frame);
		}
		frame++;
		if(frame == frames)
		{
			frame = 0;
			nextGeneration();
		}
	}

	@Override
	public void update(double delta)
	{
		if(!paused)
		{
			if(speed)
			{
//				int n = Math.max(1, (int) (Math.random() * (frames - frame - 2)));
				int n = Math.max(1, (int) (Math.random() * frames));
				for(; n > 0; n--)
				{
					tick();
				}
			}
			else
			{
				tick();
			}
		}
		
		if(btn != -1)
		{
			float nx = game.handler.getX();
			float ny = game.handler.getY();
			Vector2F p = new Vector2F(nx, ny);
			Vector2F d = p.subtract(tx, ty).normalizeLocal();
			p.set(tx, ty);
			
			while(p.distanceSquared(nx, ny) >= 4)
			{
				int mx = (int) (p.x / scl);
				int my = (int) (p.y / scl);
				int indx = mx + my * (game.width / scl);
				if(indx >= 0 && indx < grid.length)
				{
					grid[indx] = btn == MouseEvent.BUTTON1;
				}
				p.addLocal(d);
			}
			
			tx = nx;
			ty = ny;
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.RED);
		g2d.enable(G2D.G_FILL);
		for(int i = 0; i < grid.length; i++)
		{
			int x = i % (game.width / scl);
			int y = i / (game.width / scl);
			if(grid[i])
			{
				g2d.drawRectangle(x * scl, y * scl, scl, scl);
			}
		}
		
		for(int i = 0; i < size; i++)
		{
			cars[i].paintCar(g2d);
		}
		g2d.disable(G2D.G_FILL);

		g2d.setColor(Color.RED);
		g2d.drawRectangle(1, 1, g2d.width - 2, g2d.height - 2);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Generation: " + StringUtil.commaFormat(gen), 5, 15);
		g2d.drawString("Average Fitness: " + StringUtil.commaFormat((int) avgFitness), 5, 30);
		if(speed)
		{
			g2d.setColor(Color.GREEN);
			g2d.drawString("Speeding Evolution", 5, 45);
		}
		
		float mx = game.handler.getX() / scl;
		float my = game.handler.getY() / scl;

		g2d.setColor(Color.BLUE);
		g2d.drawRectangle((int) mx * scl, (int) my * scl, scl, scl);
	}
	
	private void nextGeneration()
	{
		gen++;
		float sum = 0;
		for(Car car : cars)
		{
			car.fitness = car.px * car.px;
			if(car.hitWall)
			{
				car.fitness /= 20;
			}
			if(car.hitGoal)
			{
				car.fitness *= 100 * (((float) frames - car.life) / frames + 1);
			}
			sum += car.fitness;
		}
		avgFitness = sum / size;
		for(Car car : cars)
		{
			car.fitness /= sum;
		}
		Car[] newCars = new Car[size];
		for(int i = 0; i < size; i++)
		{
			float[][] dna;
			if(Math.random() < 0.1)
			{
				dna = null;
			}
			else
			{
				Car a = getRandomCar();
				Car b = getRandomCar();
				dna = breed(a.vels, b.vels);
			}
			newCars[i] = new Car(dna);
		}
		System.arraycopy(newCars, 0, cars, 0, size);
	}
	
	private void mutate(float[][] dna)
	{
		int rng = (int) (Math.random() * frames);
		dna[rng] = unitVec();
	}
	
	private float[][] breed(float[][] a, float[][] b)
	{
		float[][] c = new float[frames][2];
		int mid = (int) (Math.random() * (frames - 1)) + 1;
		System.arraycopy(a, 0, c, 0, mid);
		System.arraycopy(b, mid, c, mid, frames - mid);
		mutate(c);
		return c;
	}
	
	private Car getRandomCar()
	{
		float chance = (float) Math.random();
		int i = 0;
		for(; i < size; i++)
		{
			chance -= cars[i].fitness;
			
			if(chance <= 0) break;
		}
		return cars[Math.min(i, cars.length - 1)];
	}
	
	private float[] unitVec()
	{
		float[] v = new float[2];
		double ang = Math.random() * Math.PI * 2;
		v[0] = (float) Math.cos(ang);
		v[1] = (float) Math.sin(ang);
		return v;
	}
	
	public void mouse(float x, float y, boolean pressed)
	{
		if(pressed)
		{
			if(btn == -1)
			{
				tx = x;
				ty = y;
				btn = handler.getButton();
			}
		}
		else
		{
			btn = -1;
		}
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed)
		{
			if(key == KeyEvent.VK_X)
			{
				paused = !paused;
			}
			if(key == KeyEvent.VK_C)
			{
				speed = !speed;
			}
			if(key == KeyEvent.VK_R)
			{
				boolean[] prev = null;
				if(game.handler.isKeyDown(KeyEvent.VK_SHIFT))
				{
					prev = Arrays.copyOf(grid, grid.length);
				}
				reset();
				if(prev != null)
				{
					System.arraycopy(prev, 0, grid, 0, grid.length);
				}
			}
		}
	}
	
	public class Car
	{
		private float px, py, vx, vy, rot, fitness;
		private float[][] vels;
		private boolean hitWall, hitGoal;
		private int life;
		
		public Car(float[][] vels)
		{
			px = game.width / 12;
			py = game.height / 2;
			this.vels = new float[frames][2];
			if(vels == null)
			{
				for(int i = 0; i < frames; i++)
				{
					this.vels[i] = unitVec();
				}
			}
			else
			{
				System.arraycopy(vels, 0, this.vels, 0, frames);
			}
		}
		
		public void updateCar(int frame)
		{
			if(!hitGoal && !hitWall)
			{
				vx += vels[frame][0];
				vy += vels[frame][1];
				float len = (float) Math.sqrt(vx * vx + vy * vy);
				if(len > 9)
				{
					vx = vx / len * 9;
					vy = vy / len * 9;
				}
				px += vx;
				py += vy;
				rot = (float) (Math.atan2(vy, vx) * 180 / Math.PI);
				boolean hit = false;
				
				if(px > game.width)
				{
					hitGoal = true;
					hit = true;
				}
				if(py < 0 || py >= game.height || px < 0)
				{
					hitWall = true;
					hit = true;
				}
				int cx = (int) (px / scl);
				int cy = (int) (py / scl);
				int indx = cx + cy * (game.width / scl);
				if(indx < 0 || indx >= grid.length || grid[indx])
				{
					hitWall = true;
					hit = true;
				}
				
				if(hit)
				{
					life = frame;
				}
			}
		}
		
		public void paintCar(G2D g2d)
		{
			Color clr = Color.WHITE;
			if(hitWall)
			{
				clr = Color.RED;
			}
			else if(hitGoal)
			{
				clr = Color.GREEN;
			}
			g2d.setColor(clr, 0.6F);
			g2d.translate(px, py);
			g2d.rotate(rot);
			g2d.drawRectangle(-15, -7.5, 30, 15);
			g2d.rotate(-rot);
			g2d.translate(-px, -py);
		}
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Smart Rockets";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1280;
		settings.height = 720;
		settings.showFPS = true;
		settings.background = Color.BLACK;
//		settings.maxFPS = 60;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new SmartRockets(frame.game));
		frame.launch();
	}
}
