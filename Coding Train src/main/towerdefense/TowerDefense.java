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
package main.towerdefense;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;
import main.towerdefense.Spot.SpotType;

public class TowerDefense extends Game
{
	public final int gridW = 30, gridH = 20;
	private final DecimalFormat df = new DecimalFormat("#,###");
	private int hoverX = -1, hoverY = -1;
	private final Spot[][] grid = new Spot[gridW][gridH];
	public final List<Point> path;
	private final List<Enemy> enemies;
	public int health = 100, money = 0;
	
	public TowerDefense() throws Exception
	{
		path = new ArrayList<>();
		File f = new File("C:\\Users\\kayan\\Desktop\\Nothing\\level.png");
		BufferedImage img = ImageIO.read(f);
		Point start = null, end = null;
		for(int x = 0; x < img.getWidth(); x++)
		{
			for(int y = 0; y < img.getHeight(); y++)
			{
				int rgb = img.getRGB(x, y) & 0x00FFFFFF;
				SpotType type = null;
				if(rgb == 0xFF0000 || rgb == 0x00FF00 || rgb == 0x0000FF)
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
		for(int i = 0; i <= 10; i++)
		{
			Enemy e = new Enemy(this, start.x + i, start.y, 10);
			e.currentSpot = i;
			e.health = i;
			e.speed = 0.1F;
			enemies.add(e);
		}
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
			if(x < gridW && y >= 0 && y < gridH && grid[x][y].type.isPath())
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
	public void update(int ticks)
	{
		for(int x = 0; x < gridW; x++)
		{
			for(int y = 0; y < gridH; y++)
			{
				Spot s = grid[x][y];
				if(s.data != null)
				{
					s.data.update();
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
			}
		}
		
		enemies.removeAll(removables);
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < gridW; x++)
		{
			for(int y = 0; y < gridH; y++)
			{
				Spot s = grid[x][y];
				s.paint(g2d, hoverX == x && hoverY == y);
			}
		}
		g2d.setColor(Color.WHITE);
		for(Enemy enemy : enemies)
		{
			enemy.paint(g2d);
		}
		
		g2d.disable(G2D.G_FILL);
		
//		g2d.setColor(Color.BLUE);
//		for(int i = 0; i < path.size() - 1; i++)
//		{
//			Point a = path.get(i);
//			Point b = path.get(i + 1);
//			g2d.drawLine(a.x * 20 + 10, a.y * 20 + 10, b.x * 20 + 10, b.y * 20 + 10);
//		}
		
		g2d.enable(G2D.G_FILL);
		g2d.setColor(Color.RED);
		g2d.drawRectangle(5, 5, health, 10);
		g2d.disable(G2D.G_FILL);

		g2d.pushFont();
		g2d.setFontSize(g2d.getFont().getSize2D() / 2F);
		g2d.enable(G2D.G_CENTER);
		g2d.setColor(Color.WHITE);
		g2d.drawString(health + "%", 55, 10);
		g2d.disable(G2D.G_CENTER);
		g2d.popFont();
		g2d.drawString("$" + df.format(money), 120, 15);

		g2d.setColor(Color.WHITE);
		g2d.drawRectangle(5, 5, 100, 10);
	}
	
	public void mouse(float x, float y, boolean pressed, int button)
	{
		mouseMoved(x, y);
		if(!pressed && button == MouseEvent.BUTTON1)
		{
			if(hoverX != -1 && hoverY != 0)
			{
				
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
				if(x >= x1 * 20 && x < x1 * 20 + 20 && y >= y1 * 20 && y < y1 * 20 + 20)
				{
					hoverX = x1;
					hoverY = y1;
				}
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		GameSettings settings = new GameSettings();
		settings.title = "Tower Defense";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1200;
		settings.height = 800;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		System.setProperty("Main.WIDTH", "600");
		System.setProperty("Main.HEIGHT", "400");

		TowerDefense game = new TowerDefense();
		Main.initialize(game, settings);
	}
}
