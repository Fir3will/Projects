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
	
	public Side getClockwise()
	{
		switch(this)
		{
			case NORTH: return EAST;
			case EAST: return SOUTH;
			case SOUTH: return WEST;
			case WEST: return NORTH;
		}
		throw new AssertionError(this);
	}
	
	public Side getCClockwise()
	{
		switch(this)
		{
			case NORTH: return WEST;
			case EAST: return NORTH;
			case SOUTH: return EAST;
			case WEST: return SOUTH;
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
