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
package main.runespace;

public enum Direction
{
	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0);

	public final int xOff, yOff;
	public final char letter;

	Direction(int x, int y)
	{
		xOff = x;
		yOff = y;
		letter = name().charAt(0);
	}

	public static int size()
	{
		return 4;
	}

	public static Direction get(int i)
	{
		return values()[i];
	}
}
