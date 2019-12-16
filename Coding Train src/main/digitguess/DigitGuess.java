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
package main.digitguess;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.hk.array.Concat;
import com.hk.collections.lists.ListUtil;
import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.io.stream.InStream;
import com.hk.io.stream.OutStream;
import com.hk.io.stream.Stream;
import com.hk.math.FloatMath;
import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.neuralnetwork.Mat;
import com.hk.neuralnetwork.NeuralNetwork;
import com.hk.neuralnetwork.NeuralNetwork.ActivationFunction;

public class DigitGuess extends GuiScreen
{
	private final int[] amtDrawn = new int[10];
	static final int gw = 32, gh = gw, scl = 12;
	private final boolean[][] drawing = new boolean[gw][gh];
	private double[][] map;
	private float px, py;
	private int currentNumber, currentGuess;
	private NeuralNetwork neuralNetwork;
	
	public DigitGuess(Game game)
	{
		super(game);
		
		neuralNetwork = new NeuralNetwork(gw * gh, 2, 16, 10);
		neuralNetwork.setActivationFunction(new ActivationFunction(Mat.TANH, Mat.TANH_DERIVATIVE));
		
		currentNumber = getRandNumber();
		currentGuess = -1;
		map = new double[gw][gh];
		px = py = -1;
		
		try
		{
			for(int i = 0; i < 10; i++)
			{
				File f = new File("src/main/digitguess/digits/" + i + ".dat");
				if(!f.exists())
					continue;
				
				Stream in = new InStream(new FileInputStream(f), false);
				int size = in.readInt();
				for(int j = 0; j < size; j++)
				{
					double[] mp = Concat.concat(generateMap(getMap(in)));
					double[] correct = new double[10];
					correct[i] = 1;
					neuralNetwork.train(mp, correct);
				}
				amtDrawn[i] = size;
				in.close();
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		System.out.println(Arrays.toString(amtDrawn));
	}

	@Override
	public void update(double delta)
	{
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.enable(G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawRect(7, 7, gw * scl + 6, gh * scl + 6);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(10, 10, gw * scl, gh * scl);

		g2d.setColor(Color.WHITE);
		g2d.translate(10, 10);
		for(int i = 0; i < gw; i++)
		{
			for(int j = 0; j < gh; j++)
			{
				float f = (float) map[i][j];
				g2d.setColor(f, f, f);
				g2d.drawRect(i * scl, j * scl, scl, scl);
			}
		}
		g2d.disable(G2D.G_FILL);
		g2d.translate(gw * scl + 10, -10);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Draw a" + (currentNumber == 8 ? "n" : "") + " " + currentNumber + ".", 5, 15);
		g2d.drawString("Current guess is " + currentGuess + ".", 5, 30);
	}

	public static boolean[][] getMap(Stream in) throws Exception
	{
		boolean[][] arr = new boolean[gw][gh];
		for(int x = 0; x < 32; x++)
		{
			int i = in.readInt();
			for(int y = 0; y < 32; y++)
			{
				arr[x][y] = (i & (1 << y)) != 0;
			}
		}
		return arr;
	}
	
	public static double[][] generateMap(boolean[][] drawing)
	{
		double[][] map = new double[gw][gh];
		for(int x = 0; x < gw; x++)
		{
			for(int y = 0; y < gh; y++)
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
							
							if(dx >= 0 && dx < gw && dy >= 0 && dy < gh)
							{
								double amt = 0D;
								if(i == 0 && j == 0)
								{
									amt = 3D / 5D;
								}
								else if(i == 0 || j == 0)
								{
									amt = 2D / 5D;
								}
								else
								{
									amt = 1D / 5D;
								}
								map[dx][dy] = MathUtil.between(0, map[dx][dy] + amt, 1);
							}
						}
					}
				}
			}
		}
		return map;
	}
	
	@Override
	public void mouse(float x, float y, boolean pressed)
	{
		int button = game.handler.getButton();
		
		if(button == MouseEvent.BUTTON1)
		{
			if(pressed)
			{
				if(px == -1 || py == -1)
				{
					px = x;
					py = y;
				}
				float len = FloatMath.ceil(MathUtil.hypot(x - px, y - py));
				for(int i = 0; i < len; i++)
				{
					float f = i / (len - 1F);
					float x1 = px * f + x * (1 - f);
					float y1 = py * f + y * (1 - f);
					if(x1 >= 10 && x1 < gw * scl + 10 && y1 >= 10 && y1 < gh * scl + 10)
					{
						x1 -= 10;
						y1 -= 10;
						drawing[(int) (x1 / scl)][(int) (y1 / scl)] = true;
					}
				}
				px = x;
				py = y;
				map = generateMap(drawing);
				double[] result = neuralNetwork.process(Concat.concat(map));
				int indx = 0;
				for(int i = 1; i < 10; i++)
				{
					if(result[i] > result[indx])
						indx = i;
				}
				currentGuess = indx;
			}
			else
				px = py = -1;
		}
		else if(button == MouseEvent.BUTTON2 || button == MouseEvent.BUTTON3)
		{
			if(pressed)
				return;
			
			if(button == MouseEvent.BUTTON3)
			{
				submit();
				amtDrawn[currentNumber]++;
			}
			clearScreen();
			currentNumber = getRandNumber();
		}
	}
	
	private void submit()
	{
		try
		{
			File fs = new File("src/main/digitguess/digits/" + currentNumber + ".dat");
			fs.getParentFile().mkdirs();
			if(!fs.exists())
			{
				fs.createNewFile();
				Stream out = new OutStream(new FileOutputStream(fs), false);
				out.writeInt(0);
				out.close();
			}
			InputStream fin = new FileInputStream(fs);
			Stream in = new InStream(fin, false);
			int size = in.readInt();
			byte[] prev = new byte[size * 128];
			if(size > 0)
			{
				fin.read(prev);
			}
			in.close();
			
			FileOutputStream fout = new FileOutputStream(fs);
			Stream out = new OutStream(fout, false);
			out.writeInt(size + 1);
			fout.write(prev);
			for(int x = 0; x < 32; x++)
			{
				int i = 0;
				for(int y = 0; y < 32; y++)
				{
					if(drawing[x][y])
						i |= 1 << y;
				}
				out.writeInt(i);
			}
			out.close();

			double[] mp = Concat.concat(map);
			double[] correct = new double[10];
			correct[currentNumber] = 1;
			neuralNetwork.train(mp, correct);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void clearScreen()
	{
		for(int i = 0; i < gw; i++)
		{
			for(int j = 0; j < gh; j++)
			{
				drawing[i][j] = false;
			}
		}
		map = new double[gw][gh];
		currentGuess = -1;
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
		Settings settings = new Settings();
		settings.title = "Digit Guesser";
		settings.quality = Quality.POOR;
		settings.width = gw * scl + 20 + 200;
		settings.height = gh * scl + 20;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new DigitGuess(frame.game));
		frame.launch();
	}
}