package main.smartrockets;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class SmartRockets extends Game
{
	public static final int MAX = 1000;
	private final Vector2F target;
	private final Rocket[] rockets;
	private final Rectangle obs;
	private int frame, gen;
			
	public SmartRockets()
	{
		target = new Vector2F(Main.WIDTH / 2F, 40);
		rockets = new Rocket[30];
		for(int i = 0; i < rockets.length; i++)
		{
			rockets[i] = new Rocket(null);
		}
		
		obs = new Rectangle(Main.WIDTH / 2, Main.HEIGHT / 2, Main.WIDTH * 2 / 4, 100);
//		obs = new Rectangle(Main.WIDTH / 2, Main.HEIGHT / 2, 10, 10);
		obs.x -= obs.width / 2;
		obs.y -= obs.height / 2;
	}

	@Override
	public void update(int ticks)
	{
		for(Rocket rocket : rockets)
		{
			rocket.applyForce(rocket.dna.velocities[frame]);
			rocket.update();
			
			if(obs.contains(rocket.pos.x, rocket.pos.y))
			{
				rocket.dead = true;
			}

			if(rocket.pos.x < 0 || rocket.pos.x > Main.WIDTH)
			{
				rocket.vel.x /= -0.75F;
			}
			if(rocket.pos.y < 0 || rocket.pos.y > Main.HEIGHT)
			{
				rocket.vel.y /= -0.75F;
			}
			
			if(rocket.pos.distance(target) < 30)
			{
				rocket.complete = true;
			}
		}
		frame++;
		if(frame >= MAX)
		{
			frame = 0;
			
			evolve();
			gen++;
		}
		
//		if(ticks % 5 == 0)
//		{
//			target.x = Main.HEIGHT / 2F - 35;
//			target.y = 0;
//			
//			target.rotateAround(FloatMath.toRadians(ticks / 200F), true);
//			target.addLocal(Main.WIDTH / 2F, Main.HEIGHT / 2F);
//		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.setFontSize(18F);
		g2d.drawString("Generation: " + gen, 5, 15);
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		g2d.drawCircle(target.x, target.y, 30);
		g2d.disable(G2D.G_CENTER | G2D.G_FILL);
		g2d.drawShape(obs);
		for(Rocket rocket : rockets)
		{
			rocket.draw(g2d);
		}
	}
	
	public void evolve()
	{
		List<Rocket> rs = new ArrayList<>();
		float maxWorth = 0;
		
		for(Rocket rocket : rockets)
		{
			float d = rocket.pos.distance(target);
			rocket.worth = Main.WIDTH - d;
			
			rocket.worth = rocket.complete ? rocket.worth * 10 : rocket.worth;
			rocket.worth = rocket.dead ? rocket.worth / 10 : rocket.worth;
			
			if(rocket.worth > maxWorth)
			{
				maxWorth = rocket.worth;
			}
		}
		
		for(Rocket rocket : rockets)
		{
			for(int i = 0; i < rocket.worth / maxWorth * 100; i++)
			{
				rs.add(rocket);
			}
		}
		
		for(int i = 0; i < rockets.length; i++)
		{
			rockets[i] = new Rocket(DNA.child(Rand.nextFrom(rs).dna, Rand.nextFrom(rs).dna));
		}
	}

	public static void main(String[] args)
	{
		System.setProperty("Main.WIDTH", "1024");
		System.setProperty("Main.HEIGHT", "768");
		SmartRockets game = new SmartRockets();
		
		GameSettings settings = new GameSettings();
		settings.title = "Smart Rockets";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.BLACK;
//		settings.maxFPS = 60;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
}
