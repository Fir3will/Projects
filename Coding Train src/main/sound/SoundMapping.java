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
package main.sound;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.TargetDataLine;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.PrimitiveUtil;

public class SoundMapping extends GuiScreen
{
	private float zoom = 1;
	private final float[] heights;
	private final AudioFormat fmt;
	private final Clip clip;
	
	public SoundMapping(Game game) throws Exception
	{
		super(game);
		
		AudioInputStream ais = AudioSystem.getAudioInputStream(SoundMapping.class.getResource("/main/sound/test.wav"));
		fmt = ais.getFormat();
		TargetDataLine dl = AudioSystem.getTargetDataLine(fmt);
		int bufSize = 1024;
		int r = 0;
		byte[] bs = new byte[bufSize];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		dl.open(ais.getFormat(), bufSize);
		dl.start();
		while((r = ais.read(bs, 0, bufSize)) > 0)
		{
			baos.write(bs, 0, r);
		}
		dl.stop();
		dl.close();
		bs = baos.toByteArray();
		heights = new float[bs.length / 16];
		for(int i = 0; i < heights.length; i++)
		{
			heights[i] = PrimitiveUtil.bytesToShort(i * 16, bs) / 100F;
		}
		clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
		clip.open(fmt, bs, 0, bs.length);
		clip.start();
	}
	
	@Override
	public void update(double delta)
	{}

	@Override
	public void paint(G2D g2d)
	{
		int indx = clip.getFramePosition() / fmt.getFrameSize();

		g2d.setColor(Color.RED);
		g2d.drawLine(g2d.width / 2, 0, g2d.width / 2, g2d.height);
		double ax = (g2d.width - (g2d.width * zoom)) / 2D;
		double ay = (g2d.height - (g2d.height * zoom)) / 2D;
		g2d.translate(ax, ay);
		g2d.scale(zoom, zoom);
		g2d.translate(g2d.width / 2 - indx, g2d.height / 2);
		
		g2d.setColor(Color.BLACK);
		for(int i = (int) Math.max(0, indx - (1200 / zoom)); i < Math.min(heights.length - 1, indx + (1200 / zoom)); i++)
		{
			g2d.drawLine(i, heights[i] / zoom, i + 1, heights[i + 1] / zoom);
		}
	}
	
	public void key(int keyCode, char keyChar, boolean pressed)
	{
		if(keyCode == KeyEvent.VK_SPACE)
		{
			if(pressed)
				return;
			
			if(clip.isRunning())
				clip.stop();
			else
				clip.start();
		}
		else if(!pressed)
		{
			if(keyCode == KeyEvent.VK_R)
			{
				clip.setMicrosecondPosition(0);
				clip.start();
			}
		}
	}
	
	public void mouseWheel(int amt)
	{
		zoom *= 1F - amt * 0.1F;
	}

	public static void main(String[] args) throws Exception
	{
		Settings settings = new Settings();
		settings.title = "Sound Dancer";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1200;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new SoundMapping(frame.game));
		frame.launch();
	
	}
}
