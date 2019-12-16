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

import java.util.ArrayList;
import java.util.List;

public abstract class GuiScreen
{
	public final Game game;
	public final Handler handler;
	List<GuiOverlay> overlays;
	
	public GuiScreen(Game game)
	{
		this.game = game;
		this.handler = game.handler;
		overlays = new ArrayList<>();
	}
	
	public void initialize()
	{}

	public abstract void update(double delta);

	public abstract void paint(G2D g2d);

	public void onGuiClose() {}

	public void mouse(float x, float y, boolean pressed)
	{}
	
	public void mouseMoved(float x, float y)
	{}

	public void mouseWheel(int amt)
	{}

	public void key(int key, char keyChar, boolean pressed)
	{}

	public void addOverlay(GuiOverlay overlay)
	{
		overlay.initialize(game);
		overlays.add(overlay);
	}
}
