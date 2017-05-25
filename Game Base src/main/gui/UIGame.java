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
