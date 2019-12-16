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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

import com.hk.g2d.Settings.Quality;

public class Game
{
	public final int width, height;
	public final Handler handler;
	private final Settings settings;
	private final G2D g2d;
	private final Dimension frame;
	private GuiScreen current;
	private long last;
	
	public Game(Settings settings, Handler handler)
	{
		this.settings = settings;
		this.handler = handler;
		width = settings.width;
		height = settings.height;
		g2d = new G2D(width, height);
		frame = new Dimension();
		last = System.nanoTime();
	}

	public void setCurrentScreen(GuiScreen scr)
	{
		if (current != null)
		{
			current.onGuiClose();
		}
		current = scr;
		if (scr != null)
		{
			scr.initialize();
		}
	}
	
	public void update()
	{
		long time = System.nanoTime();
		double delta = (time - last) / 1E9D;
		last = time;
		
		update(delta);
	}
	
	public void update(double delta)
	{
		current.update(delta);
		
		Iterator<GuiOverlay> itr = current.overlays.iterator();
		while(itr.hasNext())
		{
			GuiOverlay ovl = itr.next();
			if(ovl.remove)
			{
				itr.remove();
				continue;
			}
			
			if(ovl.visible)
			{
				ovl.updateOverlay(delta);
			}
		}
	}

	public void paint(Graphics2D graphic, int w, int h)
	{
		if(settings.frame == null)
			frame.setSize(width, height);
		else
			frame.setSize(settings.frame);
		
		graphic.scale(w * 1D / frame.width, h * 1D / frame.height);

		g2d.g2d = graphic;
		if (settings.quality == Quality.GOOD)
		{
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}
		else if (settings.quality == Quality.AVERAGE)
		{
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		}
		else if (settings.quality == Quality.POOR)
		{
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		}
		g2d.reset();
		AffineTransform aft = graphic.getTransform();
		g2d.setColor(Color.GRAY);

		current.paint(g2d);
		
		for(GuiOverlay ovl : current.overlays)
		{
			if(ovl.visible)
			{
				ovl.paintOverlay(g2d);
			}
		}
		
		graphic.setTransform(aft);
		g2d.g2d = null;
	}

	public void mouse(float x, float y, boolean pressed)
	{
		for(int i = current.overlays.size() - 1; i >= 0; i--)
		{
			GuiOverlay ovl = current.overlays.get(i);
			if(ovl.visible && ovl.mouse(x, y, pressed))
			{
				return;
			}
		}
		current.mouse(x, y, pressed);
	}
	
	public void mouseMoved(float x, float y)
	{
		for(int i = current.overlays.size() - 1; i >= 0; i--)
		{
			GuiOverlay ovl = current.overlays.get(i);
			if(ovl.visible && ovl.mouseMoved(x, y))
			{
				return;
			}
		}
		current.mouseMoved(x, y);
	}

	public void mouseWheel(int amt)
	{
		for(int i = current.overlays.size() - 1; i >= 0; i--)
		{
			GuiOverlay ovl = current.overlays.get(i);
			if(ovl.visible && ovl.mouseWheel(amt))
			{
				return;
			}
		}
		current.mouseWheel(amt);
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		for(int i = current.overlays.size() - 1; i >= 0; i--)
		{
			GuiOverlay ovl = current.overlays.get(i);
			if(ovl.visible && ovl.key(key, keyChar, pressed))
			{
				return;
			}
		}
		current.key(key, keyChar, pressed);
	}
}
