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

public class LogicalPiece extends Piece
{
	public final boolean and;
	
	protected LogicalPiece(boolean and)
	{
		super((and ? "AND" : "OR") + " Gate");
		this.and = and;
	}
	
	public void paintPiece(G2D g2d, World world, int x, int y, int xc, int yc)
	{
		g2d.enable(G2D.G_CENTER);
		
		int meta = world.getMeta(xc, yc);
		g2d.setColor((meta & 1) != 0 ? Color.BLUE : Color.BLACK);
		g2d.drawLine(x + 0.5F, y + 0.5F, x + 9.5F, y + 0.5F);
		g2d.setColor((meta & 2) != 0 ? Color.BLUE : Color.BLACK);
		g2d.drawLine(x + 0.5F, y + 9.5F, x + 9.5F, y + 9.5F);

		g2d.setColor((and ? meta == 3 : (meta & 3) != 0) ? Color.BLUE : Color.BLACK);
		g2d.drawLine(x + 9.5F, y + 0.5F, x + 9.5F, y + 9.5F);

		g2d.setColor(Color.BLACK);
		g2d.drawLine(x + 0.5F, y + 0.5F, x + 0.5F, y + 9.5F);
		g2d.drawString(and ? "x" : "+", x + 5, y + 2 - (and ? 1 : 0));
		g2d.disable(G2D.G_CENTER);
	}
	
	public boolean onAdded(World world, int x, int y)
	{
		onNeighborChanged(world, x, y, null);
		return super.onAdded(world, x, y);
	}
	
	public void onNeighborChanged(World world, int x, int y, Side side)
	{
		Piece i1 = world.getPiece(x, y + Side.NORTH.yOff);
		Piece i2 = world.getPiece(x, y + Side.SOUTH.yOff);
		
		int meta = world.getMeta(x, y);
		int old = meta;
		meta = i1.powerProvided(world, x, y + Side.NORTH.yOff, Side.SOUTH) > 0 ? meta | 1 : meta & ~1;
		meta = i2.powerProvided(world, x, y + Side.SOUTH.yOff, Side.NORTH) > 0 ? meta | 2 : meta & ~2;
		if(meta != old)
		{
			world.setMeta(x, y, meta);
			world.notifyNeighbors(x, y);
		}
	}
	
	public int powerProvided(World world, int x, int y, Side to)
	{
		boolean flag;
		if(and)
		{
			flag = world.getMeta(x, y) == 3;
		}
		else
		{
			flag = (world.getMeta(x, y) & 3) != 0;
		}
		return to == Side.EAST && flag ? 15 : 0;
	}
	
	public boolean canTransfer(World world, int x, int y, Side to)
	{
		return to != Side.WEST;
	}
}
