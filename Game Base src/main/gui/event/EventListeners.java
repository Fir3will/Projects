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
