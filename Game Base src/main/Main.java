package main;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import main.GameSettings.Quality;

public class Main extends JPanel implements ActionListener
{
	public static final int WIDTH = Integer.getInteger("Main.WIDTH", 1920), HEIGHT = Integer.getInteger("Main.HEIGHT", 1080);
	private final GameSettings settings;
	private final Game game;
	private final G2D g2d;
	private final Handler handler;
	private final Timer timer;
	private int fps;
	private long lastFPSCheck;

	public Main(Game game, GameSettings settings)
	{
		super(true);
		this.game = game;
		this.settings = settings;
		setBackground(settings.background);

		g2d = new G2D(WIDTH, HEIGHT);
		game.handler = handler = new Handler(game);
		timer = new Timer((int) (1000F / settings.maxFPS), this);
		lastFPSCheck = System.currentTimeMillis();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (settings.constantGCCalls)
		{
			System.gc();
		}
		handler.w = getWidth();
		handler.h = getHeight();
		handler.update();
		game.update(game.ticks++);
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g2d.g2d = (Graphics2D) g;

		fps++;
		if (fps % 30 == 0)
		{
			long time = System.currentTimeMillis();
			long elapsed = time - lastFPSCheck;
			double fps = 1D / (elapsed / 1000D / 30D);
			fps *= 100;
			fps = (int) fps;
			fps /= 100D;

			lastFPSCheck = time;

			if (settings.showFPS)
			{
				frame.setTitle(settings.title + (settings.version != null ? " v" + settings.version : "") + " [" + fps + "]");
			}
		}

		if (settings.quality == Quality.GOOD)
		{
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		}
		else if (settings.quality == Quality.AVERAGE)
		{
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		}
		else if (settings.quality == Quality.POOR)
		{
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			g2d.g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		}

		g2d.scale(getWidth() / (float) WIDTH, getHeight() / (float) HEIGHT);
		g2d.pushMatrix();
		game.paint(g2d);
		g2d.popMatrix();
		g2d.g2d = null;
	}

	public static void initialize(Game game, GameSettings settings)
	{
		if (initialized)
		{
			throw new UnsupportedOperationException("Initialized Already");
		}
		initialized = true;
		settings = new GameSettings(settings);
		frame = new JFrame(settings.title + (settings.version != null ? " v" + settings.version : ""));

		if (!settings.fullscreen)
		{
			frame.setResizable(settings.resizeable);
			frame.setSize(settings.width, settings.height);
		}
		else
		{
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}

		Main main = new Main(game, settings);
		frame.addKeyListener(main.handler);
		main.addMouseListener(main.handler);
		main.addMouseMotionListener(main.handler);
		main.addMouseWheelListener(main.handler);
		game.initialize();

		frame.add(main);

		frame.setDefaultCloseOperation(settings.closeOperation);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				initialized = false;
			}
		});

		main.timer.start();
	}

	public static JFrame getFrame()
	{
		if (!initialized)
		{
			throw new UnsupportedOperationException("Not Initialized Yet");
		}
		return frame;
	}

	private static JFrame frame;
	private static boolean initialized = false;
	private static final long serialVersionUID = 1L;
}