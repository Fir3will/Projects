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
package main;

public abstract class Game
{
	int ticks;
	Handler handler;

	public void initialize()
	{}

	public abstract void update(int ticks);

	public abstract void paint(G2D g2d);

	public final Handler getHandler()
	{
		return handler;
	}

	public int getTicks()
	{
		return ticks;
	}

	public void mouse(float x, float y, boolean pressed, int button)
	{}

	public void mouseMoved(float x, float y)
	{}

	public void mouseWheel(int amt)
	{}

	public void key(int key, char keyChar, boolean pressed)
	{}
}
