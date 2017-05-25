package main;

public abstract class Game
{
	int ticks;
	Handler handler;

	public void initialize()
	{}

	public abstract void update(int ticks);

	public abstract void paint(G2D g2d);

	public final Handler getHandler()
	{
		return handler;
	}

	public int getTicks()
	{
		return ticks;
	}

	public void mouse(float x, float y, boolean pressed, int button)
	{}

	public void mouseMoved(float x, float y)
	{}

	public void mouseWheel(int amt)
	{}

	public void key(int key, char keyChar, boolean pressed)
	{}
}
