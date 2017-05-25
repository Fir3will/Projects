package main.inversekinematics;

import java.awt.Color;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class InverseKinematics extends Game
{
	private final Segment[] segments = new Segment[3];
	
	public InverseKinematics()
	{
		segments[0] = new Segment(Main.WIDTH / 2, Main.HEIGHT / 2, 100);
		for(int i = 1; i < segments.length; i++)
		{
			segments[i] = new Segment(segments[i - 1].b.x, segments[i - 1].b.y, 100);
		}
	}

	@Override
	public void update(int ticks)
	{
		float mx = getHandler().mouseX();
		float my = getHandler().mouseY();
		if(mx > 0 && mx < Main.WIDTH && my > 0 && my < Main.HEIGHT)
		{
			segments[segments.length - 1].follow(getHandler().mouseX(), getHandler().mouseY());
		}
		for(int i = 0; i < segments.length - 1; i++)
		{
			segments[i].follow(segments[i + 1].a.x, segments[i + 1].a.y);
		}
		
//		for(int i = 0; i < segments.length - 1; i++)
//		{
//			segments[i].update();
//		}
		
		segments[0].a.set(Main.WIDTH / 2, Main.HEIGHT);
		segments[0].calculateEnd();
		
		for(int i = 1; i < segments.length; i++)
		{
			segments[i].a.set(segments[i - 1].b);
			segments[i].calculateEnd();
		}
		
		for(int i = 0; i < segments.length; i++)
		{
			segments[i].update();
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		for(Segment s : segments)
		{
			s.show(g2d);
		}
	}

	public static void main(String[] args)
	{
		InverseKinematics game = new InverseKinematics();
		
		GameSettings settings = new GameSettings();
		settings.title = "Forward Kinematics";
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
