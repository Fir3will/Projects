package main.gui;

import com.hk.math.shape.Rectangle;
import main.G2D;
import main.gui.event.EventListener;
import main.gui.event.EventListeners;

public abstract class GuiWidget
{
	public final GuiScreen screen;
	protected final Rectangle bounds;
	protected EventListeners listeners;

	public GuiWidget(GuiScreen screen)
	{
		this.screen = screen;
		bounds = new Rectangle(1, 1);
		listeners = new EventListeners(this);
	}

	public GuiWidget centerWidget()
	{
		bounds.x -= bounds.width / 2F;
		bounds.y -= bounds.height / 2F;
		return this;
	}

	public void initialize()
	{}

	public void updateWidget(int ticks)
	{}

	public abstract void paintWidget(G2D g2d, float mouseX, float mouseY);

	public GuiWidget setBounds(float x, float y, float width, float height)
	{
		bounds.set(x, y, width, height);
		return this;
	}

	public GuiWidget setBounds(float width, float height)
	{
		bounds.set(width, height);
		return this;
	}

	public Rectangle getBounds()
	{
		return bounds;
	}

	public boolean needsFocus()
	{
		return false;
	}

	public boolean isCurrentFocus()
	{
		return screen.getFocus() == this;
	}

	public GuiWidget addListener(EventListener listener)
	{
		listeners.addListener(listener);
		return this;
	}

	public GuiWidget removeListener(EventListener listener)
	{
		listeners.removeListener(listener);
		return this;
	}

	public void onClick(float x, float y, int button)
	{}

	public void onMouseOver(float x, float y)
	{}

	public void onWheelSpin(int amt)
	{}

	public void onKeyRelease(int keyCode, char keyChar)
	{}
}
