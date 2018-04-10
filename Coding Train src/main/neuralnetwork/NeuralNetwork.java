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
package main.neuralnetwork;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

import com.hk.collections.lists.ListUtil;
import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.math.shape.Rectangle;

public class NeuralNetwork extends Game
{
	private final int[] amtDrawn = new int[10];
	private final boolean[][] drawing = new boolean[16][16];
	private int currentNumber = -1;
	private int currX = -1, currY = -1;
	private Rectangle doneButton, clearButton;
	private int delay = 0;
	private boolean debug;
	private Network neuralNetwork;
	private float[][] results;
	
	public NeuralNetwork()
	{
		doneButton = new Rectangle(20, 740, 300, 80);
		clearButton = new Rectangle(340, 740, 300, 80);
		currentNumber = getRandNumber();
		neuralNetwork = new Network();
		try
		{
//			neuralNetwork.train(50);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		/*
		 * Try using the weighted method with the final nodes instead. Apply a weight to everything.
		 * It'll also make it easier to have it learn as the weights can be tweaked depending on the
		 * actual number supposed to be drawn. Should the weights be 0 - 1?
		 *
		 * Have much more edge pieces, not just for numbers, but possibly letters?
		 *
		 * This shit is lit...
		 */
	}

	@Override
	public void update(int ticks)
	{
		delay = Math.max(0, delay - 1);
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.BLACK);
		g2d.drawRectangle(0, 0, g2d.width - 1, g2d.height - 1);
		g2d.enable(G2D.G_FILL);
		g2d.drawRectangle(20 - 3, 20 - 3, 640 + 6, 640 + 6);
		g2d.disable(G2D.G_FILL);
		
		g2d.setColor(Color.WHITE);
		if(delay <= 0)
		{
			float[][] map = debug ? null : generateMap(drawing);
			g2d.enable(G2D.G_FILL);
			for(int i = 0; i < 16; i++)
			{
				for(int j = 0; j < 16; j++)
				{
					float f = debug ? (drawing[i][j] ? 1 : 0) : map[i][j];
					g2d.setColor(f, f, f);
					g2d.drawRectangle(20 + i * 40, 20 + j * 40, 40, 40);
					
					if(debug)
					{
						g2d.setColor(Color.WHITE);
						g2d.disable(G2D.G_FILL);
						g2d.drawRectangle(20 + i * 40, 20 + j * 40, 40, 40);
						g2d.enable(G2D.G_FILL);
					}
				}
			}
			g2d.disable(G2D.G_FILL);
		}
		g2d.setColor(Color.BLACK);
		g2d.setFontSize(18);
		g2d.enable(G2D.G_CENTER);
		g2d.drawString("Draw a '" + currentNumber + "'", 330, 680);
		g2d.disable(G2D.G_CENTER);
		
		g2d.drawRectangle(doneButton.x, doneButton.y, doneButton.width, doneButton.height);
		g2d.drawRectangle(clearButton.x, clearButton.y, clearButton.width, clearButton.height);
		
		g2d.enable(G2D.G_CENTER);
		g2d.drawString("Done", doneButton.getCenterX(), doneButton.getCenterY());
		g2d.drawString("Clear", clearButton.getCenterX(), clearButton.getCenterY());
		g2d.disable(G2D.G_CENTER);
				
		int x = 680;
		int y = 20;
		g2d.drawRectangle(x, y, 900, g2d.height - 40);
		
		if(results != null)
		{
			float max = -1;
			int maxIndx = -1;
			g2d.enable(G2D.G_FILL);
			for(int i = 0; i < results.length; i++)
			{
				boolean last = i == results.length - 1;
				
				float size = 900F / results[i].length;
				for(int j = 0; j < results[i].length; j++)
				{
					float f = results[i][j];
					g2d.setColor(f, 0, 1 - f);
					g2d.drawEllipse(x + j * size, y, size, size);
					
					if(last && max < f)
					{
						max = f;
						maxIndx = j;
					}
				}
				y += size + 10;
			}
			g2d.setColor(Color.BLACK);
			g2d.drawString("Detected '" + maxIndx + "'", x + 20, y + 20);
			g2d.disable(G2D.G_FILL);
		}
	}
	
	public void mouse(float x, float y, boolean pressed, int button)
	{		
		if(delay > 0) return;
		
		if(!pressed && (button == MouseEvent.BUTTON1 || button == MouseEvent.BUTTON3));
		{			
			mouseMoved(x, y);
		
			if(currX != -1 && currY != -1)
			{
				results = neuralNetwork.setWeights(generateMap(drawing));
				drawing[currX][currY] = button == MouseEvent.BUTTON1;
			}
		}

		if(!pressed && button == MouseEvent.BUTTON1)
		{
			if(doneButton.contains(x, y))
			{
				results = neuralNetwork.setWeights(generateMap(drawing));
				float[] desired = new float[10];
				desired[currentNumber] = 1F;
				neuralNetwork.tweak(desired, results);
				
				amtDrawn[currentNumber]++;
				currentNumber = getRandNumber();
				clearScreen();
				delay = 20;
			}
			else if(clearButton.contains(x, y))
			{
				results = null;
				clearScreen();
				delay = 20;
			}
		}
	}
	
	public void mouseMoved(float x, float y)
	{
		currX = currY = -1;
		
		if(x >= 20 && x < 660 && y >= 20 && y < 660)
		{
			currX = (int) ((x - 20) / 40);
			currY = (int) ((y - 20) / 40);
		}
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed && key == KeyEvent.VK_X)
		{
			debug = !debug;
		}
		if(!pressed && key == KeyEvent.VK_C)
		{
			List<Integer> selected = new ArrayList<>();
			for(int i = 0; i < 16; i++)
			{
				for(int j = 0; j < 16; j++)
				{
					if(drawing[i][j])
					{
						selected.add(i);
						selected.add(j);
					}
				}
			}
			JOptionPane.showMessageDialog(null, "Copied to Clipboard!");
			String s = selected.toString();
			s = s.substring(1, s.length() - 1);
			StringSelection ss = new StringSelection("new int[] { " + s + " }");
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
		}
		if(!pressed && key == KeyEvent.VK_Q)
		{
			try
			{
				neuralNetwork.save(drawing, currentNumber);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			System.out.println("Saved [" + new Date() + "]");
			
			amtDrawn[currentNumber]++;
			currentNumber = getRandNumber();
			clearScreen();
			delay = 20;
		}
	}
	
	public static float[][] generateMap(boolean[][] drawing)
	{
		float[][] map = new float[16][16];
		for(int x = 0; x < 16; x++)
		{
			for(int y = 0; y < 16; y++)
			{
				boolean flag = drawing[x][y];
				if(flag)
				{
					for(int i = -1; i <= 1; i++)
					{
						for(int j = -1; j <= 1; j++)
						{
							int dx = x + i;
							int dy = y + j;
							
							if(dx >= 0 && dx < 16 && dy >= 0 && dy < 16)
							{
								float amt = 0F;
								if(i == 0 && j == 0)
								{
									amt = 3F / 5F;
								}
								else if(i == 0 || j == 0)
								{
									amt = 2F / 5F;
								}
								else
								{
									amt = 1F / 5F;
								}
								map[dx][dy] = MathUtil.clamp(map[dx][dy] + amt, 1F, 0F);
							}
						}
					}
				}
				
			}
		}
		return map;
	}
	
	private void clearScreen()
	{
		for(int i = 0; i < 16; i++)
		{
			for(int j = 0; j < 16; j++)
			{
				drawing[i][j] = false;
			}
		}
	}
	
	private int getRandNumber()
	{
		int min = amtDrawn[0];
		for(int i = 1; i < 10; i++)
		{
			if(amtDrawn[i] < min)
			{
				min = amtDrawn[i];
			}
		}
		List<Integer> indexes = ListUtil.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		Iterator<Integer> itr = indexes.iterator();
		while(itr.hasNext())
		{
			int n = itr.next();
			if(amtDrawn[n] != min)
			{
				itr.remove();
			}
		}
		return Rand.nextFrom(indexes);
	}

	public static void main(String[] args)
	{
		NeuralNetwork game = new NeuralNetwork();
		
		GameSettings settings = new GameSettings();
		settings.title = "Neural Network";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1600;//1280;
		settings.height = 900;//720;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		System.setProperty("Main.WIDTH", String.valueOf(settings.width));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height));
		Main.initialize(game, settings);
	}
}
