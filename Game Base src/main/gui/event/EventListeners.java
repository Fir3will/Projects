package main.gui.event;

import java.util.ArrayList;
import java.util.List;
import main.gui.GuiWidget;

public final class EventListeners
{
	public final GuiWidget widget;
	private final List<EventListener> listeners;

	public EventListeners(GuiWidget widget)
	{
		this.widget = widget;
		listeners = new ArrayList<>();
	}

	public void addListener(EventListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(EventListener listener)
	{
		listeners.remove(listener);
	}

	public void removeListener(int index)
	{
		listeners.remove(index);
	}

	public void fireActionPerformed(ActionEvent event)
	{
		for (int i = listeners.size() - 1; i >= 0; i--)
		{
			if (event.consumed)
			{
				break;
			}
			listeners.get(i).actionPerformed(event);
		}
	}
}
