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
package main.twozerofoureight;

import java.awt.Color;
import java.awt.Point;
import java.util.Arrays;

import com.hk.array.ArrayUtil;
import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;
import com.hk.math.shape.Rectangle;
import com.sun.glass.events.KeyEvent;

public class TwoZeroFourEight extends GuiScreen
{
	private int[][] nextGrid = null;
	private int[][] grid = new int[4][4];
	private int score;
	
	public TwoZeroFourEight(Game game)
	{
		super(game);
		reset();
	}
	
	public void reset()
	{
		score = 0;
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 4; y++)
			{
				set(x, y, 0);
			}
		}
		for(int i = 0; i < 2; i++)
		{
			randomSpot();
		}
	}
	
	@Override
	public void update(double delta)
	{
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setFontSize(48F);
		g2d.enable(G2D.G_FILL);
		g2d.setColor(new Color(0x776E65).brighter());
		g2d.drawRectangle(0, 0, g2d.width, g2d.height);
		
		g2d.setColor(new Color(0x776E65).brighter().brighter());
		float w = g2d.width / 4F;
		float h = g2d.height / 4F;
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 4; y++)
			{
				int num = get(x, y);
				Rectangle r = new Rectangle(x * w, y * h, w, h);
				
				if(num > 0)	g2d.setColor(new Color(0x776E65).brighter().brighter());
				else g2d.setColor(new Color(0x776E65));

				r.grow(-10, -10);
				g2d.drawRoundRectangle(r.x, r.y, r.width, r.height, w / 10, h / 10);

				if(num > 0)
				{
					g2d.pushColor();
					g2d.setColor(Color.BLACK);
					g2d.enable(G2D.G_CENTER);
					g2d.drawString(num, r.getCenterX(), r.getCenterY());
					g2d.disable(G2D.G_CENTER);
					g2d.popColor();
				}
			}
		}
		g2d.disable(G2D.G_FILL);
		g2d.setFontSize(12F);
		g2d.setColor(Color.BLACK);
		g2d.drawShadowedString("Score: " + score, g2d.height - 55, g2d.height - 15, Color.YELLOW);
	}

	private void randomSpot()
	{
		Point[] points = new Point[16];
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 4; y++)
			{
				points[x + y * 4] = new Point(x, y);
			}
		}
		ArrayUtil.shuffleArray(points);
		Point p = null;
		
		for(int i = 0; i < points.length; i++)
		{
			if(get(points[i].x, points[i].y) == 0)
			{
				p = points[i];
				break;
			}
		}
		
		if(p != null)
		{
			set(p.x, p.y, Rand.nextBoolean() ? 2 : 4);
		}
		else
		{
			System.err.println("Can't choose random spot, it's already filled");
			System.exit(-1);
		}
	}
	
	private void move(boolean horiz, boolean reverse)
	{
		nextGrid = new int[4][4];
		for(int x = 0; x < 4; x++)
		{
			for(int y = 0; y < 4; y++)
			{
				nextGrid[x][y] = x + y * 4;
			}
		}
		System.out.println(Arrays.deepToString(nextGrid));
		nextGrid = null;
		for(int y = 0; y < 4; y++)
		{
			int[] na = new int[4];
			int indx = reverse ? 0 : 3;
			int last = -1;
			for(int x = 3; x >= 0; x--)
			{
				int rx = horiz ? reverse ? 3 - x : x : y;
				int ry = horiz ? y : reverse ? 3 - x : x;
				//int nextIndx = rx + ry * 4;
				int n = get(rx, ry);
				
				if(last == n)
				{
					na[reverse ? indx - 1 : indx + 1] = n * 2;
					score++;
					n = -1;
				}
				
				if(n != 0)
				{
					last = n;
					if(n != -1)
					{
						na[reverse ? indx++ : indx--] = n;
					}
				}
			}
			
			for(int i = 0; i < 4; i++)
				set(horiz ? i : y, horiz ? y : i, na[i]);
		}
	}
	
	public int get(int x, int y)
	{
		return grid[x][y];
	}
	
	public void set(int x, int y, int val)
	{
		grid[x][y] = val;
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed && nextGrid == null)
		{
			if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
			{
				move(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT, key == KeyEvent.VK_LEFT || key == KeyEvent.VK_UP);
				randomSpot();
			}
		}
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "2048";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 600;
		settings.height = 600;
		settings.showFPS = false;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new TwoZeroFourEight(frame.game));
		frame.launch();
	}
}
