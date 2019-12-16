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
package main.tictactoe;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.io.stream.InStream;
import com.hk.io.stream.OutStream;
import com.hk.io.stream.Stream;
import com.hk.math.Rand;
import com.hk.neuralnetwork.NeuralNetwork;

public class TicTacToe extends GuiScreen
{
	public int playerWins = 0;
	public int cpuWins = 0;
	private int turn, win;
	private int[] winIndxs;
	public final NeuralNetwork brain;
	public final int[] grid;
	private long wonTime = -1;
	
	public TicTacToe(Game game)
	{
		super(game);
		brain = new NeuralNetwork(9, 2, 13, 9);
		brain.setLearningRate(0.1);
		grid = new int[9];
		reset();
		load();
	}
	
	public void reset()
	{
		Arrays.fill(grid, 0);
		turn = Rand.nextFloat() < 0.25 ? -1 : 1;
		wonTime = -1;
		win = 0;
		winIndxs = null;
	}
	
	@Override
	public void update(double delta)
	{
		if(wonTime == -1)
		{
			int[] xWin = getWin(1);
			int[] oWin = getWin(-1);
			
			if(xWin != null || oWin != null)
			{
				wonTime = System.currentTimeMillis();
				win = xWin == null ? 1 : -1;
				winIndxs = xWin == null ? oWin : xWin;
				turn = 0;

				playerWins += win == -1 ? 0 : 1;
				cpuWins += win == -1 ? 1 : 0;
			}
			else if(isDraw())
			{
				wonTime = System.currentTimeMillis();
				turn = 0;
			}
		}
		if(turn == 1)
		{
			double[] input = getArray(false);
			double[] output = brain.process(input);
			
			double max = Double.MIN_VALUE;
			int indx = -1;
			for(int i = 0; i < 9; i++)
			{
				if(grid[i] == 0 && output[i] > max)
				{
					max = output[i];
					indx = i;
				}
			}
			Arrays.fill(output, 0);
			output[indx] = 1;
			max = (int) (max * 1000) / 10D;
			System.out.println("-------------Did: " + max + "% sure");
			printGrids(input, output);

			grid[indx] = 1;
			turn = -1;
			
			int[] won = getWin(1);
			if(won != null) brain.train(input, output);
		}
	}
	
	public void printGrids(double[] a, double[] b)
	{
		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 3; x++)
			{
				if(a[x + y * 3] == 0) System.out.print("_");
				else System.out.print(a[x + y * 3] > 0 ? "X" : "O");
			}
			System.out.print(y == 1 ? "->" : "  ");
			for(int x = 0; x < 3; x++)
			{	
				if(b[x + y * 3] == 0) System.out.print("_");
				else System.out.print(b[x + y * 3] > 0 ? "X" : "O");
			}
			System.out.println();
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setColor(Color.WHITE);
		g2d.drawRectangle(10, 10, 600, 600);
		
		for(int i = 1; i < 3; i++)
		{
			g2d.drawLine(10, 10 + i * 200, 610, 10 + i * 200);
			g2d.drawLine(10 + i * 200, 10, 10 + i * 200, 610);
		}

		Rectangle r = new Rectangle();
		for(int x = 0; x < 3; x++)
		{
			for(int y = 0; y < 3; y++)
			{
				r.x = 10 + x * 200;
				r.y = 10 + y * 200;
				r.width = r.height = 200;
				
				r.grow(-50, -50);
				int mark = get(x, y);

				if(mark == -1)
				{
					g2d.drawEllipse(r.x, r.y, r.width, r.height);
				}
				else if(mark == 1)
				{
					g2d.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
					g2d.drawLine(r.x, r.y + r.height, r.x + r.width, r.y);
				}
			}
		}
		
		if(winIndxs != null)
		{
			g2d.setColor(win == 1 ? Color.GREEN : Color.RED);
			float px = 110 + (winIndxs[0] % 3) * 200;
			float py = 110 + (winIndxs[0] / 3) * 200;
			for(int i = 1; i < winIndxs.length; i++)
			{
				int indx = winIndxs[i];
				r.x = 10 + (indx % 3) * 200;
				r.y = 10 + (indx / 3) * 200;
				r.width = r.height = 200;

				g2d.drawLine(r.getCenterX(), r.getCenterY(), px, py);
				
				px = r.x + r.width / 2F;
				py = r.y + r.height / 2F;
			}
		}
		else if(isDraw()) g2d.setColor(Color.YELLOW);
		
		g2d.enable(G2D.G_FILL);
		r.setBounds(620, 10, g2d.width - 630, g2d.height - 20);
		g2d.drawRectangle(r.x, r.y, r.width, r.height);
		g2d.setColor(Color.BLACK);
		g2d.disable(G2D.G_FILL);

		
		g2d.setFontSize(36F);
		g2d.enable(G2D.G_CENTER);
		g2d.drawString("Tic Tac Toe", r.getCenterX(), r.y + 18);
		g2d.disable(G2D.G_CENTER);
		
		g2d.setFontSize(24F);
		String msg = "Player Wins: " + playerWins + "\nCPU Wins: " + cpuWins + "\n";
		
		if(turn == 0)
		{
			if(win == 1) msg += "You Won!\nYou pretty much beat yourself";
			else if(win == 0) msg += "Draw!\nIt looks like you're as smart as\nyourself";
			else if(win == -1) msg += "The CPU Won!\nYou pretty much got beat by\nyourself";
		}
		else
		{
			msg += (turn == -1 ? "Player's " : "Cpu's") + " Turn";
		}
		g2d.drawString(msg, r.x + 9, r.y + 70);
	}
	
	public void mouse(float x, float y, boolean pressed)
	{
		if(pressed) return;
		
		if(wonTime != -1 && System.currentTimeMillis() - wonTime > 120)
		{
			reset();
		}
		else if(x > 10 && x < 610 && y > 10 && y < 610)
		{
			int x1 = (int) ((x - 10) / 200);
			int y1 = (int) ((y - 10) / 200);

			if(get(x1, y1) == 0 && turn == -1)
			{
				turn = 1;
				double[] input = getArray(true);
				set(x1, y1, -1);
				double[] output = new double[9];
				output[x1 + y1 * 3] = 1;
				System.out.println("-------------Taught:");
				printGrids(input, output);
				
				brain.train(input, output);
			}
		}
	}
	
	public boolean isDraw()
	{
		for(int i = 0; i < 9; i++)
		{
			if(grid[i] == 0) return false;
		}
		return true;
	}
	
	public int[] getWin(int num)
	{
		for(int i = 0; i < 3; i++)
		{
			if(get(i, 0) == num && get(i, 1) == num && get(i, 2) == num)
			{
				return new int[] { i + 0, i + 3, i + 6 };
			}
			
			if(get(0, i) == num && get(1, i) == num && get(2, i) == num)
			{
				i *= 3;
				return new int[] { i + 0, i + 1, i + 2 };
			}
		}
		
		if(get(0, 0) == num && get(1, 1) == num && get(2, 2) == num)
		{
			return new int[] { 0, 4, 8 };
		}
		
		if(get(2, 0) == num && get(1, 1) == num && get(0, 2) == num)
		{
			return new int[] { 2, 4, 6 };
		}
		
		return null;
	}
	
	public double[] getArray(boolean flip)
	{
		double[] input = new double[9];
		for(int i = 0; i < 9; i++)
		{
			input[i] = flip ? -grid[i] : grid[i];
		}
		return input;
	}
	
	public int get(int x, int y)
	{
		return grid[x + y * 3];
	}
	
	public void set(int x, int y, int num)
	{
		grid[x + y * 3] = num;
	}
	
	public void load()
	{
		System.out.println("Loading...");
		try
		{
			File saveFile = new File("tictactoe_brain.dat");
			
			if(saveFile.exists())
			{
				FileInputStream fin = new FileInputStream(saveFile);
				Stream in = new InStream(fin);
				cpuWins = in.readInt();
				playerWins = in.readInt();
				brain.readFrom(in);
				in.close();
				fin.close();
			}
		}
		catch (Exception e) { throw new RuntimeException(e); }
	}
	
	public void save()
	{
		System.out.println("Saving...");
		try
		{
			File saveFile = new File("tictactoe_brain.dat");
			
			if(saveFile.exists())
			{
				saveFile.delete();
				saveFile.createNewFile();
			}
			
			FileOutputStream fout = new FileOutputStream(saveFile);
			Stream out = new OutStream(fout);
			out.writeInt(cpuWins);
			out.writeInt(playerWins);
			brain.writeTo(out);
			out.close();
			fout.close();
		}
		catch (Exception e) { throw new RuntimeException(e); }
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "Tic Tac Toe";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = 620;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		final TicTacToe game = new TicTacToe(frame.game);
		frame.game.setCurrentScreen(game);
		frame.window.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				game.save();
			}
		});
		frame.launch();
	}
}
