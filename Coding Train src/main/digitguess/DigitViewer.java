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

import static main.digitguess.DigitGuess.gh;
import static main.digitguess.DigitGuess.gw;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.hk.array.Concat;
import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.io.stream.InStream;
import com.hk.io.stream.Stream;
import com.hk.neuralnetwork.Mat;
import com.hk.neuralnetwork.NeuralNetwork;
import com.hk.neuralnetwork.NeuralNetwork.ActivationFunction;

public class DigitViewer extends GuiScreen
{
	private static final int scl = 4;
	private List<double[][]>[] drawings;
	private int tilt, max;
	private NeuralNetwork neuralNetwork;
	
	@SuppressWarnings("unchecked")
	public DigitViewer(Game game)
	{
		super(game);
		
		neuralNetwork = new NeuralNetwork(gw * gh, 2, 16, 10);
		neuralNetwork.setActivationFunction(new ActivationFunction(Mat.TANH, Mat.TANH_DERIVATIVE));
		
		tilt = max = 0;
		drawings = new List[10];
		try
		{
			for(int i = 0; i < 10; i++)
			{
				File f = new File("src/main/digitguess/digits/" + i + ".dat");
				if(!f.exists())
					continue;
				
				Stream in = new InStream(new FileInputStream(f), false);
				int size = in.readInt();
				List<double[][]> lst = new ArrayList<>(size);
				for(int j = 0; j < size; j++)
				{
					double[][] map = DigitGuess.generateMap(DigitGuess.getMap(in));
					lst.add(map);

					double[] mp = Concat.concat(map);
					double[] correct = new double[10];
					correct[i] = 1;
					neuralNetwork.train(mp, correct);
				}
				max = Math.max(size, max);
				in.close();
				drawings[i] = lst;
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(double delta)
	{
		
	}

	@Override
	public void paint(G2D g2d)
	{
		for(int i = 0; i < 10; i++)
		{
			List<double[][]> lst = drawings[i];
			if(tilt >= lst.size())
				continue;

			int x = i % 5;
			int y = i / 5;
			float w = gw * scl, h = gh * scl;
			g2d.pushMatrix();
			g2d.translate(x * (w + 10) + 10, y * (h + 40) + 10);
			double[][] map = lst.get(tilt);
			g2d.enable(G2D.G_FILL);
			for(int x1 = 0; x1 < gw; x1++)
			{
				for(int y1 = 0; y1 < gh; y1++)
				{
					float f = (float) map[x1][y1];
					g2d.setColor(f, f, f);
					g2d.drawRect(x1 * scl, y1 * scl, scl, scl);
				}
			}
			g2d.disable(G2D.G_FILL);
			g2d.setColor(Color.WHITE);
			g2d.drawRect(0, 0, w, h);
			
			g2d.enable(G2D.G_CENTER);
			double[] result = neuralNetwork.process(Concat.concat(map));
			int indx = 0;
			for(int i2 = 1; i2 < 10; i2++)
			{
				if(result[i2] > result[indx])
					indx = i2;
			}
			g2d.drawString(indx, w / 2, h + 15);
			g2d.disable(G2D.G_CENTER);

			g2d.popMatrix();
		}
		g2d.drawString("Page " + (tilt + 1) + "/" + max, 10, g2d.height - 10);
	}

	public void mouseWheel(int amt)
	{
		tilt += amt;
		while(tilt < 0)
			tilt += max;
		while(tilt >= max)
			tilt -= max;
	}

	public static void main(String[] args)
	{		
		Settings settings = new Settings();
		settings.title = "Digit Viewer";
		settings.quality = Quality.POOR;
		settings.width = gw * scl * 5 + 60;
		settings.height = gw * scl * 2 + 110;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new DigitViewer(frame.game));
		frame.launch();
	}
}
