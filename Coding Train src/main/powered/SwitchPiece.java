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
package main.powered;

import java.awt.Color;

import com.hk.g2d.G2D;

public class SwitchPiece extends Piece
{
	public SwitchPiece()
	{
		super("Switch");
	}

	@Override
	public void paintPiece(G2D g2d, World world, int x, int y, int xc, int yc)
	{
		g2d.enable(G2D.G_FILL);
		g2d.setColor(Color.GREEN);
		g2d.drawRectangle(x, y, 10, 10);
		
		g2d.setColor(world.getMeta(xc, yc) == 0 ? Color.BLUE : Color.RED);
		g2d.drawRectangle(x + 3, y + 3, 4, 4);
		g2d.disable(G2D.G_FILL);
	}
	
	public int powerProvided(World world, int x, int y, Side to)
	{
		return world.getMeta(x, y) == 0 ? 0 : 15;
	}
	
	public boolean canTransfer(World world, int x, int y, Side to)
	{
		return true;
	}

	@Override
	public void onInteract(World world, int x, int y)
	{
		world.setMeta(x, y, world.getMeta(x, y) == 0 ? 1 : 0);
		world.notifyNeighbors(x, y);
	}
}
