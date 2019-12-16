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
package main.xorwithnn;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.neuralnetwork.Mat;
import com.hk.neuralnetwork.NeuralNetwork;
import com.hk.neuralnetwork.NeuralNetwork.ActivationFunction;

public class XORWithNN extends GuiScreen
{
	private NeuralNetwork nn;
	
	public XORWithNN(Game game)
	{
		super(game);
		reset();
	}
	
	public void reset()
	{
		nn = new NeuralNetwork(2, 3, 1);
		nn.setActivationFunction(new ActivationFunction(Mat.TANH, Mat.TANH_DERIVATIVE));
	}

	@Override
	public void update(double delta)
	{
		for(int i = 0; i < 50; i++)
		{
			int n = Rand.nextInt(5);
			n = n == 4 ? i : n;
			int a = (n % 4) % 2;
			int b = (n % 4) / 2;

			double[] input = { a, b };
			double[] correct = { a ^ b };
			nn.train(input, correct);
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		int scl = 25;
		int w = g2d.width / scl;
		int h = g2d.height / scl;

		g2d.enable(G2D.G_FILL);
		for(int x = 0; x < w; x++)
		{
			for(int y = 0; y < h; y++)
			{
				float f = 0;

				double a = x / (double) (w - 1);
				double b = y / (double) (h - 1);
				
				f = (float) nn.process(new double[] { a, b })[0];
				f = MathUtil.between(0, f, 1);
				g2d.setColor(f, f, f);
				g2d.drawRectangle(x * scl, y * scl, scl, scl);
			}
		}
		g2d.disable(G2D.G_FILL);
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		if(!pressed && key == KeyEvent.VK_SPACE)
		{
			reset();
		}
	}
	
	public static void main(String[] args)
	{	
		Settings settings = new Settings();
		settings.title = "XOR with Neural Networks";
		settings.version = "0.0.1";
		settings.quality = Quality.POOR;
		settings.width = 800;
		settings.height = 800;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new XORWithNN(frame.game));
		frame.launch();
	}
}
