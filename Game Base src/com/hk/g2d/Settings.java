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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Settings
{
	public boolean resizeable = true, showFPS = false, refresh = true;
	public int width = 1280, height = 720;
	public int maxFPS = 60, closeOperation = JFrame.EXIT_ON_CLOSE;
	public String title = "Game", version = null;
	public Quality quality = Quality.GOOD;
	public Color background = Color.WHITE;
	public Dimension frame = null;
	
	public static enum Quality
	{
		POOR,
		AVERAGE,
		GOOD;
	}
}
