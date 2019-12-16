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
package com.hk.g2d;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.hk.math.FPSCounter;

public class GameFrame extends JPanel implements ActionListener
{
	public final Game game;
	public final JFrame window;
	private final Settings settings;
	private final SwingHandler handler;
	private final Timer timer;
	private final FPSCounter ctr;
	
	private GameFrame(JFrame frame, Settings settings)
	{
		this.window = frame;
		this.settings = settings;
		handler = new SwingHandler();
		game = new Game(settings, handler);
		handler.game = game;
		ctr = new FPSCounter(30);
		setBackground(settings.background);
		
		timer = new Timer(settings.maxFPS <= 0 ? 0 : 1000 / settings.maxFPS, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		ctr.calc();
		if(settings.showFPS)
		{
			window.setTitle(settings.title + (settings.version != null ? " v" + settings.version : "") + " [" + ((int) (ctr.getFPS() * 100) / 100D) + "]");
		}
		
		handler.frameWidth = getWidth();
		handler.frameHeight = getHeight();
		handler.update();
		game.update();
		
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(settings.refresh)
			super.paintComponent(g);
		
		game.paint((Graphics2D) g, getWidth(), getHeight());
	}
	
	public void launch()
	{
		window.setVisible(true);
		timer.start();
	}

	public static GameFrame create(Settings settings)
	{
		JFrame frame = new JFrame(settings.title + (settings.version != null ? " v" + settings.version : ""));
		final GameFrame gameFrame = new GameFrame(frame, settings);
		
		frame.setSize(settings.frame == null ? new Dimension(settings.width, settings.height) : settings.frame);
		frame.setContentPane(gameFrame);

		frame.setDefaultCloseOperation(settings.closeOperation);
		frame.setLocationRelativeTo(null);

		frame.addKeyListener(gameFrame.handler);
		gameFrame.addMouseListener(gameFrame.handler);
		gameFrame.addMouseMotionListener(gameFrame.handler);
		gameFrame.addMouseWheelListener(gameFrame.handler);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				gameFrame.timer.stop();
			}
		});
		return gameFrame;
	}

	private static final long serialVersionUID = 922996320891858208L;
}
