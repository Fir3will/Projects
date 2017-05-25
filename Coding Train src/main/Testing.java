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
