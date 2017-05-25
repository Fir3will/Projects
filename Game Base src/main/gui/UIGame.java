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
package main.gui;

import java.util.Objects;
import main.G2D;
import main.Game;

public abstract class UIGame extends Game
{
	protected GuiScreen currScreen;

	@Override
	public void update(int ticks)
	{
		currScreen.updateScreen(ticks);
	}

	@Override
	public void paint(G2D g2d)
	{
		currScreen.paintScreen(g2d, getHandler().mouseX(), getHandler().mouseY());
	}

	public void setCurrScreen(GuiScreen screen)
	{
		Objects.requireNonNull(screen);
		if (currScreen != screen)
		{
			if (currScreen != null)
			{
				currScreen.onGuiClosed();
			}
			screen.initialize(getHandler());
			currScreen = screen;
		}
	}

	@Override
	public void mouse(float x, float y, boolean pressed, int button)
	{
		currScreen.mouse(x, y, pressed, button);
	}

	@Override
	public void mouseMoved(float x, float y)
	{
		currScreen.mouseMoved(x, y);
	}

	@Override
	public void mouseWheel(int amt)
	{
		currScreen.mouseWheel(amt);
	}

	@Override
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		currScreen.key(keyCode, keyChar, pressed);
	}
}
