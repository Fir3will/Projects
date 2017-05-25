package main.gui;

import java.util.ArrayList;
import java.util.List;
import main.G2D;
import main.Handler;
import main.gui.event.ActionEvent;
import main.gui.event.EventListener;
import main.gui.widget.GuiButton;

public abstract class GuiScreen implements EventListener
{
	protected Handler handler;
	private final List<GuiWidget> widgets, addWidgets, removeWidgets;
	private GuiWidget focus;

	public GuiScreen()
	{
		widgets = new ArrayList<>();
		addWidgets = new ArrayList<>();
		removeWidgets = new ArrayList<>();
	}

	public void initialize(Handler handler)
	{
		this.handler = handler;
		widgets.clear();
	}

	public void updateScreen(int ticks)
	{
		updateWidgets(ticks);
	}

	public void paintScreen(G2D g2d, float mouseX, float mouseY)
	{
		paintWidgets(g2d, mouseX, mouseY);
	}

	public void addWidget(GuiWidget widget)
	{
		addWidgets.add(widget);
		if (widget instanceof GuiButton)
		{
			widget.addListener(this);
		}
	}

	public void removeWidget(int index)
	{
		removeWidgets.add(widgets.get(index));
	}

	public void removeWidget(GuiWidget widget)
	{
		removeWidgets.add(widget);
	}

	public void updateWidgets(int ticks)
	{
		widgets.addAll(addWidgets);
		addWidgets.clear();
		widgets.removeAll(removeWidgets);
		removeWidgets.clear();
		for (int i = 0; i < widgets.size(); i++)
		{
			widgets.get(i).updateWidget(ticks);
		}
	}

	public void paintWidgets(G2D g2d, float mouseX, float mouseY)
	{
		for (int i = 0; i < widgets.size(); i++)
		{
			widgets.get(i).paintWidget(g2d, mouseX, mouseY);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event instanceof GuiButton.ButtonEvent)
		{
			buttonPressed(((GuiButton.ButtonEvent) event).button, ((GuiButton.ButtonEvent) event).mouseButton);
		}
	}

	public void buttonPressed(GuiButton button, int mouseButton)
	{}

	public void onGuiClosed()
	{}

	public GuiWidget getFocus()
	{
		return focus;
	}

	public void mouse(float x, float y, boolean pressed, int button)
	{
		if (!pressed)
		{
			focus = null;
			for (int i = 0; i < widgets.size(); i++)
			{
				GuiWidget widget = widgets.get(i);
				if (widget.bounds.contains(x, y))
				{
					if (widget.needsFocus())
					{
						if (focus == widget)
						{
							widget.onClick(x, y, button);
						}
						else
						{
							focus = widget;
						}
					}
					else
					{
						widget.onClick(x, y, button);
					}
				}
			}
		}
	}

	public void mouseMoved(float x, float y)
	{
		for (int i = 0; i < widgets.size(); i++)
		{
			GuiWidget widget = widgets.get(i);
			if (widget.bounds.contains(x, y))
			{
				widget.onMouseOver(x, y);
			}
		}
	}

	public void mouseWheel(int amt)
	{
		for (int i = 0; i < widgets.size(); i++)
		{
			GuiWidget widget = widgets.get(i);
			if (widget.bounds.contains(handler.mouseX(), handler.mouseY()))
			{
				widget.onWheelSpin(amt);
			}
		}
	}

	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if (!pressed)
		{
			if (focus != null)
			{
				focus.onKeyRelease(keyCode, keyChar);
			}
			else
			{
				for (int i = 0; i < widgets.size(); i++)
				{
					GuiWidget widget = widgets.get(i);
					if (widget.bounds.contains(handler.mouseX(), handler.mouseY()))
					{
						widget.onKeyRelease(keyCode, keyChar);
					}
				}
			}
		}
	}
}
