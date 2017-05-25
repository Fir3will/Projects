package main.forwardkinematics;

import java.awt.Color;

import com.hk.math.FloatMath;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.Main;
import main.GameSettings.Quality;

public class ForwardKinematics extends Game
{
	private final Segment[] segments = new Segment[50];
	
	public ForwardKinematics()
	{
		segments[0] = new Segment(Main.WIDTH / 2, Main.HEIGHT, 20);
		for(int i = 1; i < segments.length; i++)
		{
			segments[i] = new Segment(segments[i - 1].b.x, segments[i - 1].b.y, 20);
		}
	}

	@Override
	public void update(int ticks)
	{
		for(int i = 0; i < segments.length; i++)
		{
			segments[i].angle = FloatMath.sin((ticks + i * 50) * (FloatMath.PI / 720F)) - FloatMath.PI / 2F;
		}
		
		for(int i = 0; i < segments.length; i++)
		{
			if(i > 0)
			{
				segments[i].a.set(segments[i - 1].b);
			}
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
		ForwardKinematics game = new ForwardKinematics();
		
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
