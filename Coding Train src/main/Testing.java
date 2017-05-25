package main;

import java.awt.Color;

import main.GameSettings.Quality;
import main.gui.UIGame;

public class Testing extends UIGame
{
	public Testing()
	{
		
	}
	
	public void initialize()
	{
		setCurrScreen(new TestScreen(this));
	}

	public static void main(String[] args)
	{
		System.setProperty("Main.WIDTH", String.valueOf(1200));
		System.setProperty("Main.HEIGHT", String.valueOf(900));
		Testing game = new Testing();
		
		GameSettings settings = new GameSettings();
		settings.title = "Testing";
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
