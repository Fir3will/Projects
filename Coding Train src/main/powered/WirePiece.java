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

public class WirePiece extends Piece
{
	public WirePiece()
	{
		super("Wire");
	}

	@Override
	public void paintPiece(G2D g2d, World world, int x, int y, int xc, int yc)
	{
		g2d.setColor(Color.BLACK);
		
		int meta = world.getMeta(xc, yc);
		int n1 = 4;
		int n2 = 10 - n1;

		if((meta & 1 << Side.EAST.ordinal() + 4) != 0)
		{
			g2d.drawLine(x + n2, y + n1, x + 10, y + n1);
			g2d.drawLine(x + n2, y + n2, x + 10, y + n2);
		}
		else
		{
			g2d.drawLine(x + n2, y + n1, x + n2, y + n2);
		}

		if((meta & 1 << Side.WEST.ordinal() + 4) != 0)
		{
			g2d.drawLine(x, y + n1, x + n1, y + n1);
			g2d.drawLine(x, y + n2, x + n1, y + n2);
		}
		else
		{
			g2d.drawLine(x + n1, y + n1, x + n1, y + n2);
		}

		if((meta & 1 << Side.NORTH.ordinal() + 4) != 0)
		{
			g2d.drawLine(x + n1, y, x + n1, y + n1);
			g2d.drawLine(x + n2, y, x + n2, y + n1);
		}
		else
		{
			g2d.drawLine(x + n1, y + n1, x + n2, y + n1);
		}

		if((meta & 1 << Side.SOUTH.ordinal() + 4) != 0)
		{
			g2d.drawLine(x + n1, y + n2, x + n1, y + 10);
			g2d.drawLine(x + n2, y + n2, x + n2, y + 10);
		}
		else
		{
			g2d.drawLine(x + n1, y + n2, x + n2, y + n2);
		}

		g2d.setColor(clrs[meta & 0xF]);
		g2d.drawLine(x + 5, y + 5, x + 5, y + 5);
		
		for(int i = 0; i < Side.size(); i++)
		{
			Side s = Side.get(i);
			if((meta & 1 << i + 4) != 0)
			{
				g2d.drawLine(x + 5, y + 5, x + 5 + 5 * s.xOff, y + 5 + 5 * s.yOff);
			}
		}
	}
	
	public boolean onAdded(World world, int x, int y)
	{
		int meta = 0;
		for(int i = 0; i < Side.size(); i++)
		{
			Side side = Side.get(i);
			Piece p = world.getPiece(x + side.xOff, y + side.yOff);
			if(p.canTransfer(world, x, y, side.getOpposite()))
			{
				meta |= 1 << side.ordinal() + 4;
			}
		}
		meta &= ~0xF;
		meta |= getPower(world, x, y);
		world.setMeta(x, y, meta);
		return true;
	}
	
	public void onNeighborChanged(World world, int x, int y, Side side)
	{
		Piece p = world.getPiece(x + side.xOff, y + side.yOff);
		
		int meta = world.getMeta(x, y);
		int old = meta;
		if(p.canTransfer(world, x + side.xOff, y + side.yOff, side.getOpposite()))
		{
			meta |= 1 << side.ordinal() + 4;
		}
		else
		{
			meta &= ~(1 << side.ordinal() + 4);
		}
		meta &= ~0xF;
		meta |= getPower(world, x, y);
		if(meta != old)
		{
			world.setMeta(x, y, meta);
			world.notifyNeighbors(x, y);
		}
	}
	
	private int getPower(World world, int x, int y)
	{
		int pwr = 0;
		for(int i = 0; i < Side.size(); i++)
		{
			Side side = Side.get(i);
			Piece p = world.getPiece(x + side.xOff, y + side.yOff);
			if(p.canTransfer(world, x + side.xOff, y + side.yOff, side.getOpposite()))
			{
				int pv = p.powerProvided(world, x + side.xOff, y + side.yOff, side.getOpposite());
				if(p instanceof WirePiece)
					pv--;
				pwr = Math.max(pwr, pv);
			}
		}
		return pwr & 0xF;
	}
	
	public int powerProvided(World world, int x, int y, Side to)
	{
		return world.getMeta(x, y) & 0xF;
	}
	
	public boolean canTransfer(World world, int x, int y, Side to)
	{
		return true;
	}
	
	private static final Color[] clrs = new Color[16];
	static
	{
		clrs[0] = Color.GRAY;
		for(int i = 1; i < 16; i++)
		{
			int c = 255 - (15 - i) * 10;
			clrs[i] = new Color(0, 0, c);
		}
	}
}
