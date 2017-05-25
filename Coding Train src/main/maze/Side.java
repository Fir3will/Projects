package main.maze;

public enum Side
{
	NORTH(0, 1),
	SOUTH(0, -1),
	WEST(-1, 0),
	EAST(1, 0);
	
	public final int xOff, yOff;
	
	private Side(int xOff, int yOff)
	{
		this.xOff = xOff;
		this.yOff = yOff;
	}
	
	public Side getOpposite()
	{
		switch(this)
		{
			case NORTH: return SOUTH;
			case SOUTH: return NORTH;
			case WEST: return EAST;
			case EAST: return WEST;
		}
		throw new AssertionError(this);
	}
	
	public static Side get(int index)
	{
		return values()[index];
	}
	
	public static int size()
	{
		return values().length;
	}
}
