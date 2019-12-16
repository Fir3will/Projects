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
package main.clock;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.shape.Rectangle;
import com.hk.math.vector.Vector2F;

public class Clocks extends GuiScreen
{
	private Vector2F last;
	private final List<Clock> clocks;
	private float scrollAmt = 0, scrollV = 0;
	private long frozen = -1;
	private Font theFont;
	
	public Clocks(Game game)
	{
		super(game);
		
		clocks = new ArrayList<>();
		
		clocks.add(new ClockColored());			
		clocks.add(new ClockRegular());			
		clocks.add(new ClockCircular());			
		clocks.add(new ClockBars());
		clocks.add(new ClockBinary());
		clocks.add(new ClockLighted());
		clocks.add(new ClockTextWall());
		clocks.add(new ClockGrowingText());
	}
	
	@Override
	public void update(double delta)
	{
		float f = (float) (Math.ceil(clocks.size() / 2D) * 410 - 900);
		scrollAmt += scrollV;
		scrollV *= 0.95F;
		
		if(scrollAmt < 0)
		{
			scrollV = -scrollAmt / 50F;
		}
		else if(scrollAmt > f)
		{
			scrollV = -(scrollAmt - f) / 50F;
		}
		
		if(last != null && last.distance(game.handler.getX(), game.handler.getY()) > 10)
		{
			scrollV = last.y - game.handler.getY();
			last.set(game.handler.getX(), game.handler.getY());
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		if(theFont == null)
		{
			theFont = new Font("Courier", Font.BOLD, 12);
		}
		g2d.setFont(theFont);
		Rectangle r = new Rectangle(400, 400);
		long t = frozen == -1 ? System.currentTimeMillis() : frozen;
		Time time = new Time(t);
		
		g2d.translate(0, -scrollAmt);
		int start = Math.max(0, (int) (scrollAmt / 410) * 2);
		
		for(int i = start; i < Math.min(clocks.size(), start + 6); i++)
		{
			Clock c = clocks.get(i);
			r.x = (i % 2) * 410 + 5;
			r.y = (i / 2) * 410 + 5;
			
			g2d.pushColor();
			g2d.pushFont();
			g2d.pushMatrix();
			g2d.translate(r.x, r.y);
			c.drawClock(g2d, time, r.width, r.height);
			g2d.popMatrix();
			g2d.popFont();
			g2d.popColor();
			
			if(r.contains(game.handler.getX(), game.handler.getY() + scrollAmt))
			{
				g2d.enable(G2D.G_FILL);
				g2d.setColor(0, 0, 0, 0.4F);
				g2d.drawRectangle(r.x, r.y, r.width, r.height);
				g2d.disable(G2D.G_FILL);
				
				g2d.pushFont();
				g2d.setColor(Color.WHITE);
				g2d.setFontSize(g2d.getFont().getSize2D() * 2F);
				g2d.drawShadowedString(c.name, r.x + r.width * 0.05F, r.y + r.height * 0.95F, Color.BLACK);
				g2d.popFont();
			}
			
			g2d.setColor(Color.WHITE);
			g2d.drawRectangle(r.x, r.y, r.width, r.height);
		}
	}

	public void mouse(float x, float y, boolean pressed)
	{
		if(last == null)
		{
			last = new Vector2F(x, y);
		}

		if(!pressed)
		{
			last = null;
		}
	}

	public void mouseWheel(int amt)
	{
		scrollV += amt * 3F;
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed)
		{
			if(key == KeyEvent.VK_SPACE)
			{
				if(frozen == -1)
				{
					frozen = System.currentTimeMillis();
				}
				else
				{
					frozen = -1;
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Clocks";
		settings.quality = Quality.GOOD;
		settings.width = 820;
		settings.height = 900;
		settings.background = Color.BLACK;
		settings.maxFPS = 60;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Clocks(frame.game));
		frame.launch();
	}
	
	public static abstract class Clock
	{
		public final String name;
		
		protected Clock(String name)
		{
			this.name = name;
		}
		
		public abstract void drawClock(G2D g2d, Time currentTime, float width, float height);
	}
}
