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
package main.towerdefense;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.str.StringUtil;

import main.towerdefense.Spot.SpotType;

public class TowerDefense extends GuiScreen
{
	public final int gridW = 30, gridH = 20;
	private int hoverX = -1, hoverY = -1, hoverEnemy = -1;
	private final Spot[][] grid = new Spot[gridW][gridH];
	public final List<Point> path;
	public final List<Enemy> enemies;
	public int health = 100, money = 2500, level = 0;
	
	public TowerDefense(Game game) throws Exception
	{
		super(game);
		path = new ArrayList<>();
		File f = new File("level.png");
		BufferedImage img = ImageIO.read(f);
		Point start = null, end = null;
		for(int x = 0; x < img.getWidth(); x++)
		{
			for(int y = 0; y < img.getHeight(); y++)
			{
				int rgb = img.getRGB(x, y) & 0x00FFFFFF;
				SpotType type = null;
				if(y == 0 || y == 1)
				{
					type = SpotType.NOTHING;
				}
				else if(rgb == 0xFF0000 || rgb == 0x00FF00 || rgb == 0x0000FF)
				{
					if(rgb == 0x0000FF)
					{
						start = new Point(x, y);
					}
					else if(rgb == 0x00FF00)
					{
						end = new Point(x, y);
					}
					
					type = SpotType.PATH;
				}
				else if(rgb == 0xFFFFFF)
				{
					type = SpotType.PLATFORM;
				}
				else if(rgb == 0x000000)
				{
					type = SpotType.NOTHING;
				}
				else
				{
					throw new RuntimeException("Invalid Color: " + x + ", " + y + ", " + Integer.toHexString(rgb));
				}
				
				grid[x][y] = new Spot(this, x, y, type);
			}
		}
		
		path.clear();
		path.addAll(checkPath(start, end));
		
		enemies = new ArrayList<>();
	}
	
	private List<Point> checkPath(Point start, Point end)
	{
		start = Objects.requireNonNull(start);
		end = Objects.requireNonNull(end);
		List<Point> path = new ArrayList<>();
		path.add(start);
		Point p = start;
		while(true)
		{
			p = getValidNeighbor(p, path);
			path.add(p);
			if(p.equals(end))
			{
				break;
			}
		}
		return path;
	}
	
	private Point getValidNeighbor(Point point, List<Point> path)
	{
		int[][] spots = new int[][]
		{
			{-1, 0},
			{1, 0},
			{0, -1},
			{0, 1},
			{-1, -1},
			{-1, 1},
			{1, -1},
			{1, 1}
		};
		for(int i = 0; i < spots.length; i++)
		{
			int x = point.x + spots[i][0];
			int y = point.y + spots[i][1];
			if(x >= 0 && x < gridW && y >= 0 && y < gridH && grid[x][y].type.isPath())
			{
				Point p = new Point(x, y);
				if(!path.contains(p))
				{
					return p;
				}
			}
		}
		throw new RuntimeException("No valid path from (" + point.x + ", " + point.y + ")");
	}
	
	@Override
	public void update(double delta)
	{
		if(health > 0)
		{
			for(int x = 0; x < gridW; x++)
			{
				for(int y = 0; y < gridH; y++)
				{
					Spot s = grid[x][y];
					if(s.data != null)
					{
						s.data.update(delta);
					}
				}
			}
			
			List<Enemy> removables = new ArrayList<>();
			for(Enemy enemy : enemies)
			{
				enemy.update();
				
				if(enemy.isDead())
				{
					removables.add(enemy);
					money++;
				}
			}
			
			enemies.removeAll(removables);
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		for(int x = 0; x < gridW; x++)
		{
			for(int y = 0; y < gridH; y++)
			{
				Spot s = grid[x][y];
				s.paint(g2d, hoverX == x && hoverY == y);
			}
		}
		if(hoverX != -1 && hoverY != -1)
		{
			Spot s = grid[hoverX][hoverY];
			if(s.data != null && s.data.hasTower)
			{
				g2d.enable(G2D.G_FILL);
				g2d.setColor(new Color(0x55000000, true));
				g2d.drawCircle(hoverX * 40 + 20 - s.data.reach, hoverY * 40 + 20 - s.data.reach, s.data.reach);
				g2d.disable(G2D.G_FILL);
				g2d.setColor(new Color(0xBB000000, true));
				g2d.drawCircle(hoverX * 40 + 20 - s.data.reach, hoverY * 40 + 20 - s.data.reach, s.data.reach);
			}
		}
		g2d.setColor(Color.WHITE);
		for(int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).paint(g2d, hoverEnemy == i);
		}
				
//		g2d.setColor(Color.BLUE);
//		for(int i = 0; i < path.size() - 1; i++)
//		{
//			Point a = path.get(i);
//			Point b = path.get(i + 1);
//			g2d.drawLine(a.x * 20 + 10, a.y * 20 + 10, b.x * 20 + 10, b.y * 20 + 10);
//		}
		
		g2d.enable(G2D.G_FILL);
		g2d.setColor(Color.RED);
		g2d.drawRectangle(40, 10, health * 1.5F, 25);
		g2d.disable(G2D.G_FILL);

		g2d.setColor(Color.WHITE);
		g2d.drawRectangle(40, 10, 150, 25);

		g2d.enable(G2D.G_CENTER);
		g2d.setColor(Color.WHITE);
		g2d.drawString(health, 115, 20);
		g2d.disable(G2D.G_CENTER);

		String str = "Money: $" + StringUtil.commaFormat(money);
		g2d.drawString(str, 200, 35);
		if(hoverX != -1 && hoverY != -1)
		{
			Spot s = grid[hoverX][hoverY];
			if(s.type.isPlatform() && s.data != null)
			{
				String extra = null;
				Color clr = null;
				if(!s.data.hasTower)
				{
					clr = money >= 20 ? Color.GREEN : Color.RED;
					extra = " - $20 = " + StringUtil.commaFormat(money - 20);
				}
				else if(s.data.hasTower && s.data.damage == 1)
				{
					clr = money >= 40 ? Color.GREEN : Color.RED;
					extra = " - $40 = " + StringUtil.commaFormat(money - 40);
				}
				
				if(extra != null)
				{
					Rectangle2D bounds = g2d.getStringBounds(str);
					g2d.setColor(clr);
					g2d.drawString(extra, 200 + bounds.getMaxX(), 35);
				}
			}
		}
		g2d.setColor(Color.WHITE);
		g2d.drawString(level == 0 ? "Press [SPACE] to begin!" : "Level: " + level + (enemies.isEmpty() ? ", [SPACE] for next level." : ", Enemies Left: " + enemies.size()), 200, 20);
		
		if(health <= 0)
		{
			g2d.enable(G2D.G_FILL);
			g2d.setColor(new Color(0xAA000000, true));
			g2d.drawRectangle(0, 0, g2d.width, g2d.height);
			g2d.enable(G2D.G_CENTER);
			g2d.setColor(Color.BLACK);
			g2d.drawRectangle(g2d.width / 2, g2d.height / 2, g2d.width / 3, g2d.height / 3);
			g2d.disable(G2D.G_FILL);
			g2d.setColor(Color.WHITE);
			g2d.drawRectangle(g2d.width / 2, g2d.height / 2, g2d.width / 3, g2d.height / 3);
			g2d.drawString("You Lost!", g2d.width / 2, g2d.height * 2 / 5);
			g2d.drawString("Reached Level " + level + "!", g2d.width / 2, g2d.height * 3 / 5);
			g2d.disable(G2D.G_CENTER | G2D.G_FILL);
		}
	}
	
	public void mouse(float x, float y, boolean pressed)
	{
		mouseMoved(x, y);
		int button = handler.getButton();
		if(!pressed)
		{
			if(hoverX != -1 && hoverY != 0)
			{
				grid[hoverX][hoverY].clickedOn(button);
			}
		}
	}
	
	public void mouseMoved(float x, float y)
	{
		hoverX = hoverY = -1;
		for(int x1 = 0; x1 < gridW; x1++)
		{
			for(int y1 = 0; y1 < gridH; y1++)
			{
				if(x >= x1 * 40 && x < x1 * 40 + 40 && y >= y1 * 40 && y < y1 * 40 + 40)
				{
					hoverX = x1;
					hoverY = y1;
				}
			}
		}
		
		hoverEnemy = -1;
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			if(e.position.distance(x, y) <= 20)
			{
				hoverEnemy = i;
			}
		}
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed && key == KeyEvent.VK_SPACE)
		{
			if(enemies.isEmpty())
			{
				level++;
				
				for(int i = 0; i < level + 5; i++)
				{
					Enemy e = new Enemy(this, -i, 4, 45 + level * 2);
					enemies.add(e);
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		Settings settings = new Settings();
		settings.title = "Tower Defense";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1200;
		settings.height = 800;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;


		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new TowerDefense(frame.game));
		frame.launch();
	}
}
