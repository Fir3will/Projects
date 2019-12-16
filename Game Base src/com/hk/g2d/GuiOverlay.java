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
package com.hk.g2d;

public abstract class GuiOverlay
{
	boolean remove = false, visible = true;

	public void initialize(Game game)
	{}

	public abstract void updateOverlay(double delta);

	public abstract void paintOverlay(G2D g2d);

	public boolean mouse(float x, float y, boolean pressed)
	{
		return false;
	}
	
	public boolean mouseMoved(float x, float y)
	{
		return false;
	}

	public boolean mouseWheel(int amt)
	{
		return false;
	}

	public boolean key(int key, char keyChar, boolean pressed)
	{
		return false;
	}

	public void removeSelf()
	{
		remove = true;
	}
}
