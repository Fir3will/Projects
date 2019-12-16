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
package main.newtest;

import java.awt.Color;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;

public class Test extends GuiScreen
{
	public Test(Game game)
	{
		super(game);
	}
	
	@Override
	public void update(double delta)
	{
		
	}

	@Override
	public void paint(G2D g2d)
	{
		
	}

	public void mouse(float x, float y, boolean pressed)
	{
		
	}
	
	public void mouseMoved(float x, float y)
	{
		
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Test Dis Bitch";
		settings.width = 1600;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;		

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Test(frame.game));
		frame.launch();
	}
}
