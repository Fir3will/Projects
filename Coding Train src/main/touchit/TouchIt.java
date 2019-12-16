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
package main.touchit;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hk.array.ArrayUtil;
import com.hk.g2d.G2D;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;

public class TouchIt extends GuiScreen
{
	public final static int SCL = 10;
	public final int wt, ht;
	public final Cube[][] grid;
	public final List<Shatter> shatters;
	public final Trail player;
	private final List<Trail> trails;
	private int count;
	
	public TouchIt(com.hk.g2d.Game game)
	{
		super(game);
		
		wt = game.width / SCL;
		ht = game.height / SCL;
		trails = new ArrayList<>();
		shatters = new ArrayList<>();
		
		grid = new Cube[wt][ht];	
		for(int x = 0; x < wt; x++)
		{
			for(int y = 0; y < ht; y++)
			{
				grid[x][y] = new Cube(this, x, y);
			}
		}
		count = 0;

		trails.add(player = new Trail(this, wt / 2, ht / 2, 1));
	}
	
	@Override
	public void update(double delta)
	{
		int[] arr = new int[wt * ht];
		
		for(int i = 0; i < arr.length; i++)
		{
			arr[i] = i;
		}
		ArrayUtil.shuffleArray(arr);
		
		for(int x1 = 0; x1 < wt; x1++)
		{
			for(int y1 = 0; y1 < ht; y1++)
			{
				int i = arr[x1 + y1 * wt];
				int x2 = i % wt;
				int y2 = i / wt;
				if(grid[x2][y2].updateCube(delta))
				{
					grid[x2][y2].team = 0;
					grid[x2][y2].life = 0;
				}
			}
		}
		
		for(int i = 0; i < trails.size(); i++)
		{
			Trail t = trails.get(i);
			t.updateTrail(delta);
			
			if(t.dead)
			{
				if(t.team == 1)
				{
					t.dead = false;
				}
				else
				{
					trails.remove(i);
					i--;
				}
			}
		}
		
		for(int i = 0; i < shatters.size(); i++)
		{
			Shatter sh = shatters.get(i);
			
			if(sh.updateShatter(delta))
			{
				shatters.remove(i--);
			}
		}

		while(trails.size() < 5)
		{
			trails.add(new Trail(this, Rand.nextInt(wt), Rand.nextInt(ht), (count++) % 6 + 2));
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < wt; x++)
		{
			for(int y = 0; y < ht; y++)
			{
				if(grid[x][y].team != 0)
					grid[x][y].paintCube(g2d);
			}
		}
		g2d.disable(G2D.G_FILL);
		
		g2d.setLineWidth(2);
		for(int i = 0; i < shatters.size(); i++)
		{
			Shatter sh = shatters.get(i);
			sh.paintShatter(g2d);
		}
		g2d.setLineWidth(1);
	}

	public List<Cube> getTrail(int cx, int cy)
	{
		Cube curr = grid[cx][cy];
		int team = curr.team;
		if(team == 0)
			return Collections.emptyList();

		List<Cube> lst = new ArrayList<>();
		while(curr != null && curr.team == team && !lst.contains(curr))
		{
			lst.add(curr);
			curr = curr.prev;
		}
		return lst;
	}
	
	public boolean inBounds(int x, int y)
	{
		return x >= 0 && x < wt && y >= 0 && y < ht;
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		if(pressed)
			return;

		if(key == KeyEvent.VK_UP)
		{
			player.dir = 0;
		}
		if(key == KeyEvent.VK_RIGHT)
		{
			player.dir = 1;
		}
		if(key == KeyEvent.VK_DOWN)
		{
			player.dir = 2;
		}
		if(key == KeyEvent.VK_LEFT)
		{
			player.dir = 3;
		}
	}
	
	public static void main(String[] args)
	{		
		Settings settings = new Settings();
		settings.title = "Touch It  Bring It Babe Watch It Turn It Leave It";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 800;
		settings.height = 800;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new TouchIt(frame.game));
		frame.launch();
	}
}
