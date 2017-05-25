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

import java.awt.Color;
import javax.swing.JFrame;

public class GameSettings
{
	public boolean fullscreen = false, constantGCCalls = false, resizeable = true, showFPS = false;
	public int width = 640, height = 480, maxFPS = 30, closeOperation = JFrame.EXIT_ON_CLOSE;
	public String title = "Game", version = null;
	public Quality quality = Quality.AVERAGE;
	public Color background = Color.WHITE;

	public GameSettings()
	{}

	GameSettings(GameSettings s)
	{
		fullscreen = s.fullscreen;
		constantGCCalls = s.constantGCCalls;
		resizeable = s.resizeable;
		showFPS = s.showFPS;
		width = s.width;
		height = s.height;
		maxFPS = s.maxFPS;
		closeOperation = s.closeOperation;
		title = s.title;
		version = s.version;
		quality = s.quality;
		background = s.background;
	}

	public static enum Quality
	{
		POOR,
		AVERAGE,
		GOOD;
	}
}
