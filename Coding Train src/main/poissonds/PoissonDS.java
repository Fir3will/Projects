package main.poissonds;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List; 

import com.hk.math.FloatMath;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class PoissonDS extends Game
{
	private int scl = 25, width = Main.WIDTH / scl, height = Main.HEIGHT / scl;
	private final int k = 30;
	private final Vector2F[][] grid = new Vector2F[width][height];
	private final List<Vector2F> active = new ArrayList<>();
	
	public PoissonDS()
	{
		Vector2F v = new Vector2F(Rand.nextFloat(Main.WIDTH), Rand.nextFloat(Main.HEIGHT));
//		Vector2F v = new Vector2F(Rand.nextFloat(scl), Rand.nextFloat(scl));
		grid[(int) (v.x / scl)][(int) (v.y / scl)] = v;
		active.add(v);
	}
	
	@Override
	public void update(int ticks)
	{
		if(ticks % 3 != 0) return;
		
		if(!active.isEmpty())
		{
			int indx = Rand.nextInt(active.size());
			Vector2F pos = active.get(indx);
			boolean found = false;
			
			for(int i = 0; i < k; i++)
			{
				Vector2F next = new Vector2F(Rand.nextFloat(scl) + scl, 0);
				next.rotateAround(Rand.nextFloat() * FloatMath.PI * 2, true);
				next.addLocal(pos);
				
				int x = (int) (next.x / scl);
				int y = (int) (next.y / scl);
				
				if(inBound(x, y) && grid[x][y] == null)
				{
					boolean okay = true;
					
					for(int a = -1; a <= 1; a++)
					{
						for(int b = -1; b <= 1; b++)
						{
							int x1 = x + a;
							int y1 = y + b;
							
							if(inBound(x1, y1))
							{
								Vector2F o = grid[x1][y1];
								
								if(o != null && o.distance(next) < scl)
								{
									okay = false;
								}
							}
						}
					}
					
					if(okay)
					{
						found = true;
						
						grid[x][y] = next;
						active.add(next);
					}
				}
			}
			
			if(!found)
			{
				active.remove(pos);
			}
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		g2d.setColor(Color.RED);
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				Vector2F v = grid[x][y];
				
				if(v != null)
				{
//					g2d.drawCircle(v.x, v.y, scl / 2F);
					g2d.drawCircle(v.x, v.y, 3);
				}
			}
		}
		g2d.setColor(Color.WHITE);
		for(int i = 0; i < active.size(); i++)
		{
			Vector2F v = active.get(i);
			
			if(v != null)
			{
//				g2d.drawCircle(v.x, v.y, scl / 2F);
				g2d.drawCircle(v.x, v.y, 3);
			}
		}
		g2d.disable(G2D.G_FILL);
	}
	
	public boolean inBound(int x, int y)
	{
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	public void mouse(float x, float y, boolean pressed, int button)
	{
		if(!pressed && button == MouseEvent.BUTTON1)
		{
			active.add(new Vector2F(x, y));
		}
	}

	public static void main(String[] args)
	{
		PoissonDS game = new PoissonDS();
		
		GameSettings settings = new GameSettings();
		settings.title = "Poisson-Disc Sampling";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
}
