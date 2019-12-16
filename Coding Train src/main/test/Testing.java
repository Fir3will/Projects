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
package main.test;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;

public class Testing extends GuiScreen
{
	public Testing(Game game)
	{
		super(game);
	}

	@Override
	public void update(double delta)
	{}

	@Override
	public void paint(G2D g2d)
	{
		
	}

	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Randomness";
		settings.quality = Quality.POOR;
		settings.width = 600;
		settings.height = 600;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Testing(frame.game));
		frame.launch();
	}
}
