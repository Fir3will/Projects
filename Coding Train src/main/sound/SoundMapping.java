package main.sound;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.TargetDataLine;

import com.hk.math.PrimitiveUtil;
import com.sun.glass.events.KeyEvent;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class SoundMapping extends Game
{
	private float zoom = 1;
	private final float[] heights;
	private final AudioFormat fmt;
	private final Clip clip;
	
	public SoundMapping() throws Exception
	{
		AudioInputStream ais = AudioSystem.getAudioInputStream(new File("c304-2.wav"));
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
	
	public void initialize()
	{}

	@Override
	public void update(int ticks)
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
			clip.stop();
		}
	}
	
	public void mouseWheel(int amt)
	{
		zoom *= 1F - amt * 0.1F;
	}

	public static void main(String[] args) throws Exception
	{
		System.setProperty("Main.WIDTH", String.valueOf(1200));
		System.setProperty("Main.HEIGHT", String.valueOf(900));
		SoundMapping game = new SoundMapping();
		
		GameSettings settings = new GameSettings();
		settings.title = "Sound Dancer";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1200;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;
		Main.initialize(game, settings);
	
	}
}
