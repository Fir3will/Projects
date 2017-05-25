package main.gui.event;

import main.gui.GuiWidget;

public class BasicEvent extends ActionEvent
{
	public final GuiWidget widget;

	public BasicEvent(GuiWidget widget)
	{
		this.widget = widget;
	}
}
