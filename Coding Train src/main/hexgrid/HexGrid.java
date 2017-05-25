package main.hexgrid;

import java.awt.Color;

import main.G2D;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;
import main.gui.UIGame;

public class HexGrid extends UIGame
{
	public HexGrid()
	{
	}
	
	public void initialize()
	{
		setCurrScreen(new GameScreen(this));
	}
	
	@Override
	public void update(int ticks)
	{
		super.update(ticks);
	}

	@Override
	public void paint(G2D g2d)
	{
		super.paint(g2d);
	}
	
	public static void main(String[] args)
	{
		System.setProperty("Main.WIDTH", "1200");
		System.setProperty("Main.HEIGHT", "900");
		HexGrid game = new HexGrid();

		GameSettings settings = new GameSettings();
		settings.title = "Hexagonal Grid";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1200;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
}
